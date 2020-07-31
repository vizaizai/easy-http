package com.lcw.annotation;



import com.lcw.model.ContentType;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Put {
    String value() default "";
    String contentType() default ContentType.APPLICATION_JSON;
}
