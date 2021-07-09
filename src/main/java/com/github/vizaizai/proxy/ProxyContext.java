package com.github.vizaizai.proxy;

import com.github.vizaizai.client.AbstractClient;
import com.github.vizaizai.codec.Decoder;
import com.github.vizaizai.codec.Encoder;
import com.github.vizaizai.hander.mapping.PathConverter;
import com.github.vizaizai.interceptor.HttpInterceptor;
import com.github.vizaizai.entity.HttpRequestConfig;
import com.github.vizaizai.entity.RetrySettings;
import com.github.vizaizai.interceptor.InterceptorGenerator;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * 代理环境
 * @author liaochongwei
 * @date 2020/12/30 15:21
 */
public class ProxyContext<T> {
    /**
     * 接口类对象
     */
    private final Class<T> targetClazz;
    /**
     * 客户端
     */
    private AbstractClient client;
    /**
     * 请求地址
     */
    private String url;
    /**
     * 编码器
     */
    private Encoder encoder;
    /**
     * 解码器
     */
    private Decoder decoder;
    /**
     * 请求配置
     */
    private HttpRequestConfig requestConfig;
    /**
     * 拦截器列表
     */
    private List<HttpInterceptor> interceptors;
    /**
     * 异步请求线程池
     */
    private Executor executor;
    /**
     * 重试参数
     */
    private RetrySettings retrySettings;
    /**
     * 路径转化器
     */
    private PathConverter pathConverter;
    /**
     * 拦截器生成器
     */
    private InterceptorGenerator interceptorGenerator;

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

    public InterceptorGenerator getInterceptorGenerator() {
        return interceptorGenerator;
    }

    public void setInterceptorGenerator(InterceptorGenerator interceptorGenerator) {
        this.interceptorGenerator = interceptorGenerator;
    }
}
