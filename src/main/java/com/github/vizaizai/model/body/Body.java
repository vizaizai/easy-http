package com.github.vizaizai.model.body;

import java.io.IOException;
import java.io.InputStream;
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
    Integer length();

    /**
     * 是否可重复读取
     */
    boolean isRepeatable();

    /**
     * 转化为InputStream
     */
    InputStream asInputStream() throws IOException;

    /**
     * 转化为Reader
     * @param charset 编码
     */
    Reader asReader(Charset charset) throws IOException;

}
