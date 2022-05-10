package com.day1.component.statemachine;

import com.day1.component.statemachine.impl.TransitionType;

/**
 * 流转，表示从一个状态到另一个状态
 * 包含：原状态，目标状态，状态变更事件，流程类型（内部，外部）
 * @author : linhanghui
 * @since : 2022/5/9 15:15
 */
public interface Transition<S, E, C> {

    /**
     * Gets the source state of this transition.
     *
     * @return the source state
     */
    State<S,E,C> getSource();

    /**
     * set source
     * @param state
     */
    void setSource(State<S, E, C> state);

    /**
     * get source
     * @return
     */
    E getEvent();

    /**
     * set event
     * @param event
     */
    void setEvent(E event);

    /**
     * set type
     * @param type
     */
    void setType(TransitionType type);

    /**
     * Gets the target state of this transition.
     *
     * @return the target state
     */
    State<S,E,C> getTarget();

    /**
     * set target
     * @param state
     */
    void setTarget(State<S, E, C> state);

    /**
     * Gets the guard of this transition.
     *
     * @return the guard
     */
    Condition<C> getCondition();

    /**
     * set condition
     * @param condition
     */
    void setCondition(Condition<C> condition);

    /**
     * get action
     * @return
     */
    Action<S,E,C> getAction();

    /**
     * set action
     * @param action
     */
    void setAction(Action<S, E, C> action);

    /**
     * Do transition from source state to target state.
     *
     * @param ctx
     * @param checkCondition
     * @return the target state
     */
    State<S,E,C> transit(C ctx, boolean checkCondition);

    /**
     * Verify transition correctness
     */
    void verify();
}
