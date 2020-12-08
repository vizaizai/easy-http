package com.github.vizaizai.codec;

import com.github.vizaizai.model.HttpResponse;
import com.github.vizaizai.util.TypeUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;


/**
 * 简单类型解码器
 * @author liaochongwei
 * @date 2020/7/31 9:45
 */
public class SimpleDecoder implements Decoder {
    @Override
    public Object decode(HttpResponse response, Type type) {

        if (!TypeUtils.isSimple(type.getTypeName())) {
            return null;
        }
        if (StringUtils.isBlank(response.getBody())) {
            return null;
        }

        String typeStr = TypeUtils.getType(type.getTypeName());
        if (TypeUtils.getIntType().equals(typeStr)) {
            return Integer.valueOf(response.getBody());

        }else if (TypeUtils.getShortType().equals(typeStr)) {
            return Short.valueOf(response.getBody());

        }else if (TypeUtils.getLongType().equals(typeStr)) {
            return Long.valueOf(response.getBody());

        }else if (TypeUtils.getByteType().equals(typeStr)) {
            return Byte.valueOf(response.getBody());

        }else if (TypeUtils.getDoubleType().equals(typeStr)) {
            return Double.valueOf(response.getBody());

        }else if (TypeUtils.getFloatType().equals(typeStr)) {
            return Float.valueOf(response.getBody());

        }else if (TypeUtils.getBoolType().equals(typeStr)) {
            return Boolean.valueOf(response.getBody());

        }else if (TypeUtils.getCharType().equals(typeStr)) {
            return response.getBody().charAt(0);

        } else if (TypeUtils.getVoidType().equals(typeStr)) {
            return null;
        }else {
            return response.getBody();
        }
    }
}
