package com.github.vizaizai.proxy;


import com.github.vizaizai.client.AbstractClient;
import com.github.vizaizai.codec.Decoder;
import com.github.vizaizai.codec.Encoder;
import com.github.vizaizai.hander.AsyncHttpHandler;
import com.github.vizaizai.hander.HttpHandler;
import com.github.vizaizai.hander.RequestHandler;
import com.github.vizaizai.hander.ResponseHandler;
import com.github.vizaizai.interceptor.HttpInterceptor;
import com.github.vizaizai.model.HttpRequestConfig;
import com.github.vizaizai.model.RetrySettings;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author liaochongwei
 * @date 2020/7/30 14:09
 */
public class HttpInvocationHandler<T> implements InvocationHandler {

    private final Class<T> targetClazz;
    private AbstractClient client;
    private String url;
    private Encoder encoder;
    private Decoder decoder;
    private HttpRequestConfig requestConfig;
    private List<HttpInterceptor> interceptors;
    private Executor executor;
    private RetrySettings retrySettings;

    public HttpInvocationHandler(Class<T> targetClazz) {
        this.targetClazz = targetClazz;
    }

    @SuppressWarnings("unchecked")
    public T getProxy() {
        return (T) Proxy.newProxyInstance(targetClazz.getClassLoader(), new Class[]{ targetClazz }, this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 构建请求处理
        RequestHandler requestHandler = RequestHandler.create(this, method, args);
        // 构建响应处理
        ResponseHandler responseHandler = ResponseHandler.create(this, requestHandler);

        // 异步返回
        if (requestHandler.getRequest().isAsync()) {
            return AsyncHttpHandler.create(requestHandler, responseHandler)
                    .addExecutor(this.executor)
                    .execute();
        }

        return HttpHandler.create(requestHandler,responseHandler).execute();
    }

    public HttpInvocationHandler<T> client(AbstractClient client) {
        this.client = client;
        return this;
    }
    public HttpInvocationHandler<T> url(String url) {
        this.url = url;
        return this;
    }
    public HttpInvocationHandler<T> encoder(Encoder encoder) {
        this.encoder = encoder;
        return this;
    }
    public HttpInvocationHandler<T> decoder(Decoder decoder) {
        this.decoder = decoder;
        return this;
    }
    public HttpInvocationHandler<T> requestConfig(HttpRequestConfig requestConfig) {
        this.requestConfig = requestConfig;
        return this;
    }
    public HttpInvocationHandler<T> interceptors(List<HttpInterceptor> interceptors) {
        this.interceptors = interceptors;
        return this;
    }
    public HttpInvocationHandler<T> executor(Executor executor) {
        this.executor = executor;
        return this;
    }
    public HttpInvocationHandler<T> enableRetry(RetrySettings retrySettings) {
        this.retrySettings = retrySettings;
        return this;
    }

    public Class<T> getTargetClazz() {
        return targetClazz;
    }

    public AbstractClient getClient() {
        return client;
    }

    public String getUrl() {
        return url;
    }

    public Encoder getEncoder() {
        return encoder;
    }

    public Decoder getDecoder() {
        return decoder;
    }

    public HttpRequestConfig getRequestConfig() {
        return requestConfig;
    }

    public List<HttpInterceptor> getInterceptors() {
        return interceptors;
    }

    public RetrySettings getRetrySettings() {
        return retrySettings;
    }
}
