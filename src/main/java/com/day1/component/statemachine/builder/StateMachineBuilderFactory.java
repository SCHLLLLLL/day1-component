package com.day1.component.statemachine.builder;

/**
 * S:状态
 * E:事件
 * C:Context
 * @author : linhanghui
 * @since : 2022/5/9 15:22
 */
public class StateMachineBuilderFactory {

    public static <S, E, C> StateMachineBuilder<S, E, C> create(){
        return new StateMachineBuilderImpl<>();
    }
}
