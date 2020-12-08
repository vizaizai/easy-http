package com.github.vizaizai.exception;

/**
 * 拦截异常
 * @author liaochongwei
 * @date 2020/7/30 15:05
 */
public class HttpInterceptorException extends RuntimeException{
    public HttpInterceptorException(String message) {
        super(message);
    }
}
