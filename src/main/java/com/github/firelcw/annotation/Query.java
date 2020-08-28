package com.github.firelcw.annotation;


import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Query {
    String TYPE = "query";
    // 查询参数名
    String value() default "";
}
