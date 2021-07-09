package com.github.vizaizai.interceptor;

/**
 * @author liaochongwei
 * @date 2021/7/9 15:40
 */
public interface InterceptorGenerator {

    HttpInterceptor get(Class<? extends HttpInterceptor> clazz);
}
