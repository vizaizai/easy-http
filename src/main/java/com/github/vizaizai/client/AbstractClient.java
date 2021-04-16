package com.github.vizaizai.client;

import com.github.vizaizai.entity.HttpRequest;
import com.github.vizaizai.entity.HttpRequestConfig;
import com.github.vizaizai.entity.HttpResponse;

import java.io.IOException;

/**
 * @author liaochongwei
 * @date 2020/8/20 10:09
 */
public abstract class AbstractClient {
    private HttpRequestConfig httpRequestConfig;
    /**
     * 执行HTTP请求
     * @param request HttpRequest
     * @return HttpResponse
     */
    public abstract HttpResponse request(HttpRequest request) throws IOException;

    public HttpRequestConfig getHttpRequestConfig() {
        return httpRequestConfig;
    }

    public void setConfig(HttpRequestConfig config) {
        this.httpRequestConfig = config;
    }
}
