package com.github.vizaizai.interceptor;

import com.github.vizaizai.exception.EasyHttpException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认拦截器生成器
 * @author liaochongwei
 * @date 2021/7/9 15:40
 */
public class DefaultInterceptorGenerator implements InterceptorGenerator{
    /**
     * 默认生成器实例
     */
    private static final DefaultInterceptorGenerator defaultInterceptorGenerator = new DefaultInterceptorGenerator();
    /**
     * 拦截器缓存
     */
    private static final Map<Class<? extends HttpInterceptor>, HttpInterceptor> httpInterceptorCache = new ConcurrentHashMap<>();

    @Override
    public HttpInterceptor get(Class<? extends HttpInterceptor> clazz) {
        try {
            if (httpInterceptorCache.containsKey(clazz)) {
                return httpInterceptorCache.get(clazz);
            }
            HttpInterceptor httpInterceptor = clazz.newInstance();
            httpInterceptorCache.put(clazz,httpInterceptor);
            return httpInterceptor;
        }catch (Exception ex) {
            throw new EasyHttpException("Create instance 'interceptor' error.", ex);
        }
    }

    public static DefaultInterceptorGenerator getGenerator() {
        return defaultInterceptorGenerator;
    }
}
