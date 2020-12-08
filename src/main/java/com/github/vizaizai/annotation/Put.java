package com.github.vizaizai.annotation;



import com.github.vizaizai.interceptor.HttpInterceptor;
import com.github.vizaizai.model.ContentType;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Put {
    String value() default "";
    String contentType() default ContentType.APPLICATION_JSON_UTF8;
    /**
     * @return 拦截器
     */
    Class<? extends HttpInterceptor>[] interceptors() default {};
}
