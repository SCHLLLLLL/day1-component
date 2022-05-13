package com.day1.component.eventbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : linhanghui
 * @since : 2022/5/10 15:24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Day1Subscribe {

    /**
     * 方法最长执行时间,单位 秒
     */
    int maxExecuteTime() default 10;
}
