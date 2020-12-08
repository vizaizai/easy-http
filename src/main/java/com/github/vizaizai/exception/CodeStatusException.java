package com.github.vizaizai.exception;

/**
 * HTTP状态码异常
 * @author liaochongwei
 * @date 2020/7/30 15:05
 */
public class CodeStatusException extends RuntimeException{

    /**
     * http状态码
     */
    private final int code;
    public CodeStatusException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
