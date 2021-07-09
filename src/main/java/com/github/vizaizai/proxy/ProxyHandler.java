package com.github.vizaizai.proxy;


/**
 * @author liaochongwei
 * @date 2020/7/30 14:09
 */
public class ProxyHandler<T>{
    private final ProxyContext<T> proxyContext;
    public ProxyHandler(Class<T> targetClazz) {
        proxyContext = new ProxyContext<>(targetClazz);
    }

    public ProxyContext<T> getProxyContext() {
        return proxyContext;
    }

    public T getProxyImpl() {
        return new JDKProxy<>(this.proxyContext).getProxy();
    }

}
