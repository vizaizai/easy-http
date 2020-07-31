package com.lcw.codec;

import com.lcw.model.HttpResponse;
import org.apache.commons.lang3.StringUtils;

import static com.lcw.util.TypeUtils.*;


/**
 * 简单类型解码器
 * @author liaochongwei
 * @date 2020/7/31 9:45
 */
public class SimpleDecoder implements Decoder{
    @Override
    public Object decode(HttpResponse response, Class<?> clazz) {

        if (!isSimple(clazz)) {
            return null;
        }
        if (StringUtils.isBlank(response.getBody())) {
            return null;
        }

        String type = getType(clazz);
        if (getIntType().equals(type)) {
            return Integer.valueOf(response.getBody());

        }else if (getShortType().equals(type)) {
            return Short.valueOf(response.getBody());

        }else if (getLongType().equals(type)) {
            return Long.valueOf(response.getBody());

        }else if (getByteType().equals(type)) {
            return Byte.valueOf(response.getBody());

        }else if (getDoubleType().equals(type)) {
            return Double.valueOf(response.getBody());

        }else if (getFloatType().equals(type)) {
            return Float.valueOf(response.getBody());

        }else if (getBoolType().equals(type)) {
            return Boolean.valueOf(response.getBody());

        }else if (getCharType().equals(type)) {
            return response.getBody().charAt(0);

        }else {
            return response.getBody();
        }
    }
}
