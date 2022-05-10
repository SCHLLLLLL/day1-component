package com.day1.component.statemachine.builder;

/**
 * @author : linhanghui
 * @since : 2022/5/9 15:23
 */
public interface To<S, E, C> {

    /**
     * Build transition event
     *
     * @param event transition event
     * @return On clause builder
     */
    On<S, E, C> on(E event);
}
