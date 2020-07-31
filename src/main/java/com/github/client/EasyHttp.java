package com.github.client;

import com.github.codec.*;
import com.github.interceptor.HttpInterceptor;
import com.github.model.HttpRequestConfig;
import com.github.proxy.HttpInvocationHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liaochongwei
 * @date 2020/7/31 11:19
 */
public class EasyHttp {
    private EasyHttp() {
    }

    public static EasyHttp.Builder builder() {
        return new EasyHttp.Builder();
    }
    public static class Builder {
        private HttpRequestConfig config;
        private Decoder decoder;
        private Encoder encoder;
        private String url;
        private final List<HttpInterceptor> interceptors;

        public Builder() {
            this.encoder = new DefaultEncoder();
            this.decoder = new DefaultDecoder();
            this.config = new HttpRequestConfig();
            this.interceptors = new ArrayList<>();
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
            this.interceptors.add(interceptor);
            return this;
        }

        public <T> T build(Class<T> clazz) {
            HttpInvocationHandler<T> invocationHandler = new HttpInvocationHandler<>(clazz);
            invocationHandler.url(url);
            invocationHandler.decoder(decoder);
            invocationHandler.encoder(encoder);
            invocationHandler.requestConfig(config);
            invocationHandler.interceptors(interceptors);
            return invocationHandler.getProxy();
        }

    }
}
