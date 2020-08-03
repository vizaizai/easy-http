package com.github.firelcw.hander;

import com.github.firelcw.codec.Decoder;
import com.github.firelcw.codec.SimpleDecoder;
import com.github.firelcw.model.HttpResponse;
import com.github.firelcw.util.TypeUtils;

import java.lang.reflect.Type;

/**
 * @author liaochongwei
 * @date 2020/7/31 13:17
 */
public class DecodeHandler {
    private final HttpResponse response;

    private Decoder decoder;

    private final Type returnType;

    public DecodeHandler(HttpResponse response, Decoder decoder, Type returnType) {
        this.response = response;
        this.decoder = decoder;
        this.returnType = returnType;
    }

    public Object handle() {
        if (TypeUtils.isSimple(this.returnType.getTypeName())) {
            this.decoder = new SimpleDecoder();
        }
        return this.decoder.decode(this.response, this.returnType);
    }

    public Type getReturnType() {
        return returnType;
    }

    public Decoder getDecoder() {
        return decoder;
    }

    public HttpResponse getResponse() {
        return response;
    }
}
