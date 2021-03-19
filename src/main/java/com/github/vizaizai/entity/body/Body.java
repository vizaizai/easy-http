package com.github.vizaizai.entity.body;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * 响应体
 * @author liaochongwei
 * @date 2021/1/11 14:43
 */
public interface Body {
    /**
     * 字节数
     */
    long length();

    /**
     * 是否可重复读取
     */
    boolean isRepeatable();

    /**
     * 转化为InputStream
     */
    InputStream asInputStream();

    /**
     * 转化为Reader
     * @param charset 编码
     */
    Reader asReader(Charset charset) throws IOException;

    /**
     * 转化为字符串
     * @param charset 编码
     */
    String asString(Charset charset) throws IOException;

    /**
     * 写入输出流
     */
    void writeTo(OutputStream os) throws IOException;
}
