package com.day1.component.statemachine.builder;

import com.day1.component.statemachine.Condition;

/**
 * @author : linhanghui
 * @since : 2022/5/9 15:23
 */
public interface On<S, E, C> extends When<S, E, C> {

    /**
     * Add condition for the transition
     * @param condition transition condition
     * @return When clause builder
     */
    When<S, E, C> when(Condition<C> condition);
}
