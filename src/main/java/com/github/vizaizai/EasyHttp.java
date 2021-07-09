package com.github.vizaizai;

import com.github.vizaizai.client.AbstractClient;
import com.github.vizaizai.client.ApacheHttpClient;
import com.github.vizaizai.codec.*;
import com.github.vizaizai.hander.mapping.PathConverter;
import com.github.vizaizai.interceptor.HttpInterceptor;
import com.github.vizaizai.entity.HttpRequestConfig;
import com.github.vizaizai.entity.RetrySettings;
import com.github.vizaizai.interceptor.InterceptorGenerator;
import com.github.vizaizai.proxy.ProxyContext;
import com.github.vizaizai.proxy.ProxyHandler;
import com.github.vizaizai.retry.RetryTrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author liaochongwei
 * @date 2020/7/31 11:19
 */
public class EasyHttp {
    private EasyHttp() {
    }

    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private AbstractClient client;
        private HttpRequestConfig config;
        private Decoder decoder;
        private Encoder encoder;
        private String url;
        private final List<HttpInterceptor> interceptors;
        private Executor executor;
        private RetrySettings retrySettings;
        private PathConverter pathConverter;
        private InterceptorGenerator interceptorGenerator;
        public Builder() {
            this.client = ApacheHttpClient.getInstance();
            this.encoder = new JacksonEncoder();
            this.decoder = new JacksonDecoder();
            this.config =  HttpRequestConfig.defaultConfig();
            this.interceptors = new ArrayList<>();
        }
        public Builder client(AbstractClient client) {
            this.client = client;
            return this;
        }
        public Builder decoder(Decoder decoder) {
            this.decoder = decoder;
            return this;
        }
        public Builder encoder(Encoder encoder) {
            this.encoder = encoder;
            return this;
        }
        public Builder config(HttpRequestConfig config) {
            this.config = config;
            return this;
        }
        public Builder url(String url) {
            this.url = url;
            return this;
        }
        public Builder withInterceptor(HttpInterceptor interceptor) {
            if (!this.interceptorIsExists(interceptor)) {
                this.interceptors.add(interceptor);
            }
            return this;
        }
        public Builder executor(Executor executor) {
            this.executor = executor;
            return this;
        }
        /**
         * 可重试
         * @param retries 最大重试次数
         * @param interval 间隔时间(ms)
         * @param retryTrigger 重试规则
         * @return
         */
        public Builder retryable(Integer retries, Integer interval, RetryTrigger retryTrigger) {
            this.retrySettings = new RetrySettings();
            this.retrySettings.setEnable(true);
            this.retrySettings.setMaxAttempts(retries);
            this.retrySettings.setIntervalTime(interval);
            this.retrySettings.setRetryTrigger(retryTrigger);
            return this;
        }

        public Builder retryable(Integer retries, Integer interval) {
            return this.retryable(retries,interval, null);
        }

        public Builder pathConverter(PathConverter pathConverter) {
            this.pathConverter = pathConverter;
            return this;
        }

        public Builder interceptorGenerator(InterceptorGenerator interceptorGenerator) {
            this.interceptorGenerator = interceptorGenerator;
            return this;
        }

        public <T> T build(Class<T> clazz) {
            ProxyHandler<T> proxyHandler = new ProxyHandler<>(clazz);
            ProxyContext<T> proxyContext = proxyHandler.getProxyContext();

            proxyContext.setClient(client);
            proxyContext.setUrl(url);
            proxyContext.setDecoder(decoder);
            proxyContext.setEncoder(encoder);
            proxyContext.setRequestConfig(config);
            proxyContext.setInterceptors(interceptors);
            proxyContext.setRetrySettings(retrySettings);
            proxyContext.setPathConverter(pathConverter);
            proxyContext.setExecutor(executor);
            proxyContext.setInterceptorGenerator(interceptorGenerator);

            return proxyHandler.getProxyImpl();
        }

        /**
         * 判断拦截器是否存在
         * @param interceptor
         * @return boolean
         */
        private boolean interceptorIsExists(HttpInterceptor interceptor){
            String name = interceptor.getClass().getName();
            return this.interceptors.stream()
                             .anyMatch(e -> e.getClass().getName().equals(name));
        }
    }
}
