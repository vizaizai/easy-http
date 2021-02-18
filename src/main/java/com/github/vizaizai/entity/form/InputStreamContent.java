package com.github.vizaizai.entity.form;

import com.github.vizaizai.entity.ContentType;
import com.github.vizaizai.exception.EasyHttpException;
import com.github.vizaizai.util.Assert;

import java.io.IOException;
import java.io.InputStream;

/** 输入流请求体内容
 * @author liaochongwei
 * @date 2021/2/5 16:15
 */
public class InputStreamContent implements BodyContent {
    private final InputStream inputStream;
    private Integer length;
    private String contentType;
    private String filename;

    private InputStreamContent(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public static InputStreamContent of(InputStream inputStream) {
        return of(inputStream,null);
    }

    public static InputStreamContent of(InputStream inputStream, String filename) {
        return of(inputStream, filename, null);
    }

    public static InputStreamContent of(InputStream inputStream, String filename,String contentType) {
        return toFileBody(inputStream).fileName(filename).contentType(contentType);
    }

    private static InputStreamContent toFileBody(InputStream inputStream) {
        Assert.notNull(inputStream,"inputStream must be not null");
        try {
            InputStreamContent content = new InputStreamContent(inputStream);
            content.length = inputStream.available();
            return content;
        }catch (IOException ex) {
            throw new EasyHttpException(ex);
        }
    }

    public InputStreamContent contentType(String contentType) {
        if (contentType == null) {
            contentType = ContentType.STREAM;
        }
        this.contentType = contentType;
        return this;
    }

    public InputStreamContent fileName(String filename) {
        this.filename = filename;
        return this;
    }
    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public boolean isFile() {
        return true;
    }

    public Integer getLength() {
        return length;
    }

    public String getContentType() {
        return contentType;
    }
}
