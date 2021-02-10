package com.github.vizaizai.annotation;


import java.lang.annotation.*;

/**
 * 参数注解
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {
    String TYPE = "param";
    // 参数名
    String value() default "";
}
