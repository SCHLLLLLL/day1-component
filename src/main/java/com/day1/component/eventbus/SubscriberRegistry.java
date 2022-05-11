package com.day1.component.eventbus;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.*;
import com.google.common.primitives.Primitives;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.UncheckedExecutionException;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 订阅者注册器
 *
 * @author : linhanghui
 * @since : 2022/5/10 14:55
 */
public class SubscriberRegistry {

    /**
     * 存放事件对应的处理器
     * 所谓处理器，目前只是简单的方法处理逻辑，也就是method
     * eventType -> collection(method)
     */
    private final ConcurrentMap<Class<?>, CopyOnWriteArraySet<Subscriber>> subscribers = Maps.newConcurrentMap();

    /**
     *
     */
    private final EventBus bus;

    /**
     * 本地缓存，解决反射带来的性能问题
     * eventType -> collection(method)
     */
    private static final LoadingCache<Class<?>, ImmutableList<Method>> subscriberMethodsCache =
            CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<>() {
                @Override
                public ImmutableList<Method> load(Class<?> concreteClass) {
                    return getAnnotatedMethodsNotCached(concreteClass);
                }
            });

    /**
     *
     */
    private static final LoadingCache<Class<?>, ImmutableSet<Class<?>>> flattenHierarchyCache =
            CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<>() {
                // <Class<?>> is actually needed to compile
                @SuppressWarnings("RedundantTypeArguments")
                @Override
                public ImmutableSet<Class<?>> load(Class<?> concreteClass) {
                    // 根据类查询所有超类
                    return ImmutableSet.<Class<?>>copyOf(TypeToken.of(concreteClass).getTypes().rawTypes());
                }
            });

    SubscriberRegistry(EventBus bus) {
        this.bus = (EventBus) Preconditions.checkNotNull(bus);
    }

    /**
     * 注册监听器
     *
     * @param listener 监听器，监听多个事件
     */
    void register(Object listener) {

        Multimap<Class<?>, Subscriber> listenerMethods = findAllSubscribers(listener);

        for (Map.Entry<Class<?>, Collection<Subscriber>> entry : listenerMethods.asMap().entrySet()) {
            Class<?> eventType = entry.getKey();
            Collection<Subscriber> eventMethodsInListener = entry.getValue();

            CopyOnWriteArraySet<Subscriber> eventSubscribers = subscribers.get(eventType);

            if (eventSubscribers == null) {
                CopyOnWriteArraySet<Subscriber> newSet = new CopyOnWriteArraySet<>();
                eventSubscribers = MoreObjects.firstNonNull(subscribers.putIfAbsent(eventType, newSet), newSet);
            }

            eventSubscribers.addAll(eventMethodsInListener);
        }

    }

    /**
     * 根据事件获取订阅者
     * 事件考虑超类，所以是collection<Class<>>
     * @param event 事件
     * @return 订阅者集合
     */
    Iterator<Subscriber> getSubscribers(Object event) {
        ImmutableSet<Class<?>> eventTypes = flattenHierarchy(event.getClass());

        List<Iterator<Subscriber>> subscriberIterators = Lists.newArrayListWithCapacity(eventTypes.size());

        for (Class<?> eventType : eventTypes) {
            CopyOnWriteArraySet<Subscriber> eventSubscribers = subscribers.get(eventType);
            if (eventSubscribers != null) {
                // eager no-copy snapshot
                subscriberIterators.add(eventSubscribers.iterator());
            }
        }

        return Iterators.concat(subscriberIterators.iterator());
    }

    private Multimap<Class<?>, Subscriber> findAllSubscribers(Object listener) {
        Multimap<Class<?>, Subscriber> methodsInListener = HashMultimap.create();
        Class<?> clazz = listener.getClass();
        for (Method method : getAnnotatedMethods(clazz)) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            // 事件
            Class<?> eventType = parameterTypes[0];
            // 事件对应的订阅者，实际就是method
            methodsInListener.put(eventType, Subscriber.create(bus, listener, method));
        }

        return methodsInListener;
    }

    private static ImmutableList<Method> getAnnotatedMethods(Class<?> clazz) {
        try {
            return subscriberMethodsCache.getUnchecked(clazz);
        } catch (UncheckedExecutionException var2) {
            Throwables.throwIfUnchecked(var2.getCause());
            throw var2;
        }
    }

    private static ImmutableList<Method> getAnnotatedMethodsNotCached(Class<?> clazz) {
        // 根据类查询所有超类
        Set<? extends Class<?>> supertypes = TypeToken.of(clazz).getTypes().rawTypes();
        Map<MethodIdentifier, Method> identifiers = Maps.newHashMap();

        for (Class<?> supertype : supertypes) {
            for (Method method : supertype.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Day1Subscribe.class) && !method.isSynthetic()) {

                    Class<?>[] parameterTypes = method.getParameterTypes();
                    checkArgument(parameterTypes.length == 1, "Method %s has @Subscribe annotation but has %s parameters. " + "Subscriber methods must have exactly 1 parameter.", method, parameterTypes.length);

                    checkArgument(!parameterTypes[0].isPrimitive(), "@Subscribe method %s's parameter is %s. " + "Subscriber methods cannot accept primitives. " + "Consider changing the parameter to %s.", method, parameterTypes[0].getName(), Primitives.wrap(parameterTypes[0]).getSimpleName());

                    MethodIdentifier ident = new MethodIdentifier(method);
                    if (!identifiers.containsKey(ident)) {
                        identifiers.put(ident, method);
                    }
                }
            }
        }
        return ImmutableList.copyOf(identifiers.values());
    }

    static ImmutableSet<Class<?>> flattenHierarchy(Class<?> concreteClass) {
        try {
            return flattenHierarchyCache.getUnchecked(concreteClass);
        } catch (UncheckedExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }

    /**
     * 封装一层method
     */
    private static final class MethodIdentifier {
        private final String name;
        private final List<Class<?>> parameterTypes;

        MethodIdentifier(Method method) {
            this.name = method.getName();
            this.parameterTypes = Arrays.asList(method.getParameterTypes());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(new Object[]{this.name, this.parameterTypes});
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (!(o instanceof SubscriberRegistry.MethodIdentifier)) {
                return false;
            } else {
                SubscriberRegistry.MethodIdentifier ident = (SubscriberRegistry.MethodIdentifier) o;
                return this.name.equals(ident.name) && this.parameterTypes.equals(ident.parameterTypes);
            }
        }
    }
}
