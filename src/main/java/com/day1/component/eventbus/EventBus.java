package com.day1.component.eventbus;

import com.google.common.base.MoreObjects;
import com.google.common.eventbus.SubscriberExceptionHandler;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author : linhanghui
 * @since : 2022/5/10 14:56
 */
public class EventBus {

    private final String identifier;

    private final ExecutorService executor;

//    private final SubscriberExceptionHandler exceptionHandler;

    private final SubscriberRegistry subscribers = new SubscriberRegistry(this);

    private final Dispatcher dispatcher;

    public EventBus() {
        this("default");
    }

    public EventBus(String identifier) {
        this(identifier, Executors.newFixedThreadPool(10), Dispatcher.perThreadDispatchQueue());
    }

    EventBus(String identifier, ExecutorService executor, Dispatcher dispatcher) {
        this.identifier = checkNotNull(identifier);
        this.executor = checkNotNull(executor);
        this.dispatcher = checkNotNull(dispatcher);
//        this.exceptionHandler = checkNotNull(exceptionHandler);
    }


    /**
     * Returns the default executor this event bus uses for dispatching events to subscribers.
     */
    final ExecutorService executor() {
        return executor;
    }

    /**
     * Registers all subscriber methods on {@code object} to receive events.
     *
     * @param object object whose subscriber methods should be registered.
     */
    public void register(Object object) {
        subscribers.register(object);
    }

    public boolean post(Object event) {
        return post(event, event.hashCode() + "", 0);
    }

    /**
     * 都执行成功才是成功
     */
    public boolean post(Object event, String eventId, int reconsumeTimes) {

        Iterator<Subscriber> eventSubscribers = subscribers.getSubscribers(event);

        //需要删除掉已经成功的 方法
        if (reconsumeTimes > 0) {
            /**
             * 过滤掉已经成功的 method 只重试未成功的
             */
//            eventSubscribers = exceptionHandler.filterSuccessMethod(eventId, eventSubscribers);

        }

        if (eventSubscribers.hasNext()) {
            return dispatcher.dispatch(event, eventId, eventSubscribers);
        }

        return true;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).addValue(identifier).toString();
    }
}

