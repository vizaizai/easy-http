package com.github.vizaizai.entity.body;

import com.github.vizaizai.entity.form.BodyContent;
import com.github.vizaizai.entity.form.FormBodyParts;
import com.github.vizaizai.exception.EasyHttpException;
import com.github.vizaizai.util.Assert;
import com.github.vizaizai.util.Utils;
import com.github.vizaizai.util.value.StringNameValues;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 请求体
 * @author liaochongwei
 * @date 2021/2/7 15:19
 */
public class RequestBody {
    /**
     * 请求体内容
     */
    private Body content;
    /**
     * 源对象
     */
    private final Object source;
    /**
     * 类型
     */
    private final RequestBodyType type;

    public RequestBody(Object source, RequestBodyType type) {
        this.source = source;
        this.type = type;
    }

    /**
     * 创建请求体
     * @param source 表单域
     * @param type 类型
     * @return RequestBody
     */
    public static RequestBody create(FormBodyParts source, RequestBodyType type) {
        return new RequestBody(source,type);
    }

    public static RequestBody create(BodyContent source, RequestBodyType type) {
        Assert.notNull(source,"The source must be not null");
        RequestBody requestBody = new RequestBody(source,type);
        try {
            requestBody.content = InputStreamBody.ofNullable(source.getInputStream(),null,false);
        }catch (IOException e) {
            throw new EasyHttpException(e);
        }
        return requestBody;
    }

    public static RequestBody create(String source, Charset encoding, RequestBodyType type) {
        RequestBody requestBody = new RequestBody(source,type);
        requestBody.content = ByteArrayBody.ofNullable(source, encoding);
        return requestBody;
    }

    public static RequestBody create(StringNameValues source, Charset encoding, RequestBodyType type) {
        RequestBody requestBody = new RequestBody(source,type);
        requestBody.content = ByteArrayBody.ofNullable(Utils.asUrlEncoded(source),encoding);
        return requestBody;
    }

    public static RequestBody create(Body content, Object source, RequestBodyType type) {
        RequestBody requestBody = new RequestBody(source,type);
        requestBody.content = content;
        return requestBody;
    }

    /**
     * 写入输出流
     */
    public void writeTo(OutputStream os, Charset charset) throws IOException {
        if (this.content != null) {
            this.content.writeTo(os);
            return;
        }
        // form-data
        if (source instanceof FormBodyParts) {
            FormBodyParts formBodyParts = (FormBodyParts) source;
            formBodyParts.writeTo(os, charset);
        }

    }

    public InputStream getInputStream(Charset charset) throws IOException {
        if (this.content != null) {
            return this.getContent().asInputStream();
        }
        if (source instanceof FormBodyParts) {
            FormBodyParts formBodyParts = (FormBodyParts) source;
            return new ByteArrayInputStream(formBodyParts.getBytes(charset));
        }
        return null;
    }

    public long length(Charset charset) {
        if (this.content != null) {
            return this.content.length();
        }
        // form-data
        if (source instanceof FormBodyParts) {
            FormBodyParts formBodyParts = (FormBodyParts) source;
            return formBodyParts.getLength(charset);
        }
        return 0;
    }
    public Body getContent() {
        return content;
    }

    public Object getSource() {
        return source;
    }

    public RequestBodyType getType() {
        return type;
    }
}
