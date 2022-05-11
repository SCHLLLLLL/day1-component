package com.day1.component.eventbus;

import com.google.common.collect.Queues;

import java.util.Iterator;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author : linhanghui
 * @since : 2022/5/10 16:09
 */
abstract class Dispatcher {

    static Dispatcher perThreadDispatchQueue() {
        return new PerThreadQueuedDispatcher();
    }

    /**
     * Dispatches the given {@code event} to the given {@code subscribers}.
     */
    abstract boolean dispatch(Object event, String eventId, Iterator<Subscriber> subscribers);


    private static final class PerThreadQueuedDispatcher extends Dispatcher {

        /**
         * Per-thread queue of events to dispatch.
         */
        private final ThreadLocal<Queue<Event>> queue = ThreadLocal.withInitial(Queues::newArrayDeque);

        /**
         * Per-thread dispatch state, used to avoid reentrant event dispatching.
         */
        private final ThreadLocal<Boolean> dispatching = ThreadLocal.withInitial(() -> false);

        @Override
        boolean dispatch(Object event, String eventId, Iterator<Subscriber> subscribers) {
            checkNotNull(event);
            checkNotNull(subscribers);
            Queue<Event> queueForThread = queue.get();
            queueForThread.offer(new Event(event, subscribers));

            /**
             * 所有都执行成功,才是成功
             */
            boolean executeResult = true;

            if (!dispatching.get()) {
                dispatching.set(true);
                try {
                    Event nextEvent;
                    while ((nextEvent = queueForThread.poll()) != null) {
                        while (nextEvent.subscribers.hasNext()) {

                            executeResult = nextEvent.subscribers.next().dispatchEvent(nextEvent.event, eventId) && executeResult;
                        }
                    }
                } finally {
                    dispatching.remove();
                    queue.remove();
                }
            }
            // 如果全部执行成功删除redis key
            try {
                if (executeResult) {
//                    RedissonClient redissonClient = SpringUtils.getBean(RedissonClient.class);
//                    RMap<String, String> stringStringRMap = redissonClient.getMap(redisEventId(eventId));
//                    stringStringRMap.delete();
                }
            } catch (Exception e) {
//                log.error("删除事件redis key失败 {},异常 {}", redisEventId(eventId), e);
            }

            return executeResult;
        }

        private String redisEventId(String eventId) {
            return "EVENT_BUS:" + eventId;
        }


        private static final class Event {
            private final Object event;
            private final Iterator<Subscriber> subscribers;

            private Event(Object event, Iterator<Subscriber> subscribers) {
                this.event = event;
                this.subscribers = subscribers;
            }
        }
    }
}
