package com.github.vizaizai.exception;

/**
 * @author liaochongwei
 * @date 2020/7/30 15:05
 */
public class CodecException extends RuntimeException{
    public CodecException(String message) {
        super(message);
    }
    public CodecException(Throwable cause) {
        super(cause);
    }
    public CodecException(String message, Throwable cause) {
        super(message, cause);
    }
}
