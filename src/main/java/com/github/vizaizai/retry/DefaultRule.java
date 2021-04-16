package com.github.vizaizai.retry;

import com.github.vizaizai.hander.Context;
import com.github.vizaizai.entity.HttpMethod;
import com.github.vizaizai.entity.HttpRequest;
import com.github.vizaizai.entity.HttpResponse;

import java.net.ConnectException;

/**
 * 默认触发规则
 * 1.当请求方式为GET并且HTTP状态码为5XX时
 * 2.当连接超时时触发重试
 * @author liaochongwei
 * @date 2020/12/16 14:13
 */
public class DefaultRule implements RetryTrigger {
    @Override
    public boolean retryable(Context context) {
        HttpRequest request = context.getRequest();
        HttpResponse response = context.getResponse();
        // 连接被拒绝
        if (response.getCause() instanceof ConnectException) {
            return true;
        }
        if (!HttpMethod.GET.equals(request.getMethod())) {
            return false;
        }
        // GET && code >= 500
        return response.getStatusCode() >= 500;
    }
}
