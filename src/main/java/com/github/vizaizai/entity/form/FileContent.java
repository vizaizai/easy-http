package com.github.vizaizai.entity.form;

import com.github.vizaizai.entity.ContentType;
import com.github.vizaizai.exception.EasyHttpException;
import com.github.vizaizai.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件请求体内容
 * @author liaochongwei
 * @date 2021/2/5 16:15
 */
public class FileContent implements BodyContent {
    private InputStream inputStream;
    private final File file;
    private Integer length;
    private String contentType;
    private String filename;

    private FileContent(File file) {
        this.file = file;
    }

    public static FileContent of(File file) {
        return of(file,null);
    }

    public static FileContent of(File file, String contentType) {
        return toFileBody(file).fileName(file.getName()).contentType(contentType);
    }

    private static FileContent toFileBody(File file) {
        Assert.notNull(file,"file must be not null");
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            FileContent content = new FileContent(file);
            content.inputStream = inputStream;
            content.length = inputStream.available();
            return content;
        }catch (IOException ex) {
            throw new EasyHttpException(ex);
        }
    }


    public FileContent contentType(String contentType) {
        if (contentType == null) {
            contentType = ContentType.STREAM;
        }
        this.contentType = contentType;
        return this;
    }

    public FileContent fileName(String filename) {
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

    public File getFile() {
        return file;
    }

    public Integer getLength() {
        return length;
    }

    @Override
    public String getContentType() {
        return contentType;
    }
}
