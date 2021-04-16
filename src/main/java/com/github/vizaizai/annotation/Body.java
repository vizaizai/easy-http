package com.github.vizaizai.annotation;


import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Body {
    String TYPE = "body";
    /**
     * 自动包装一层，body被序列化文本时生效
     */
    String wrapRoot() default "";
}
