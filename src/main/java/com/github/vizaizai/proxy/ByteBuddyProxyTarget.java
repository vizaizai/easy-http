package com.github.vizaizai.proxy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;

/**
 * 代理委托
 * @author liaochongwei
 * @date 2020/12/30 15:14
 */
public class ByteBuddyProxyTarget {
    private final ProxyContext<?> proxyContext;

    public ByteBuddyProxyTarget(ProxyContext<?> proxyContext) {
        this.proxyContext = proxyContext;
    }

    @RuntimeType
    public Object interceptor(@This Object proxy, @Origin Method method,
                              @AllArguments Object[] args){
        return ProxyInvokes.invoke(method, args, proxyContext);
    }
}
