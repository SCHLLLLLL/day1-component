package com.day1.component.eventbus;

import com.google.common.base.Preconditions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author : linhanghui
 * @since : 2022/5/10 14:56
 */
public class Subscriber {

    private EventBus bus;

    final Object target;

    private final Method method;

    private final ExecutorService executor;

    private Subscriber(EventBus bus, Object target, Method method) {
        this.bus = bus;
        this.target = Preconditions.checkNotNull(target);
        this.method = method;
        method.setAccessible(true);
        this.executor = bus.executor();
    }

    static Subscriber create(EventBus bus, Object listener, Method method) {
        return new Subscriber(bus, listener, method);
    }

    final boolean dispatchEvent(final Object event, String eventId) {
        Future<Boolean> booleanFuture = executor.submit(() -> {
            try {
                long start = System.currentTimeMillis();
//                log.info("eventBus消费开始 eventId {} 执行handler {}, method:{}", eventId, target.getClass().toString(), method.getName());
                Object returnObj = invokeSubscriberMethod(event);
                // 执行时间
                double executionTime = (System.currentTimeMillis() - start) / 1000d;

//                log.info("eventBus消费完成 eventId {} 耗时 {}s", eventId, executionTime);
                System.out.println("eventBus消费完成 eventId " + eventId + " 耗时 " + executionTime + " s");
                //监控
                monitor(executionTime, eventId);
                if (returnObj instanceof Boolean) {
                    boolean executeSuccess = (boolean) returnObj;
                    if (!executeSuccess) {
//                        bus.handleSubscriberException(null, context(event, eventId));
                        return false;
                    }
                }
            } catch (InvocationTargetException e) {
//                bus.handleSubscriberException(e.getCause(), context(event, eventId));
                return false;
            }
//            // 消息消费成功，写入redis
//            RedissonClient redissonClient = SpringUtils.getBean(RedissonClient.class);
//            if (DataUtils.isEmpty(redissonClient)) {
//                throw new RuntimeException("erp-event-bus--> redissonClient bean is not exist");
//            }
//            RMap<String, String> stringStringRMap = redissonClient.getMap(redisEventId(eventId));
//            stringStringRMap.put(md5Method(this.method), "1");
//            stringStringRMap.expireAsync(1, TimeUnit.DAYS);
            return true;
        });

        try {
            Day1Subscribe day1Subscribe = method.getAnnotation(Day1Subscribe.class);
            return booleanFuture.get(day1Subscribe.maxExecuteTime(), TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
//            log.error("执行失败 ", e);
            return false;
        }
    }

    Object invokeSubscriberMethod(Object event) throws InvocationTargetException {
        try {
            return method.invoke(target, checkNotNull(event));
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            throw new Error("Method rejected target/argument: " + event, e);
        } catch (IllegalAccessException e) {
            throw new Error("Method became inaccessible: " + event, e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Error) {
                throw (Error) e.getCause();
            }
            throw e;
        }
    }

    /**
     * 监控超时事件
     *
     * @param executionTime 执行时间
     * @param eventId       事件id
     */
    private void monitor(double executionTime, String eventId) {
//        SystemConstant systemConfig = FeiShuRobotUtils.getSystemConfig();
//        if (systemConfig.isFeiShuMonitor() && DataUtils.isNotEmpty(systemConfig.getFeiShuEventBusUrl())) {
//            EventBusConfig mqConfig = SpringUtils.getBean(EventBusConfig.class);
//            if (executionTime > mqConfig.getEventBusTimeOut()) {
//                FeiShuRobotUtils.sendText(String.format("eventBus事件执行超时,耗时 %s秒, 事件id %s 执行handler %s.%s() ",
//                                executionTime, eventId, target.getClass().toString(), method.getName()),
//                        systemConfig.getFeiShuEventBusUrl());
//            }
//
//        }
    }
}
