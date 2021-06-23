package com.github.vizaizai.codec;

import com.github.vizaizai.entity.HttpResponse;
import com.github.vizaizai.exception.CodecException;
import com.github.vizaizai.util.NumberUtils;
import com.github.vizaizai.util.TypeUtils;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import static java.lang.String.format;


/**
 * 基础类型解码器
 * @author liaochongwei
 * @date 2020/7/31 9:45
 */
public class SimpleDecoder implements Decoder {
    @Override
    public Object decode(HttpResponse response, Type type) {
        if (response.getBody() == null) {
            return null;
        }
        String bodyString;
        try {
             bodyString = response.getBody().asString(this.encoding());
        }catch (Exception e) {
            throw new CodecException(e);
        }

        if (TypeUtils.equals(String.class, type)) {
            return bodyString;
        }

        if (TypeUtils.isVoid(type)) {
            return null;
        }

        if (TypeUtils.isInt(type)) {
            return Integer.valueOf(bodyString);
        }

        if (TypeUtils.isShort(type)) {
            return Short.valueOf(bodyString);
        }

        if (TypeUtils.isLong(type)) {
            return Long.valueOf(bodyString);
        }

        if (TypeUtils.isByte(type)) {
            return Byte.valueOf(bodyString);
        }

        if (TypeUtils.isDouble(type)) {
            return Double.valueOf(bodyString);
        }

        if (TypeUtils.isFloat(type)) {
            return Float.valueOf(bodyString);
        }

        if (TypeUtils.isBool(type)) {
            return Boolean.valueOf(bodyString);
        }

        if (TypeUtils.isChar(type)) {
            return bodyString.charAt(0);
        }

        if (TypeUtils.isBigDecimal(type)) {
            return new BigDecimal(bodyString);
        }

        if (TypeUtils.isBigInteger(type)) {
            return new BigInteger(bodyString);
        }

        if (TypeUtils.isNumber(type)) {
            return NumberUtils.createNumber(bodyString);
        }

        throw new CodecException(format("%s is not a type supported by this decoder.", type));

    }
}
