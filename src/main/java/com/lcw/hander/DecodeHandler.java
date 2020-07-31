package com.lcw.hander;

import com.lcw.codec.Decoder;
import com.lcw.codec.SimpleDecoder;
import com.lcw.model.HttpResponse;
import com.lcw.util.TypeUtils;

/**
 * @author liaochongwei
 * @date 2020/7/31 13:17
 */
public class DecodeHandler<T> {
    private final HttpResponse response;

    private Decoder decoder;

    private final Class<T> returnType;

    public DecodeHandler(HttpResponse response, Decoder decoder, Class<T> returnType) {
        this.response = response;
        this.decoder = decoder;
        this.returnType = returnType;
    }

    public Object handle() {
        if (TypeUtils.isSimple(this.returnType)) {
            this.decoder = new SimpleDecoder();
        }
        return this.decoder.decode(this.response, this.returnType);
    }

    public Class<T> getReturnType() {
        return returnType;
    }

    public Decoder getDecoder() {
        return decoder;
    }

    public HttpResponse getResponse() {
        return response;
    }
}
