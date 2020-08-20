package com.github.firelcw.interceptor;


import com.github.firelcw.model.HttpRequest;
import com.github.firelcw.model.HttpRequestConfig;
import com.github.firelcw.model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 时间拦截器
 * @author liaochongwei
 * @date 2020/7/31 14:06
 */

public class TimeInterceptor implements HttpInterceptor{
    private static final Logger log = LoggerFactory.getLogger(TimeInterceptor.class);
    private long startTime;

    @Override
    public boolean preHandle(HttpRequest request, HttpRequestConfig config) {
        startTime = System.currentTimeMillis();
        return true;
    }

    @Override
    public void postHandle(HttpRequest request, HttpResponse response) {
        long endTime = System.currentTimeMillis();
        log.debug("请求 => {},耗时为{}毫秒",request.getUrl(), endTime - startTime);
    }
}
