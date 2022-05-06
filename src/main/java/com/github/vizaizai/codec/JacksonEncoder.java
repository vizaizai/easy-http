package com.github.vizaizai.codec;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.vizaizai.exception.CodecException;
import com.github.vizaizai.entity.body.Body;
import com.github.vizaizai.entity.body.ByteArrayBody;
import com.github.vizaizai.util.Utils;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * 默认编码
 * @author liaochongwei
 * @date 2020/7/31 9:53
 */
public class JacksonEncoder implements Encoder {
    private final ObjectMapper mapper;

    public JacksonEncoder() {
        this(Collections.<Module>emptyList());
    }

    public JacksonEncoder(Iterable<Module> modules) {
        this(new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .setDefaultPrettyPrinter(new MinimalPrettyPrinter()) // 最小输出
                .registerModules(modules));
    }

    public JacksonEncoder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Body encode(Object object, Type bodyType) {
        if (object == null) {
            return null;
        }
        JavaType javaType = mapper.getTypeFactory().constructType(bodyType);
        try {
            return ByteArrayBody.ofNullable(mapper.writerFor(javaType).writeValueAsString(object).getBytes(Utils.UTF_8));
        }catch (JsonProcessingException e) {
            throw new CodecException(e);
        }
    }
}
