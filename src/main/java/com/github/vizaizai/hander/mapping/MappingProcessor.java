package com.github.vizaizai.hander.mapping;

import com.github.vizaizai.entity.MappingInfo;

import java.lang.annotation.Annotation;

/**
 * 映射处理器
 * @author liaochongwei
 * @date 2020/12/15 17:17
 */
public interface MappingProcessor {
    /**
     * 获取映射信息
     * @param annotation 请求映射注解
     * @return MappingInfo
     */
    MappingInfo getMapping(Annotation annotation);
}
