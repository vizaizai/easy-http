package com.github.vizaizai.parser;

import com.github.vizaizai.util.Utils;
import com.github.vizaizai.util.value.HeadersNameValues;

import java.lang.annotation.Annotation;

/**
 * 接口解析
 * @author liaochongwei
 * @date 2020/7/30 15:24
 */
public class InterfaceParser {
    /**
     * 目标接口
     */
    private final Class<?> target;
    /**
     * 接口级别的headers
     */
    private HeadersNameValues headers;


    public static InterfaceParser doParse(Class<?> target) {
        return new InterfaceParser(target);
    }

    private InterfaceParser(Class<?> target) {
        this.target = target;
        this.parse();
    }

    private void parse() {
        Annotation[] annotations = this.target.getAnnotations();
        this.headers = Utils.getHeaders(annotations);
    }

    public Class<?> getTarget() {
        return target;
    }

    public HeadersNameValues getHeaders() {
        return headers;
    }
}
