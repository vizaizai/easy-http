package com.github.vizaizai.codec;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.vizaizai.exception.CodecException;
import com.github.vizaizai.entity.body.Body;
import com.github.vizaizai.entity.body.ByteArrayBody;
import com.github.vizaizai.util.Utils;
import tools.jackson.core.util.MinimalPrettyPrinter;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.json.JsonMapper;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;

/**
 * 默认编码
 * @author liaochongwei
 * @date 2020/7/31 9:53
 */
public class JacksonEncoder implements Encoder {
    private final JsonMapper mapper;

    public JacksonEncoder() {
        this(Collections.emptyList());
    }

    public JacksonEncoder(Collection<Class<?>> subtypes) {
        this(JsonMapper.builder()
                .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
                .defaultPrettyPrinter(new MinimalPrettyPrinter())
                .registerSubtypes(subtypes)
                .build());
    }

    public JacksonEncoder(JsonMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Body encode(Object object, Type bodyType) {
        if (object == null) {
            return null;
        }
        JavaType javaType = mapper.getTypeFactory().constructType(bodyType);
        return ByteArrayBody.ofNullable(mapper.writerFor(javaType).writeValueAsString(object).getBytes(Utils.UTF_8));
    }
}
