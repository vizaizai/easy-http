package com.github.lcw.annotation;



import com.github.lcw.model.ContentType;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Post {
    String value() default "";
    String contentType() default ContentType.APPLICATION_JSON;
}
