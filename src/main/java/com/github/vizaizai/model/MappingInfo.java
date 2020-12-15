package com.github.vizaizai.model;

import com.github.vizaizai.interceptor.HttpInterceptor;

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
     * 拦截器
     */
    private Class<? extends HttpInterceptor>[] interceptors;
    /**
     * 重试属性
     */
    private RetryProperties retryProperties;

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

    public Class<? extends HttpInterceptor>[] getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(Class<? extends HttpInterceptor>[] interceptors) {
        this.interceptors = interceptors;
    }

    public RetryProperties getRetryProperties() {
        return retryProperties;
    }

    public void setRetryProperties(RetryProperties retryProperties) {
        this.retryProperties = retryProperties;
    }
}
