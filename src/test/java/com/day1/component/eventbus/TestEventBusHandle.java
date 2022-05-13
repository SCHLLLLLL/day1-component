package com.day1.component.eventbus;

/**
 * @author : linhanghui
 * @since : 2022/5/13 15:17
 */
public class TestEventBusHandle {

    @Day1Subscribe
    public void testEvent(TestEvent event) {
        System.out.println("testEvent");
    }
}
