package com.github.firelcw.interceptor;


import com.github.firelcw.model.ExcludePath;
import com.github.firelcw.model.HttpRequest;
import com.github.firelcw.model.HttpRequestConfig;
import com.github.firelcw.model.HttpResponse;

import java.util.Collections;
import java.util.List;

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
     * @return boolean
     */
    default boolean preHandle(HttpRequest request, HttpRequestConfig config){
        return true;
    }

    /**
     * 后置拦截
     * @param request
     * @param response
     */
    default void postHandle(HttpRequest request, HttpResponse response){
    }

    /**
     * 执行顺序(值越小越先执行)
     * @return int
     */
    default int order() {
        return 0;
    }
    /**
     *  排除路径
     * @return List<ExcludePath>
     */
    default List<ExcludePath> excludes() {
        return Collections.emptyList();
    }
}
