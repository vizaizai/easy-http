package com.github.lcw.model;

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
    private Map<String,String> headers;
    /**
     * 请求参数 ?param1=1
     */
    private Map<String,String> params;
    /**
     * 请求体文本内容
     */
    private String content;

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

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
