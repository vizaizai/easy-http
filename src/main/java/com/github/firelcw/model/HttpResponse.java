package com.github.firelcw.model;

/**
 * 响应
 * @author liaochongwei
 * @date 2020/7/30 17:32
 */
public class HttpResponse {

    /**
     * 返回体
     */
    private String body;
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


    public boolean isOk() {
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
}
