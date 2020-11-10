package com.github.firelcw.annotation;


import com.github.firelcw.model.HttpMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mapping {
    String value() default "";
    HttpMethod httpMethod() default HttpMethod.GET;
    String contentType() default "";
}
