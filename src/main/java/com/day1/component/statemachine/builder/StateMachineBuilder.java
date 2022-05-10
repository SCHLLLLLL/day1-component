package com.day1.component.statemachine.builder;

import com.day1.component.statemachine.StateMachine;

/**
 * @author : linhanghui
 * @since : 2022/5/9 15:22
 */
public interface StateMachineBuilder<S, E, C> {

    /**
     * Builder for one transition 创建一个流转
     * @return External transition builder
     */
    ExternalTransitionBuilder<S, E, C> externalTransition();

    /**
     * Builder for multiple transitions 创建多个流转
     * @return External transition builder
     */
    ExternalTransitionsBuilder<S, E, C> externalTransitions();

    /**
     * Start to build internal transition
     * @return Internal transition builder
     */
    InternalTransitionBuilder<S, E, C> internalTransition();

    /**
     *
     * @param machineId
     * @return
     */
    StateMachine<S,E,C> build(String machineId);
}
