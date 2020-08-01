package com.github.firelcw.codec;

import com.github.firelcw.model.HttpResponse;
import com.github.firelcw.util.TypeUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * 简单类型解码器
 * @author liaochongwei
 * @date 2020/7/31 9:45
 */
public class SimpleDecoder implements Decoder{
    @Override
    public Object decode(HttpResponse response, Class<?> clazz) {

        if (!TypeUtils.isSimple(clazz)) {
            return null;
        }
        if (StringUtils.isBlank(response.getBody())) {
            return null;
        }

        String type = TypeUtils.getType(clazz);
        if (TypeUtils.getIntType().equals(type)) {
            return Integer.valueOf(response.getBody());

        }else if (TypeUtils.getShortType().equals(type)) {
            return Short.valueOf(response.getBody());

        }else if (TypeUtils.getLongType().equals(type)) {
            return Long.valueOf(response.getBody());

        }else if (TypeUtils.getByteType().equals(type)) {
            return Byte.valueOf(response.getBody());

        }else if (TypeUtils.getDoubleType().equals(type)) {
            return Double.valueOf(response.getBody());

        }else if (TypeUtils.getFloatType().equals(type)) {
            return Float.valueOf(response.getBody());

        }else if (TypeUtils.getBoolType().equals(type)) {
            return Boolean.valueOf(response.getBody());

        }else if (TypeUtils.getCharType().equals(type)) {
            return response.getBody().charAt(0);

        }else {
            return response.getBody();
        }
    }
}
