package com.github.firelcw.hander;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * 异步HTTP处理器
 * @author liaochongwei
 * @date 2020/8/31 16:13
 */
public class AsyncHttpHandler extends HttpHandler {
    /**
     * 线程池
     */
    private Executor executor;

    public AsyncHttpHandler(RequestHandler requestHandler, ResponseHandler responseHandler) {
        super(requestHandler,responseHandler);
    }

    public static AsyncHttpHandler create(RequestHandler requestHandler, ResponseHandler responseHandler) {
        return new AsyncHttpHandler(requestHandler, responseHandler);
    }

    public AsyncHttpHandler addExecutor(Executor executor) {
        this.executor = executor;
        return this;
    }
    @Override
    public Object execute() {
        if (executor == null) {
            return CompletableFuture.supplyAsync(this::run);
        }
        return CompletableFuture.supplyAsync(this::run, executor);
    }
    /**
     * 异步执行方法
     * @return Object
     */
    private Object run() {
       return responseHandler.response(requestHandler.execute()).execute();
    }
    public Executor getExecutor() {
        return executor;
    }


}
