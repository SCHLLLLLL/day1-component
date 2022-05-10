package com.day1.component.statemachine.builder;

import com.day1.component.statemachine.Action;

/**
 * @author : linhanghui
 * @since : 2022/5/9 15:23
 */
public interface When<S, E, C> {

    /**
     * Define action to be performed during transition
     *
     * @param action performed action
     */
    void perform(Action<S, E, C> action);
}
