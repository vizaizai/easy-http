package com.github.vizaizai.interceptor;

import com.github.vizaizai.entity.HttpMethod;
import com.github.vizaizai.entity.HttpRequest;
import com.github.vizaizai.entity.HttpResponse;
import com.github.vizaizai.exception.HttpInterceptorException;
import com.github.vizaizai.util.VUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 拦截执行器
 * @author liaochongwei
 * @date 2020/8/31 17:11
 */
public class InterceptorExecutor {
    private List<HttpInterceptor> interceptors = new ArrayList<>();
    private InterceptorExecutor() {
    }
    public static InterceptorExecutor create(List<HttpInterceptor> interceptors) {
        InterceptorExecutor operation = new InterceptorExecutor();
        if (interceptors == null) {
            return operation;
        }
        operation.interceptors.addAll(interceptors);
        return operation;
    }

    public void addInterceptors(List<HttpInterceptor> interceptors) {
        if (VUtils.isEmpty(interceptors)) {
            return;
        }
        this.interceptors.addAll(interceptors);
    }

    /**
     * 拦截器排除
     */
    public void exclude(String url, HttpMethod method) {
        if (VUtils.isEmpty(interceptors)) {
            return;
        }
        interceptors.removeIf(e-> e.excludes().stream().anyMatch( n -> n.match(url, method)));
    }

    /**
     * 拦截器排序
     */
    public void ordered() {
        if (VUtils.isEmpty(interceptors)) {
            return;
        }
        this.interceptors = this.interceptors.stream()
                .sorted(Comparator.comparing(HttpInterceptor::order))
                .collect(Collectors.toList());
    }

    /**
     * 执行前置拦截
     * @param request
     */
    public void doPreInterceptors(HttpRequest request) {
        interceptors.forEach(e-> {
            if (!e.preHandle(request)) {
                throw new HttpInterceptorException( "The '" + e.getClass().getSimpleName() +"' failed");
            }
        });
    }

    /**
     * 执行后置拦截
     * @param request
     * @param response
     */
    public void doPostInterceptors(HttpRequest request, HttpResponse response) {
        interceptors.forEach(e->e.postHandle(request, response));
    }

    public List<HttpInterceptor> getInterceptors() {
        return interceptors;
    }
}
