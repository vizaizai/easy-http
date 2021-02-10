package com.github.vizaizai.entity;

import com.github.vizaizai.interceptor.HttpInterceptor;
import com.github.vizaizai.entity.body.RequestBodyType;

/**
 * 映射信息
 * @author liaochongwei
 * @date 2020/12/15 17:12
 */
public class MappingInfo {
    /**
     * 路径
     */
    private String path;
    /**
     * 请求方式
     */
    private HttpMethod httpMethod;
    /**
     * contentType
     */
    private String contentType;
    /**
     * 请求体类型
     */
    private RequestBodyType bodyType;
    /**
     * 拦截器
     */
    private Class<? extends HttpInterceptor>[] interceptors;
    /**
     * 重试设置
     */
    private RetrySettings retrySettings;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public RequestBodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(RequestBodyType bodyType) {
        this.bodyType = bodyType;
    }

    public Class<? extends HttpInterceptor>[] getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(Class<? extends HttpInterceptor>[] interceptors) {
        this.interceptors = interceptors;
    }

    public RetrySettings getRetrySettings() {
        return retrySettings;
    }

    public void setRetrySettings(RetrySettings retrySettings) {
        this.retrySettings = retrySettings;
    }
}
