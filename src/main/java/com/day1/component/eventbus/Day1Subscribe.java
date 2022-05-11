package com.day1.component.eventbus;

/**
 * @author : linhanghui
 * @since : 2022/5/10 15:24
 */
public @interface Day1Subscribe {

    /**
     * 方法最长执行时间,单位 秒
     */
    int maxExecuteTime() default 10;
}
