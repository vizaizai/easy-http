package com.github.firelcw.annotation;



import com.github.firelcw.interceptor.HttpInterceptor;
import com.github.firelcw.model.ContentType;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Post {
    String value() default "";
    String contentType() default ContentType.APPLICATION_JSON_UTF8;
    /**
     * @return 拦截器
     */
    Class<? extends HttpInterceptor>[] interceptors() default {};
}
