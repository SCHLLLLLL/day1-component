package com.day1.component.statemachine.builder;

/**
 * @author : linhanghui
 * @since : 2022/5/9 15:22
 */
public interface From<S, E, C> {

    /**
     * Build transition target state and return to clause builder
     * @param stateId id of state
     * @return To clause builder
     */
    To<S, E, C> to(S stateId);
}
