package com.lcw.interceptor;


import com.lcw.model.HttpRequest;
import com.lcw.model.HttpRequestConfig;
import com.lcw.model.HttpResponse;

/**
 * 响应拦截器
 * @author liaochongwei
 * @date 2020/7/31 13:43
 */
public interface HttpInterceptor {

    /**
     * 前置拦截
     * @param config 请求配置
     * @param request 后置拦截
     * @return
     */
    boolean preHandle(HttpRequest request, HttpRequestConfig config);

    /**
     * 后置拦截
     * @param request
     * @param response
     */
    void postHandle(HttpRequest request, HttpResponse response);

    /**
     * 执行顺序(值越小越先执行)
     * @return
     */
    int order();
}
