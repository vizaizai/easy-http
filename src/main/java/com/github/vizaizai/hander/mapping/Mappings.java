package com.github.vizaizai.hander.mapping;

import com.github.vizaizai.annotation.*;
import com.github.vizaizai.entity.HttpMethod;
import com.github.vizaizai.entity.MappingInfo;
import com.github.vizaizai.entity.RetrySettings;
import com.github.vizaizai.util.VUtils;

import java.lang.annotation.Annotation;

/**
 * 映射处理器工具
 * @author liaochongwei
 * @date 2020/12/15 17:31
 */
public class Mappings {
    private Mappings() {
    }


    public static MappingInfo getMappingInfo(Annotation annotation) {

        MappingProcessor processor;
        if (annotation instanceof  Mapping) {
             processor = new BaseProcessor();

        }else if (annotation instanceof Get) {
            processor = new GetProcessor();

        }else if (annotation instanceof Post) {
            processor = new PostProcessor();

        }else if (annotation instanceof Put) {
            processor = new PutProcessor();

        }else {
            processor = new DeleteProcessor();
        }
        return processor.getMapping(annotation);
    }


    /**
     * 添加重试属性
     * @param mappingInfo mappingInfo
     * @param retries retries
     * @param interval interval（ms）
     */
    private static void addRetryProps(MappingInfo mappingInfo, Integer retries, Integer interval) {
        RetrySettings retrySettings = new RetrySettings();
        retrySettings.setMaxAttempts(retries);
        retrySettings.setIntervalTime(interval);
        retrySettings.setEnable(retries == 0 ? null : retries > 0);
        mappingInfo.setRetrySettings(retrySettings);

    }


    static class BaseProcessor implements MappingProcessor{
        @Override
        public MappingInfo getMapping(Annotation annotation) {
            MappingInfo info = new MappingInfo();
            Mapping mapping = ((Mapping) annotation);
            info.setPath(mapping.value());
            if (VUtils.isNotBlank(mapping.contentType())) {
                info.setContentType(mapping.contentType());
            }
            info.setBodyType(mapping.bodyType());
            info.setHttpMethod(mapping.httpMethod());
            info.setInterceptors(mapping.interceptors());

            addRetryProps(info, mapping.retries(), mapping.interval());
            return info;
        }
    }

    static class GetProcessor implements MappingProcessor{
        @Override
        public MappingInfo getMapping(Annotation annotation) {
            MappingInfo info = new MappingInfo();
            Get get = ((Get) annotation);
            info.setPath(get.value());
            info.setHttpMethod(HttpMethod.GET);
            info.setInterceptors(get.interceptors());

            addRetryProps(info, get.retries(), get.interval());
            return info;
        }
    }

    static class PostProcessor implements MappingProcessor{
        @Override
        public MappingInfo getMapping(Annotation annotation) {
            MappingInfo info = new MappingInfo();
            Post post = ((Post) annotation);
            info.setPath(post.value());
            info.setHttpMethod(HttpMethod.POST);
            info.setInterceptors(post.interceptors());
            info.setBodyType(post.bodyType());
            if (VUtils.isNotBlank(post.contentType())) {
                info.setContentType(post.contentType());
            }

            addRetryProps(info, post.retries(), post.interval());
            return info;
        }
    }

    static class PutProcessor implements MappingProcessor{
        @Override
        public MappingInfo getMapping(Annotation annotation) {
            MappingInfo info = new MappingInfo();
            Put put = ((Put) annotation);
            info.setPath(put.value());
            info.setHttpMethod(HttpMethod.PUT);
            info.setInterceptors(put.interceptors());
            info.setBodyType(put.bodyType());
            if (VUtils.isNotBlank(put.contentType())) {
                info.setContentType(put.contentType());
            }

            addRetryProps(info, put.retries(), put.interval());
            return info;
        }
    }

    static class DeleteProcessor implements MappingProcessor{
        @Override
        public MappingInfo getMapping(Annotation annotation) {
            MappingInfo info = new MappingInfo();
            Delete delete = ((Delete) annotation);
            info.setPath(delete.value());
            info.setHttpMethod(HttpMethod.DELETE);
            info.setInterceptors(delete.interceptors());
            info.setBodyType(delete.bodyType());
            if (VUtils.isNotBlank(delete.contentType())) {
                info.setContentType(delete.contentType());
            }

            addRetryProps(info, delete.retries(), delete.interval());
            return info;
        }
    }

}
