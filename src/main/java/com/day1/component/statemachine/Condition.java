package com.day1.component.statemachine;

/**
 * 条件，表示是否允许到达某个状态
 * @author : linhanghui
 * @since : 2022/5/9 15:16
 */
public interface Condition<C> {

    /**
     * judge condition
     * @param context context object
     * @return whether the context satisfied current condition
     */
    boolean isSatisfied(C context);

    default String name(){
        return this.getClass().getSimpleName();
    }
}
