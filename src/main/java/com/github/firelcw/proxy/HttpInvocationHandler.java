package com.github.firelcw.proxy;


import com.github.firelcw.codec.Decoder;
import com.github.firelcw.codec.Encoder;
import com.github.firelcw.hander.HttpHandler;
import com.github.firelcw.interceptor.HttpInterceptor;
import com.github.firelcw.model.HttpRequestConfig;
import com.github.firelcw.hander.RequestHandler;
import com.github.firelcw.parser.ArgParser;
import com.github.firelcw.parser.MethodParser;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liaochongwei
 * @date 2020/7/30 14:09
 */
public class HttpInvocationHandler<T> implements InvocationHandler {

    private final Class<T> targetClazz;
    private String url;
    private Encoder encoder;
    private Decoder decoder;
    private HttpRequestConfig requestConfig;
    private List<HttpInterceptor> interceptors;

    public HttpInvocationHandler(Class<T> targetClazz) {
        this.targetClazz = targetClazz;
    }

    @SuppressWarnings("unchecked")
    public T getProxy() {
        return (T) Proxy.newProxyInstance(targetClazz.getClassLoader(), new Class[]{ targetClazz }, this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        MethodParser methodParser = new MethodParser(method);
        List<ArgParser> argParsers = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            argParsers.add(new ArgParser(args[i],method, i));
        }
        RequestHandler requestHandler = new RequestHandler();
        requestHandler.config(requestConfig);

        requestHandler.setArgParsers(argParsers);
        requestHandler.setMethodParser(methodParser);
        requestHandler.setUrl(url);
        requestHandler.setEncoder(encoder);

        HttpHandler httpHandler = new HttpHandler();
        httpHandler.setInterceptors(interceptors);

        return httpHandler.handle(requestHandler,decoder, method.getGenericReturnType());
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

}
