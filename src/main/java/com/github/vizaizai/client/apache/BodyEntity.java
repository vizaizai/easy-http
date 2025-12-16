package com.github.vizaizai.client.apache;

import com.github.vizaizai.entity.body.RequestBody;
import org.apache.hc.core5.http.io.entity.AbstractHttpEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @author liaochongwei
 * @date 2021/2/18 16:32
 */
public class BodyEntity extends AbstractHttpEntity {
    private final RequestBody requestBody;
    private final Charset charset;

    public BodyEntity(RequestBody requestBody, Charset charset, String contentType) {
        super(contentType, charset.name(), false);
        this.requestBody = requestBody;
        this.charset = charset;
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public long getContentLength() {
        return requestBody.length(this.charset);
    }

    @Override
    public InputStream getContent() throws IOException {
        return requestBody.getInputStream(charset);
    }

    @Override
    public void writeTo(OutputStream outStream) throws IOException {
        requestBody.writeTo(outStream, charset);
    }

    @Override
    public boolean isStreaming() {
        return false;
    }

    @Override
    public void close() throws IOException {

    }
}
