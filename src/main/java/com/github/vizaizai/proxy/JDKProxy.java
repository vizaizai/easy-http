package com.github.vizaizai.proxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author liaochongwei
 * @date 2020/7/30 14:09
 */
public class JDKProxy<T> implements InvocationHandler {

    private final ProxyContext<T> proxyContext;
    public JDKProxy(ProxyContext<T> proxyContext) {
        this.proxyContext = proxyContext;
    }

    @SuppressWarnings("unchecked")
    public T getProxy() {
        return (T) Proxy.newProxyInstance(proxyContext.getTargetClazz().getClassLoader(), new Class[]{ proxyContext.getTargetClazz() }, this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return Object.class.equals(method.getDeclaringClass())
                ? method.invoke(this, args)
                : ProxyInvokes.invoke(method, args, proxyContext);
    }


}
