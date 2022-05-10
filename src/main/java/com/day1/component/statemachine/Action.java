package com.day1.component.statemachine;

/**
 * 动作，到达某个状态之后，可以做什么
 * @author : linhanghui
 * @since : 2022/5/9 15:17
 */
public interface Action<S, E, C> {

    void execute(S from, S to, E event, C context);
}
