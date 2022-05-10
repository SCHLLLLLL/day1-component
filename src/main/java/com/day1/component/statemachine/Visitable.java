package com.day1.component.statemachine;

/**
 * @author : linhanghui
 * @since : 2022/5/9 15:13
 */
public interface Visitable {

    /**
     * 返回可观察者信息
     * @param visitor 观察者
     * @return
     */
    String accept(final Visitor visitor);
}
