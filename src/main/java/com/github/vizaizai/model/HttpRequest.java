package com.github.vizaizai.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求参数封装
 * @author liaochongwei
 * @date 2020/7/30 17:37
 */
public class HttpRequest {

    /**
     * 请求类型
     */
    private HttpMethod method;
    /**
     * 请求路径
     */
    private String url;
    /**
     * contentType
     */
    private String contentType;
    /**
     * 请求头
     */
    private Map<String, String> headers;
    /**
     * 请求参数 ?param1=1
     */
    private Map<String, String> params;
    /**
     * 请求体文本内容
     */
    private String body;
    /**
     * 扩展参数
     */
    private Map<String, Object> extendParams;
    /**
     * 是否异步
     */
    private boolean async;
    /**
     * 请求开始时间戳
     */
    private long startTime;
    /**
     * 请求配置
     */
    private HttpRequestConfig config;

    public void addHeader(String name, String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(name, value);
    }

    public void addHeaders(Map<String, String> headers) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.putAll(headers);
    }

    public void addQueryParam(String key, String value) {
        if (this.params == null) {
            this.params = new HashMap<>();
        }
        this.params.put(key, value);
    }

    public void addQueryParams(Map<String, String> query) {
        if (this.params == null) {
            this.params = new HashMap<>();
        }
        this.params.putAll(query);
    }

    public Map<String, String> getHeaders() {
        return headers == null ? Collections.emptyMap() : headers;
    }

    public Map<String, String> getQueryParams() {
        return params;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, Object> getExtendParams() {
        return extendParams;
    }

    public void setExtendParams(Map<String, Object> extendParams) {
        this.extendParams = extendParams;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public HttpRequestConfig getConfig() {
        return config;
    }

    public void setConfig(HttpRequestConfig config) {
        this.config = config;
    }
}
