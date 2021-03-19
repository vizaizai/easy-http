package com.github.vizaizai.entity.body;

import com.github.vizaizai.util.Assert;
import com.github.vizaizai.util.StreamUtils;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author liaochongwei
 * @date 2021/1/11 15:06
 */
public class ByteArrayBody implements Body{
    private final byte[] data;

    public ByteArrayBody(byte[] data) {
        this.data = data;
    }

    public static Body ofNullable(byte[] data) {
        if (data == null) {
            return null;
        }
        return new ByteArrayBody(data);
    }

    public static Body ofNullable(String text, Charset charset) {
        if (text == null) {
            return null;
        }
        Assert.notNull(charset, "charset must be not null");
        return new ByteArrayBody(text.getBytes(charset));
    }

    @Override
    public long length() {
        return data.length;
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public InputStream asInputStream() {
        return new ByteArrayInputStream(data);
    }

    @Override
    public Reader asReader(Charset charset) throws IOException {
        Assert.notNull(charset, "charset should not be null");
        return new InputStreamReader(asInputStream(), charset);
    }

    @Override
    public String asString(Charset charset) throws IOException {
        return new String(this.data, charset);
    }

    @Override
    public void writeTo(OutputStream os) throws IOException {
        StreamUtils.copy(this.asInputStream(),os);
    }
}
