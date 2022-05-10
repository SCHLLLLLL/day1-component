package com.day1.component.statemachine.builder;

/**
 * 外部流转，两个不同状态之间的流转
 * @author : linhanghui
 * @since : 2022/5/9 15:22
 */
public interface ExternalTransitionBuilder<S, E, C> {

    /**
     * Build transition source state.
     *
     * @param stateId id of state
     * @return from clause builder
     */
    From<S, E, C> from(S stateId);
}
