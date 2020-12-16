package com.github.vizaizai.exception;

/**
 * @author liaochongwei
 * @date 2020/7/30 15:05
 */
public class EasyHttpException extends RuntimeException{
    public EasyHttpException(String message) {
        super(message);
    }
    public EasyHttpException(Throwable cause) {
        super(cause);
    }
    public EasyHttpException(String message,Throwable cause) {
        super(message, cause);
    }
}
