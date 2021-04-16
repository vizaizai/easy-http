package com.github.vizaizai.codec;

import com.github.vizaizai.entity.body.Body;

import java.lang.reflect.Type;

/**
 * @author liaochongwei
 * @date 2020/7/31 9:30
 */
public interface Encoder {
    /**
     * 将对象转化成Body（如果参数类型本身为简单类型，则直接转化成string），用于编码 @Body注解的对象（默认JSON编码）
     * @param object 待编码对象
     * @param bodyType 请求体对象类型
     * @return Body
     */
    Body encode(Object object, Type bodyType);
}
