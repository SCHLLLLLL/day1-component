package com.day1.component.eventbus;

/**
 * @author : linhanghui
 * @since : 2022/5/13 15:17
 */
public class Test {

    public static void main(String[] args) {
        EventBus eventBus = new EventBus();
        eventBus.register(new TestEventBusHandle());

        eventBus.post(new TestEvent());

    }
}
