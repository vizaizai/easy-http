package com.github.vizaizai;

import com.github.vizaizai.client.AbstractClient;
import com.github.vizaizai.client.ApacheHttpClient;
import com.github.vizaizai.codec.Decoder;
import com.github.vizaizai.codec.DefaultDecoder;
import com.github.vizaizai.codec.DefaultEncoder;
import com.github.vizaizai.codec.Encoder;
import com.github.vizaizai.interceptor.HttpInterceptor;
import com.github.vizaizai.model.HttpRequestConfig;
import com.github.vizaizai.model.RetrySettings;
import com.github.vizaizai.proxy.HttpInvocationHandler;
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

        public Builder() {
            this.client = ApacheHttpClient.getInstance();
            this.encoder = new DefaultEncoder();
            this.decoder = new DefaultDecoder();
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


        public <T> T build(Class<T> clazz) {
            HttpInvocationHandler<T> invocationHandler = new HttpInvocationHandler<>(clazz);
            invocationHandler.client(client)
                             .url(url)
                             .decoder(decoder)
                             .encoder(encoder)
                             .requestConfig(config)
                             .interceptors(interceptors)
                             .enableRetry(retrySettings)
                             .executor(executor);
            return invocationHandler.getProxy();
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