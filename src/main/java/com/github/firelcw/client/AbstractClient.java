package com.github.firelcw.client;

import com.github.firelcw.model.HttpRequest;
import com.github.firelcw.model.HttpRequestConfig;
import com.github.firelcw.model.HttpResponse;

/**
 * @author liaochongwei
 * @date 2020/8/20 10:09
 */
public abstract class AbstractClient {
    private HttpRequestConfig config;
    /**
     * 执行HTTP请求
     * @param param HttpRequest
     * @return HttpResponse
     */
    public abstract HttpResponse request(HttpRequest param);

    public HttpRequestConfig getConfig() {
        return config;
    }

    public void setConfig(HttpRequestConfig config) {
        this.config = config;
    }
}
