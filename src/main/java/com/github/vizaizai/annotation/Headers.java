package com.github.vizaizai.annotation;


import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Headers {
    String TYPE = "headers";
    // 请求头
    String[] value() default {};
}
