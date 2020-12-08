package com.github.vizaizai.hander;

import com.github.vizaizai.codec.Decoder;
import com.github.vizaizai.codec.SimpleDecoder;
import com.github.vizaizai.exception.EasyHttpException;
import com.github.vizaizai.interceptor.InterceptorOperations;
import com.github.vizaizai.model.HttpRequest;
import com.github.vizaizai.model.HttpResponse;
import com.github.vizaizai.proxy.HttpInvocationHandler;
import com.github.vizaizai.util.TypeUtils;

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
        this.response.setReturnType(this.returnType);
        return this;
    }

    /**
     * 创建ResponseHandler
     * @param invocation
     * @return ResponseHandler
     */
    public static ResponseHandler create(HttpInvocationHandler<?> invocation, RequestHandler requestHandler) {
        Type returnType = requestHandler.getMethodParser().getTarget().getGenericReturnType();
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
        // 如果已经序列化,则直接返回
        if (this.response.isDeserialize()) {
            return this.response.getReturnObject();
        }
        // 响应解码
        if (TypeUtils.isSimple(this.returnType.getTypeName())) {
            this.decoder = new SimpleDecoder();
        }
        Object returnObject = this.decoder.decode(this.response, this.returnType);
        this.response.setReturnObject(returnObject);
        this.response.setDeserialize(true);
        return returnObject;
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
