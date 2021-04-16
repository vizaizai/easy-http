package com.github.vizaizai.entity;

import com.github.vizaizai.entity.body.RequestBody;
import com.github.vizaizai.util.value.HeadersNameValues;
import com.github.vizaizai.util.value.StringNameValues;

import java.nio.charset.Charset;
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
    private HeadersNameValues headers;
    /**
     * 请求参数 ?param1=1
     */
    private StringNameValues params;
    /**
     * 请求体类型
     */
    private RequestBody body;
    /**
     * 字符编码
     */
    private Charset encoding;
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
            this.headers = new HeadersNameValues();
        }
        this.headers.add(name, value);
    }

    public void addHeaders(StringNameValues headers) {
        if (this.headers == null) {
            this.headers = new HeadersNameValues();
        }
        this.headers.addAll(headers);
    }

    public void addParam(String key, String value) {
        if (this.params == null) {
            this.params = new StringNameValues();
        }
        this.params.add(key, value);
    }

    public void addParams(StringNameValues nameValues) {
        if (this.params == null) {
            this.params = new StringNameValues();
        }
        this.params.addAll(nameValues);
    }
    public void setEmptyParams() {
         params = new StringNameValues();
    }

    public HeadersNameValues getHeaders() {
        return headers == null ? new HeadersNameValues() : headers;
    }

    public StringNameValues getParams() {
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

    public RequestBody getBody() {
        return body;
    }

    public void setBody(RequestBody body) {
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

    public Charset getEncoding() {
        return encoding;
    }

    public void setEncoding(Charset encoding) {
        this.encoding = encoding;
    }
}
