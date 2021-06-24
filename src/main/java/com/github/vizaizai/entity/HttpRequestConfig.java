package com.github.vizaizai.entity;

import com.github.vizaizai.util.Utils;

import java.nio.charset.Charset;

/**
 * http请求配置
 * @author liaochongwei
 * @date 2020/7/31 12:43
 */
public class HttpRequestConfig {
    private Integer connectTimeout;
    private Integer requestTimeout;
    private Charset encoding;

    public HttpRequestConfig() {
    }

    public static HttpRequestConfig defaultConfig() {
        HttpRequestConfig config = new HttpRequestConfig();
        config.setConnectTimeout(15000);
        config.setRequestTimeout(30000);
        config.setEncoding(Utils.UTF_8);
        return config;
    }

    public HttpRequestConfig(Integer connectTimeout, Integer requestTimeout,Charset encoding) {
        this.connectTimeout = connectTimeout;
        this.requestTimeout = requestTimeout;
        this.encoding = encoding;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public Charset getEncoding() {
        return encoding == null ? Utils.UTF_8 : encoding;
    }

    public void setEncoding(Charset encoding) {
        this.encoding = encoding;
    }
}
