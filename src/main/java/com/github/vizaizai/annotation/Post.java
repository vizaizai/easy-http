package com.github.vizaizai.annotation;



import com.github.vizaizai.interceptor.HttpInterceptor;
import com.github.vizaizai.model.ContentType;

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
    /**
     * @return 重试次数，-1=禁用重试
     */
    int retries() default 0;
    /**
     * @return 重试间隔时间（ms）
     */
    int interval() default 0;
}
