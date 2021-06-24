package com.github.vizaizai.proxy;

import com.github.vizaizai.client.AbstractClient;
import com.github.vizaizai.codec.Decoder;
import com.github.vizaizai.codec.Encoder;
import com.github.vizaizai.hander.mapping.PathConverter;
import com.github.vizaizai.interceptor.HttpInterceptor;
import com.github.vizaizai.entity.HttpRequestConfig;
import com.github.vizaizai.entity.RetrySettings;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * 代理环境
 * @author liaochongwei
 * @date 2020/12/30 15:21
 */
public class ProxyContext<T> {
    private final Class<T> targetClazz;
    private AbstractClient client;
    private String url;
    private Encoder encoder;
    private Decoder decoder;
    private HttpRequestConfig requestConfig;
    private List<HttpInterceptor> interceptors;
    private Executor executor;
    private RetrySettings retrySettings;
    private PathConverter pathConverter;

    public ProxyContext(Class<T> targetClazz) {
        this.targetClazz = targetClazz;
    }

    public Class<T> getTargetClazz() {
        return targetClazz;
    }

    public AbstractClient getClient() {
        return client;
    }

    public void setClient(AbstractClient client) {
        this.client = client;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Encoder getEncoder() {
        return encoder;
    }

    public void setEncoder(Encoder encoder) {
        this.encoder = encoder;
    }

    public Decoder getDecoder() {
        return decoder;
    }

    public void setDecoder(Decoder decoder) {
        this.decoder = decoder;
    }

    public HttpRequestConfig getRequestConfig() {
        return requestConfig;
    }

    public void setRequestConfig(HttpRequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

    public List<HttpInterceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<HttpInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public RetrySettings getRetrySettings() {
        return retrySettings;
    }

    public void setRetrySettings(RetrySettings retrySettings) {
        this.retrySettings = retrySettings;
    }

    public PathConverter getPathConverter() {
        return pathConverter;
    }

    public void setPathConverter(PathConverter pathConverter) {
        this.pathConverter = pathConverter;
    }
}
