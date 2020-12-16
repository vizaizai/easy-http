package com.github.vizaizai.annotation;


import com.github.vizaizai.interceptor.HttpInterceptor;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Get {
    String value() default "";
    /**
     * @return 拦截器
     */
    Class<? extends HttpInterceptor>[] interceptors() default {};
    /**
     * @return 重试 (-1:禁用重试 0:默认 >0: 选用+重试次数)
     */
    int retries() default 0;
    /**
     * @return 重试间隔时间（ms）
     */
    int interval() default -1;
}
