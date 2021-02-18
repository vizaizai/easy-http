package com.github.vizaizai.codec;


import com.github.vizaizai.entity.HttpResponse;
import com.github.vizaizai.util.Utils;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

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

    /**
     * 字符编码
     * @return 字符编码
     */
    default Charset encoding(){
        return Utils.UTF_8;
    }
}
