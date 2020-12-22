package com.github.vizaizai.interceptor;


import com.github.vizaizai.model.HttpRequest;
import com.github.vizaizai.model.HttpResponse;
import com.github.vizaizai.util.Utils;
import com.github.vizaizai.util.value.HeadersNameValues;
import com.github.vizaizai.util.value.StringNameValue;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

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
        if (CollectionUtils.isNotEmpty(request.getHeaders())) {
            StringBuilder sb = new StringBuilder();
            HeadersNameValues headers = request.getHeaders();
            Set<String> names = headers.names();
            for (String name : names) {
                sb.append(name).append(":");
                for (String value : headers.getHeaders(name)) {
                    sb.append(value).append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append(" ");
            }
            log.debug("请求头: {}", sb);
        }
        if (CollectionUtils.isNotEmpty(request.getQueryParams())) {
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
