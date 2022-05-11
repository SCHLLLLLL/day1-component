package com.day1.component.eventbus;

import com.google.common.eventbus.SubscriberExceptionHandler;

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
}

