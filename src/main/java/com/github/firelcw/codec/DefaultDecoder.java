package com.github.firelcw.codec;

import com.alibaba.fastjson.JSON;
import com.github.firelcw.model.HttpResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * 默认解码器
 * @author liaochongwei
 * @date 2020/7/31 11:30
 */
public class DefaultDecoder implements Decoder{
    @Override
    public Object decode(HttpResponse response, Class<?> clazz) {
        if (StringUtils.isBlank(response.getBody())) {
            return null;
        }
        return JSON.parseObject(response.getBody(), clazz);
    }
}
