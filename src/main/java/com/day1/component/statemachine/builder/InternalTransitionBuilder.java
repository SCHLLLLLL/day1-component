package com.day1.component.statemachine.builder;

/**
 * 内部流转，同一个状态之间的流转
 * @author : linhanghui
 * @since : 2022/5/9 15:26
 */
public interface InternalTransitionBuilder <S, E, C>{

    /**
     * Build a internal transition
     * @param stateId id of transition
     * @return To clause builder
     */
    To<S, E, C> within(S stateId);
}
