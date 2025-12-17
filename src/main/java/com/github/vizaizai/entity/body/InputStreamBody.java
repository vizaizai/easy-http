package com.github.vizaizai.entity.body;

import com.github.vizaizai.exception.EasyHttpException;
import com.github.vizaizai.util.Assert;
import com.github.vizaizai.util.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author liaochongwei
 * @date 2021/1/11 14:57
 */
public class InputStreamBody implements Body {
    private static final Logger log = LoggerFactory.getLogger(InputStreamBody.class);
    private final InputStream source;
    private long length = -1;
    private byte[] copyBytes;
    private final boolean repeatable;

    private InputStreamBody(InputStream inputStream, long length, boolean repeatable) {
        this.source = inputStream;
        this.length = length;
        this.repeatable = repeatable;
        try {
            // 支持重复读
            if (this.repeatable) {
                this.copyBytes = StreamUtils.copyToByteArray(inputStream);
            }

            // 长度
            if (length == -1) {
                 try {
                     this.length = inputStream.available();
                 }catch (Exception e) {
                     if (log.isDebugEnabled()) {
                         log.debug("read length fail: {}", e.getMessage());
                     }
                 }
            }

        }catch (IOException e) {
            throw new EasyHttpException(e);
        }

    }
    public static Body ofNullable(InputStream inputStream, long length) {
        if (inputStream == null) {
            return null;
        }
        return new InputStreamBody(inputStream, length,true);
    }
    public static Body ofNullable(InputStream inputStream, long length, boolean repeatable) {
        if (inputStream == null) {
            return null;
        }
        return new InputStreamBody(inputStream, length, repeatable);
    }

    @Override
    public long length() {
        return length;
    }

    @Override
    public boolean isRepeatable() {
        return this.repeatable;
    }

    @Override
    public InputStream asInputStream() {
        if (!this.repeatable) {
            return this.source;
        }
        final ByteArrayInputStream bais = new ByteArrayInputStream(copyBytes);
        return new InputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }

    @Override
    public Reader asReader(Charset charset) throws IOException {
        Assert.notNull(charset, "charset should not be null");
        return new InputStreamReader(this.asInputStream(), charset);
    }

    @Override
    public String asString(Charset charset) throws IOException {
        return StreamUtils.copyToString(this.asInputStream(),charset);
    }

    @Override
    public void writeTo(OutputStream os) throws IOException {
        StreamUtils.copy(source, os);
    }
}
