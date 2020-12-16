package com.github.vizaizai.hander;

import com.github.vizaizai.codec.Decoder;
import com.github.vizaizai.codec.Encoder;
import com.github.vizaizai.exception.EasyHttpException;
import com.github.vizaizai.interceptor.HttpInterceptor;
import com.github.vizaizai.model.HttpRequest;
import com.github.vizaizai.model.HttpResponse;
import com.github.vizaizai.model.RetrySettings;
import com.github.vizaizai.parser.ArgParser;
import com.github.vizaizai.parser.InterfaceParser;
import com.github.vizaizai.parser.MethodParser;

import java.util.List;

/**
 * HTTP处理器
 * @author liaochongwei
 * @date 2020/8/31 16:13
 */
public class HttpHandler implements Handler<Object>, Context{
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
        Object result;
        if (RetryHandler.enableRetry(this.requestHandler.getRetrySettings())) {
            result = new RetryHandler(this).execute();
        }else {
            result = this.doHttp();
        }
        HttpResponse response = this.responseHandler.getResponse();
        Throwable cause = response.getCause();
        // 没有异常则直接返回
        if (response.getCause() == null) {
            return result;
        }
        if (cause instanceof RuntimeException) {
            throw (RuntimeException)cause;
        }
        if (cause instanceof Error) {
            throw (Error)cause;
        }
        throw new EasyHttpException(cause);

    }

    public Object doHttp() {
        Throwable cause = null;
        Object result = null;
        try {
            result = responseHandler.response(requestHandler.execute()).execute();
        }catch (Throwable ex) {
            cause = ex;
        }
        //发生了异常
        if (cause != null) {
            HttpResponse response = responseHandler.getResponse();
            if (response == null) {
                response = HttpResponse.exception(cause);
            }else {
                response.setCause(cause);
            }
            responseHandler.setResponse(response);
        }
        return result;
    }

    @Override
    public HttpRequest getRequest() {
        return requestHandler.getRequest();
    }

    @Override
    public HttpResponse getResponse() {
        return responseHandler.getResponse();
    }

    @Override
    public List<HttpInterceptor> getInterceptors() {
        return requestHandler.getInterceptorOps().getInterceptors();
    }

    @Override
    public MethodParser getMethodParser() {
        return requestHandler.getMethodParser();
    }

    @Override
    public List<ArgParser> getArgParsers() {
        return requestHandler.getArgParsers();
    }

    @Override
    public InterfaceParser getInterfaceParser() {
        return requestHandler.getInterfaceParser();
    }

    @Override
    public Encoder getEncoder() {
        return requestHandler.getEncoder();
    }

    @Override
    public Decoder getDecoder() {
        return responseHandler.getDecoder();
    }

    @Override
    public RetrySettings getRetrySettings() {
        return requestHandler.getRetrySettings();
    }
}
