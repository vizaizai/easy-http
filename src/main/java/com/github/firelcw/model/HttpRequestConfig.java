package com.github.firelcw.model;

/**
 * http请求配置
 * @author liaochongwei
 * @date 2020/7/31 12:43
 */
public class HttpRequestConfig {
    private Integer connectTimeout = 15000;
    private Integer socketTimeout = 15000;

    public HttpRequestConfig() {
    }

    public HttpRequestConfig(Integer connectTimeout, Integer socketTimeout) {
        this.connectTimeout = connectTimeout;
        this.socketTimeout = socketTimeout;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }
}
