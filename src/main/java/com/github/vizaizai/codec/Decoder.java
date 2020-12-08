package com.github.vizaizai.codec;


import com.github.vizaizai.model.HttpResponse;

import java.lang.reflect.Type;

/**
 * @author liaochongwei
 * @date 2020/7/31 9:32
 */
public interface Decoder {
    /**
     * 响应解码
     * @param response 响应参数
     * @param type 返回值类型
     * @return Object
     */
    Object decode(HttpResponse response, Type type);
}
