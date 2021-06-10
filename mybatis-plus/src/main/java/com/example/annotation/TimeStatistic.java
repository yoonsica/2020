package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Classname TimeStatistic
 * @Description TODO
 * @Date 2021/5/24 上午10:06
 * @Author shengli
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TimeStatistic {
    public String text() default "";
}
