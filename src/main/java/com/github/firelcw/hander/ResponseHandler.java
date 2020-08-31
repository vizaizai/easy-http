package com.github.firelcw.hander;

import com.github.firelcw.codec.Decoder;
import com.github.firelcw.codec.SimpleDecoder;
import com.github.firelcw.exception.EasyHttpException;
import com.github.firelcw.interceptor.InterceptorOperations;
import com.github.firelcw.model.HttpRequest;
import com.github.firelcw.model.HttpResponse;
import com.github.firelcw.proxy.HttpInvocationHandler;
import com.github.firelcw.util.TypeUtils;

import java.lang.reflect.Type;

/**
 * 响应处理器
 * @author liaochongwei
 * @date 2020/7/31 13:17
 */
public class ResponseHandler implements Handler<Object>{
    private HttpRequest request;
    private HttpResponse response;
    private InterceptorOperations interceptorOps;
    private Decoder decoder;
    private Type returnType;

    private ResponseHandler() {
    }

    public ResponseHandler response(HttpResponse response) {
        this.response = response;
        return this;
    }

    /**
     * 创建ResponseHandler
     * @param invocation
     * @return ResponseHandler
     */
    public static ResponseHandler create(HttpInvocationHandler<?> invocation, RequestHandler requestHandler) {
        Type returnType = invocation.getMethod().getGenericReturnType();
        ResponseHandler handler = new ResponseHandler();
        handler.request = requestHandler.getRequest();
        handler.returnType = TypeUtils.getDecodeType(returnType);
        handler.decoder = invocation.getDecoder();
        handler.interceptorOps = requestHandler.getInterceptorOps();
        return handler;
    }

    @Override
    public Object execute() {
        if (response == null) {
            throw new EasyHttpException("Response is null");
        }
        // 执行详情拦截
        interceptorOps.doPostInterceptors(this.request, this.response);

        // 响应解码
        if (TypeUtils.isSimple(this.returnType.getTypeName())) {
            this.decoder = new SimpleDecoder();
        }
        return this.decoder.decode(this.response, this.returnType);
    }

    public Type getReturnType() {
        return returnType;
    }

    public Decoder getDecoder() {
        return decoder;
    }

    public HttpResponse getResponse() {
        return response;
    }
}
