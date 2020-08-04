package com.github.firelcw.annotation;


import com.github.firelcw.codec.Decoder;
import com.github.firelcw.codec.DefaultDecoder;
import com.github.firelcw.interceptor.HttpInterceptor;

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
    Class<? extends HttpInterceptor>[] interceptors() default {};

    /**
     * @return 解码器
     */
    Class<? extends Decoder> decoder() default DefaultDecoder.class;

}
