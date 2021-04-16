package com.github.vizaizai.interceptor;

import com.github.vizaizai.exception.HttpInterceptorException;
import com.github.vizaizai.entity.HttpMethod;
import com.github.vizaizai.entity.HttpRequest;
import com.github.vizaizai.entity.HttpResponse;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 拦截器执行工具
 * @author liaochongwei
 * @date 2020/8/31 17:11
 */
public class InterceptorOperations {
    private List<HttpInterceptor> interceptors;
    private InterceptorOperations() {
    }
    public static InterceptorOperations create(List<HttpInterceptor> interceptors) {
        InterceptorOperations operation = new InterceptorOperations();
        if (interceptors == null) {
            operation.interceptors = Collections.emptyList();
        }else {
            operation.interceptors = interceptors;
        }
        return operation;
    }

    public void addInterceptors(HttpInterceptor ...interceptors) {
        if (interceptors == null) {
            return;
        }
        if (this.interceptors.isEmpty()) {
            this.interceptors = new ArrayList<>();
        }
        this.interceptors.addAll(Arrays.asList(interceptors));
    }

    public void addInterceptors(List<HttpInterceptor> interceptors) {
        if (CollectionUtils.isEmpty(interceptors)) {
            return;
        }
        if (this.interceptors.isEmpty()) {
            this.interceptors = new ArrayList<>();
        }
        this.interceptors.addAll(interceptors);
    }
    /**
     * 拦截器排除
     */
    public void exclude(String url, HttpMethod method) {
        if (CollectionUtils.isEmpty(interceptors)) {
            return;
        }
        interceptors.removeIf(e-> e.excludes().stream().anyMatch( n -> n.match(url, method)));
    }

    /**
     * 拦截器排序
     */
    public void ordered() {
        if (CollectionUtils.isEmpty(interceptors)) {
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
