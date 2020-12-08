package com.github.vizaizai.interceptor;


import com.github.vizaizai.model.HttpRequest;
import com.github.vizaizai.model.HttpResponse;
import com.github.vizaizai.util.Utils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http日志拦截器
 * @author liaochongwei
 * @date 2020/7/31 14:06
 */

public class LogInterceptor implements HttpInterceptor{
    private static final Logger log = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public boolean preHandle(HttpRequest request) {
        request.setStartTime(System.currentTimeMillis());
        if (!log.isDebugEnabled()) {
           return true;
        }
        String method = request.getMethod() == null ? "" : request.getMethod().name();
        log.debug("请求行: {} {}",method, request.getUrl());
        if (MapUtils.isNotEmpty(request.getHeaders())) {
            StringBuilder sb = new StringBuilder();
            request.getHeaders().forEach((k,v)-> {
                sb.append(k).append(":").append(v).append(", ");
            });
            sb.deleteCharAt(sb.length() - 2);
            log.debug("请求头: {}", sb);
        }
        if (MapUtils.isNotEmpty(request.getQueryParams())) {
            log.debug("请求参数: {}", Utils.asUrlEncoded(request.getQueryParams()));
        }
        if (request.getBody() != null) {
            log.debug("请求体: {}",request.getBody());
        }
        return true;
    }

    @Override
    public void postHandle(HttpRequest request, HttpResponse response) {
        long endTime = System.currentTimeMillis();
        if (!log.isDebugEnabled()) {
            return;
        }
        log.debug("请求响应: {} [{}]:{} ",request.getUrl(), response.getStatusCode(), response.getMessage());
        log.debug("响应体: {}", response.getBody());
        log.debug("耗时: {}毫秒",endTime - request.getStartTime());
    }
}
