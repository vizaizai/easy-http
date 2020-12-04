package com.github.firelcw.annotation;


import com.github.firelcw.interceptor.HttpInterceptor;
import com.github.firelcw.model.HttpMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mapping {
    String value() default "";
    HttpMethod httpMethod() default HttpMethod.GET;
    String contentType() default "";
    /**
     * @return 拦截器
     */
    Class<? extends HttpInterceptor>[] interceptors() default {};
}
