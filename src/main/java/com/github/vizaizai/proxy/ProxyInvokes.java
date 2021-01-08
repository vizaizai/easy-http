package com.github.vizaizai.proxy;

import com.github.vizaizai.hander.AsyncHttpHandler;
import com.github.vizaizai.hander.HttpHandler;
import com.github.vizaizai.hander.RequestHandler;
import com.github.vizaizai.hander.ResponseHandler;

import java.lang.reflect.Method;

/**
 * 代理调用工具
 * @author liaochongwei
 * @date 2020/12/30 17:22
 */
public class ProxyInvokes {
    private ProxyInvokes() {
    }

    /**
     * 调用
     * @param method 目标方法
     * @param args 方法参数
     * @param proxyContext 环境
     * @return 代理返回结果
     */
    public static Object invoke(Method method, Object[] args, ProxyContext<?> proxyContext) {
        // 构建请求处理
        RequestHandler requestHandler = RequestHandler.create(proxyContext, method, args);
        // 构建响应处理
        ResponseHandler responseHandler = ResponseHandler.create(proxyContext, requestHandler);
        // 异步返回
        if (requestHandler.getRequest().isAsync()) {
            return AsyncHttpHandler.create(requestHandler, responseHandler)
                    .addExecutor(proxyContext.getExecutor())
                    .execute();
        }
        return HttpHandler.create(requestHandler,responseHandler).execute();
    }
}
