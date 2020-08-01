package com.github.firelcw.interceptor;


import com.github.firelcw.exception.EasyHttpException;
import com.github.firelcw.model.HttpRequest;
import com.github.firelcw.model.HttpRequestConfig;
import com.github.firelcw.model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 错误拦截器
 * @author liaochongwei
 * @date 2020/7/31 13:43
 */
public class ErrorInterceptor implements HttpInterceptor{


    private static final Logger log = LoggerFactory.getLogger(ErrorInterceptor.class);
    @Override
    public boolean preHandle(HttpRequest request, HttpRequestConfig config) {
        return true;
    }

    @Override
    public void postHandle(HttpRequest request, HttpResponse response) {
        if (!response.isSuccess()) {
            log.info("请求错误 => {}, 状态码:{},原因:{}",request.getUrl(),response.getStatusCode(),response.getMessage());
            throw new EasyHttpException("Request error");
        }
    }

    @Override
    public int order() {
        return 1;
    }
}
