package com.github.vizaizai.proxy;


import com.github.vizaizai.util.Utils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * 通过字节码生成实现类
 * @author liaochongwei
 * @date 2020/7/30 14:09
 */
public class ByteBuddyProxy<T> {

    private final ProxyContext<T> proxyContext;
    public ByteBuddyProxy(ProxyContext<T> proxyContext) {
        this.proxyContext = proxyContext;
    }

    public T getProxy() {
        Class<T> clazz =  proxyContext.getTargetClazz();
        String className = clazz.getName() + "Impl"; // 实现类名
        try {
            DynamicType.Unloaded<?> dynamicType = new ByteBuddy(ClassFileVersion.JAVA_V8)
                    .subclass(Object.class)
                    .implement(clazz) // 实现接口
                    .name(className) //设置类名
                    .method(ElementMatchers.isAbstract()) // 只匹配抽象方法
                    .intercept(MethodDelegation.to(new ByteBuddyProxyTarget(this.proxyContext)))// 委托拦截实现
                    .make(); //构建类
            Utils.outputClazz(dynamicType.getBytes());
            return (T)dynamicType.load(ByteBuddyProxy.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER) // 加载类
                                 .getLoaded()
                                 .newInstance(); // 创建实例

        }catch (Exception e) {
            return null;
        }
    }

    public ProxyContext<T> getProxyContext() {
        return proxyContext;
    }
}
