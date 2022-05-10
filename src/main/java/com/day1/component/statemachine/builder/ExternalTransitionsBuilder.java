package com.day1.component.statemachine.builder;

/**
 * 外部流转，两个不同状态之间的流转
 * @author : linhanghui
 * @since : 2022/5/9 15:25
 */
public interface ExternalTransitionsBuilder<S, E, C> {

    /**
     *
     * @param stateIds
     * @return
     */
    From<S, E, C> fromAmong(S... stateIds);
}
