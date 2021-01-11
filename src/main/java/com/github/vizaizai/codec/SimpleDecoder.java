package com.github.vizaizai.codec;

import com.github.vizaizai.exception.EasyHttpException;
import com.github.vizaizai.model.HttpResponse;
import com.github.vizaizai.util.StreamUtils;
import com.github.vizaizai.util.TypeUtils;
import com.github.vizaizai.util.Utils;

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
        if (response.getBody() == null) {
            return null;
        }
        String bodyString;
        try {
             bodyString = StreamUtils.copyToString(response.getBody().asInputStream(), Utils.UTF_8);
        }catch (Exception e) {
            throw new EasyHttpException(e);
        }


        String typeStr = TypeUtils.getType(type.getTypeName());
        if (TypeUtils.getIntType().equals(typeStr)) {
            return Integer.valueOf(bodyString);

        }else if (TypeUtils.getShortType().equals(typeStr)) {
            return Short.valueOf(bodyString);

        }else if (TypeUtils.getLongType().equals(typeStr)) {
            return Long.valueOf(bodyString);

        }else if (TypeUtils.getByteType().equals(typeStr)) {
            return Byte.valueOf(bodyString);

        }else if (TypeUtils.getDoubleType().equals(typeStr)) {
            return Double.valueOf(bodyString);

        }else if (TypeUtils.getFloatType().equals(typeStr)) {
            return Float.valueOf(bodyString);

        }else if (TypeUtils.getBoolType().equals(typeStr)) {
            return Boolean.valueOf(bodyString);

        }else if (TypeUtils.getCharType().equals(typeStr)) {
            return bodyString.charAt(0);

        } else if (TypeUtils.getVoidType().equals(typeStr)) {
            return null;
        }else {
            return bodyString;
        }
    }
}
