package com.github.vizaizai.entity;

import com.github.vizaizai.entity.body.Body;
import com.github.vizaizai.util.value.HeadersNameValues;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * 响应
 * @author liaochongwei
 * @date 2020/7/30 17:32
 */
public class HttpResponse {

    /**
     * 响应体
     */
    private Body body;
    /**
     * 字符编码
     */
    private Charset encoding;
    /**
     * 响应头
     */
    private HeadersNameValues headers;
    /**
     * 返回类型
     */
    private Type returnType;
    /**
     * 返回是否已经序列化
     */
    private boolean deserialize = false;
    /**
     * 序列化后的响应体
     */
    private Object returnObject;
    /**
     * 响应码
     */
    private int statusCode = -1;
    /**
     * 消息
     */
    private String message;
    /**
     * 发生的异常信息
     */
    private Throwable cause;

    public boolean  isOk() {
        return statusCode >= 200 && statusCode <= 300 ;
    }
    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public boolean isDeserialize() {
        return deserialize;
    }

    public void setDeserialize(boolean deserialize) {
        this.deserialize = deserialize;
    }

    public Object getReturnObject() {
        return returnObject;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public void setReturnObject(Object returnObject) {
        this.setDeserialize(true);
        this.returnObject = returnObject;
    }

    public HeadersNameValues getHeaders() {
        return headers;
    }

    public void setHeaders(HeadersNameValues headers) {
        this.headers = headers;
    }

    public Charset getEncoding() {
        return encoding;
    }

    public void setEncoding(Charset encoding) {
        this.encoding = encoding;
    }

    public static HttpResponse ok(Body body) {
        HttpResponse response = new HttpResponse();
        response.setMessage("ok");
        response.setStatusCode(200);
        response.setBody(body);
        return response;
    }

    public static HttpResponse exception(Throwable cause) {
        HttpResponse response = new HttpResponse();
        response.setMessage(cause.getMessage());
        response.cause = cause;
        return response;
    }
}
