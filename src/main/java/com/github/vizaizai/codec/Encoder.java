package com.github.vizaizai.codec;

import java.util.Map;

/**
 * @author liaochongwei
 * @date 2020/7/31 9:30
 */
public interface Encoder {
    /**
     * 将对象转化成成Map<String,String> 用于编码@Query和@Headers注解的对象
     * @param object 待编码对象
     * @return map
     */
     Map<String,String> encodeMap(Object object);

    /**
     * 将对象转化成string（如果参数类型本身为简单类型，则直接转化成string），用于编码 @Body注解的对象（默认是解析成json字符串）
     * @param object 待编码对象
     * @return string
     */
     String encodeString(Object object);
}
