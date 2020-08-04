package com.github.firelcw.annotation;


import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EasyClient {
    /**
     * @return 客户端名称
     */
    String value() default "";

    /**
     * @return 拦截器
     */
    Class<?>[] interceptors() default {};

    /**
     * @return 解码器
     */
    Class<?> decoder() default Void.class;

}
