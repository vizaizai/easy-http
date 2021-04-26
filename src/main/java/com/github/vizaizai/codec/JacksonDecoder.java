package com.github.vizaizai.codec;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vizaizai.entity.HttpResponse;
import com.github.vizaizai.exception.CodecException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;

/**
 * 默认解码器
 * @author liaochongwei
 * @date 2020/7/31 11:30
 */
public class JacksonDecoder implements Decoder {

    private final ObjectMapper mapper;

    public JacksonDecoder() {
        this(Collections.<Module>emptyList());
    }

    public JacksonDecoder(Iterable<Module> modules) {
        this(new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModules(modules));
    }

    public JacksonDecoder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Object decode(HttpResponse response, Type type) {
        if (response.getBody() == null) {
            return null;
        }
        try {
            Reader reader = response.getBody().asReader(this.encoding());
            if (!reader.markSupported()) {
                reader = new BufferedReader(reader, 1);
            }
            reader.mark(1);
            if (reader.read() == -1) {
                return null; // Eagerly returning null avoids "No content to map due to end-of-input"
            }
            reader.reset();
            return mapper.readValue(reader, mapper.constructType(type));
        } catch (IOException e) {
            throw new CodecException(e);
        }
    }

}
