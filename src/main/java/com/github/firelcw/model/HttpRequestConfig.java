package com.github.firelcw.model;

/**
 * http请求配置
 * @author liaochongwei
 * @date 2020/7/31 12:43
 */
public class HttpRequestConfig {
    private Integer connectTimeout;
    private Integer requestTimeout;

    public HttpRequestConfig() {
    }

    public static HttpRequestConfig defaultConfig() {
        HttpRequestConfig config = new HttpRequestConfig();
        config.setConnectTimeout(15000);
        config.setRequestTimeout(30000);
        return config;
    }

    public HttpRequestConfig(Integer connectTimeout, Integer requestTimeout) {
        this.connectTimeout = connectTimeout;
        this.requestTimeout = requestTimeout;
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
}
