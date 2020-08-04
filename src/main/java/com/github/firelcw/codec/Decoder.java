package com.github.firelcw.codec;


import com.github.firelcw.model.HttpResponse;

import java.lang.reflect.Type;

/**
 * @author liaochongwei
 * @date 2020/7/31 9:32
 */
public interface Decoder {
    /**
     * 响应解码
     * @param response 响应参数
     * @param type body类型
     * @return Object
     */
    Object decode(HttpResponse response, Type type);
}
