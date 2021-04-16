package com.github.vizaizai.entity.form;

import com.github.vizaizai.util.Utils;
import com.github.vizaizai.util.value.NameValues;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 用于form-data表单提交
 * @author liaochongwei
 * @date 2021/2/5 11:16
 */
public class FormBodyParts extends NameValues<String, BodyContent> {
    private final String boundary;
    private FormDataEncoder encoder;
    public FormBodyParts() {
        boundary = Utils.generateBoundary();
    }

    public void add(String name, BodyContent value) {
        this.add(new FormDataNameValue(name, value));
    }
    public void writeTo(OutputStream os, Charset charset) throws IOException {
        this.checkEncoder(charset);
        encoder.encode(os);
    }

    public String getBoundary() {
        return boundary;
    }
    public long getLength(Charset charset) {
        this.checkEncoder(charset);
        return encoder.getLength();
    }
    public byte[] getBytes(Charset charset) {
        this.checkEncoder(charset);
        return encoder.getData();
    }

    private void checkEncoder(Charset charset) {
        if (encoder == null) {
            encoder = new FormDataEncoder(this.boundary, charset, this.getNameValues());
        }
    }
    public static FormBodyParts cast(Object object) {
        if (object == null) {
            return new FormBodyParts();
        }
        return (FormBodyParts) object;
    }
}
