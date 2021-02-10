package com.github.vizaizai.entity.form;

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

    public static InputStreamContent of(InputStream inputStream, String filename) {
        Assert.notNull(inputStream,"file must be not null");
        Assert.notNull(filename,"filename must be not null");
        return toFileBody(inputStream).fileName(filename);
    }

    public static InputStreamContent of(InputStream inputStream, String contentType, String filename) {
        Assert.notNull(contentType,"contentType must be not null");
        return of(inputStream,filename).contentType(contentType);
    }

    private static InputStreamContent toFileBody(InputStream inputStream) {
        try {
            InputStreamContent content = new InputStreamContent(inputStream);
            content.length = inputStream.available();
            return content;
        }catch (IOException ex) {
            throw new EasyHttpException(ex);
        }
    }

    public InputStreamContent contentType(String contentType) {
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
