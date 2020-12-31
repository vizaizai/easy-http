package com.github.vizaizai.proxy;


import com.github.vizaizai.client.AbstractClient;
import com.github.vizaizai.codec.Decoder;
import com.github.vizaizai.codec.Encoder;
import com.github.vizaizai.exception.EasyHttpException;
import com.github.vizaizai.interceptor.HttpInterceptor;
import com.github.vizaizai.model.HttpRequestConfig;
import com.github.vizaizai.model.ProxyMode;
import com.github.vizaizai.model.RetrySettings;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author liaochongwei
 * @date 2020/7/30 14:09
 */
public class ProxyHandler<T>{
    private final ProxyContext<T> proxyContext;
    public ProxyHandler(Class<T> targetClazz) {
        proxyContext = new ProxyContext<>(targetClazz);
    }

    public T getProxyImpl() {
        T proxy;
        if (ProxyMode.JDK.equals(this.proxyContext.getProxyMode())) {
            proxy = new JDKProxy<>(this.proxyContext).getProxy();
        }else if (ProxyMode.BYTE_BUDDY.equals(this.proxyContext.getProxyMode())) {
            proxy = new ByteBuddyProxy<>(this.proxyContext).getProxy();
        }else {
            throw new EasyHttpException("No proxy implementation");
        }
        return proxy;
    }

    public ProxyHandler<T> client(AbstractClient client) {
        this.proxyContext.setClient(client);
        return this;
    }
    public ProxyHandler<T> url(String url) {
        this.proxyContext.setUrl(url);
        return this;
    }
    public ProxyHandler<T> encoder(Encoder encoder) {
        this.proxyContext.setEncoder(encoder);
        return this;
    }
    public ProxyHandler<T> decoder(Decoder decoder) {
        this.proxyContext.setDecoder(decoder);
        return this;
    }
    public ProxyHandler<T> requestConfig(HttpRequestConfig requestConfig) {
        this.proxyContext.setRequestConfig(requestConfig);
        return this;
    }
    public ProxyHandler<T> interceptors(List<HttpInterceptor> interceptors) {
        this.proxyContext.setInterceptors(interceptors);
        return this;
    }
    public ProxyHandler<T> executor(Executor executor) {
        this.proxyContext.setExecutor(executor);
        return this;
    }
    public ProxyHandler<T> enableRetry(RetrySettings retrySettings) {
        this.proxyContext.setRetrySettings(retrySettings);
        return this;
    }
    public ProxyHandler<T> proxy(ProxyMode proxyMode) {
        this.proxyContext.setProxyMode(proxyMode);
        return this;
    }
}