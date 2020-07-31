package com.github.annotation;


import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NetClient {

    /**
     * 请求基本路径名
     * @return
     */
    String value() default "";

}
