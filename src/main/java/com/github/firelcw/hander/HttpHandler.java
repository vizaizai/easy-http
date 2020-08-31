package com.github.firelcw.hander;

/**
 * HTTP处理器
 * @author liaochongwei
 * @date 2020/8/31 16:13
 */
public class HttpHandler implements Handler<Object>{
    /**
     * 请求处理
     */
    protected final RequestHandler requestHandler;
    /**
     * 响应处理
     */
    protected final ResponseHandler responseHandler;

    public HttpHandler(RequestHandler requestHandler, ResponseHandler responseHandler) {
        this.requestHandler = requestHandler;
        this.responseHandler = responseHandler;
    }

    public static HttpHandler create(RequestHandler requestHandler, ResponseHandler responseHandler) {
        return new HttpHandler(requestHandler, responseHandler);
    }


    @Override
    public Object execute() {
        return responseHandler.response(requestHandler.execute()).execute();
    }
}
