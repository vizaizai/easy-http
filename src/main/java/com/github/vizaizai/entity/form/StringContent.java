package com.github.vizaizai.entity.form;

import com.github.vizaizai.entity.ContentType;
import com.github.vizaizai.util.Assert;
import com.github.vizaizai.util.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 字符串请求体内容
 * @author liaochongwei
 * @date 2021/2/4 18:02
 */
public class StringContent implements BodyContent {
    private final byte[] content;
    private StringContent(String content, Charset charset) {
        Assert.notNull(content,"content must be not null");
        this.content = content.getBytes(charset == null ? Utils.UTF_8 : charset);
    }

    public static  StringContent of(String content, Charset charset) {
        Assert.notNull(content,"content must be not null");
        return new StringContent(content, charset);
    }

    public static StringContent of(String content) {

        return new StringContent(content, Utils.UTF_8);
    }
    @Override
    public String getFilename() {
        return null;
    }

    @Override
    public String getContentType() {
        return ContentType.TEXT_PLAIN;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.content);
    }

    @Override
    public boolean isFile() {
        return false;
    }
}
