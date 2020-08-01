package com.github.firelcw.codec;


import com.github.firelcw.model.HttpResponse;

/**
 * @author liaochongwei
 * @date 2020/7/31 9:32
 */
public interface Decoder {
    /**
     * 响应解码
     * @param response 响应参数
     * @param clazz body类型
     * @return
     */
    Object decode(HttpResponse response, Class<?> clazz);
}
