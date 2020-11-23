package com.github.firelcw.model;

import java.lang.reflect.Type;

/**
 * 响应
 * @author liaochongwei
 * @date 2020/7/30 17:32
 */
public class HttpResponse {

    /**
     * 响应体
     */
    private String body;
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
     * 响应大小
     */
    private long contentLength;
    /**
     * 消息
     */
    private String message;


    public boolean  isOk() {
        return statusCode >= 200 && statusCode <= 300 ;
    }
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
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

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
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

    public void setReturnObject(Object returnObject) {
        this.setDeserialize(true);
        this.returnObject = returnObject;
    }

    public static HttpResponse ok(String body) {
        HttpResponse response = new HttpResponse();
        response.setMessage("ok");
        response.setStatusCode(200);
        response.setBody(body);
        return response;
    }
}
