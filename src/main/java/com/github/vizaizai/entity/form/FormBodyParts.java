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
    private final FormDataEncoder encoder;
    public FormBodyParts() {
        boundary = Utils.generateBoundary();
        encoder = new FormDataEncoder();
    }

    public void add(String name, BodyContent value) {
        this.add(new FormDataNameValue(name, value));
    }
    public void writeTo(OutputStream os, Charset charset) throws IOException {
        encoder.encode(this, os, charset);

    }

    public String getBoundary() {
        return boundary;
    }
    public long getLength() {
        return encoder.getLength();
    }
}
