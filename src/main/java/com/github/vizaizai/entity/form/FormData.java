package com.github.vizaizai.entity.form;

import com.github.vizaizai.util.Utils;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * form-data
 * @author liaochongwei
 * @date 2021/3/26 17:22
 */
public class FormData {
    private final FormBodyParts formBodyParts;

    public FormData() {
        this.formBodyParts = new FormBodyParts();
    }

    public void add(String name, BodyContent content) {
        formBodyParts.add(name, content);
    }

    public void addText(String name, String text) {
        addText(name, text, Utils.UTF_8);
    }
    public void addText(String name, String text, Charset charset) {
        formBodyParts.add(name,StringContent.of(text, charset));
    }

    public void addInputStream(String name, InputStream inputStream) {
        addInputStream(name, inputStream, null, null);
    }

    public void addInputStream(String name, InputStream inputStream, String filename) {
        addInputStream(name, inputStream, filename, null);
    }

    public void addInputStream(String name, InputStream inputStream, String filename, String contentType) {
        formBodyParts.add(name, InputStreamContent.of(inputStream, filename, contentType));
    }

    public void addFile(String name, File file) {
        addFile(name, file, null);
    }

    public void addFile(String name, File file, String contentType) {
        formBodyParts.add(name, FileContent.of(file, contentType));
    }

    public FormBodyParts getFormBodyParts() {
        return formBodyParts;
    }
}
