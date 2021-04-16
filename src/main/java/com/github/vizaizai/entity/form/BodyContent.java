package com.github.vizaizai.entity.form;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author liaochongwei
 * @date 2021/2/4 17:52
 */
public interface BodyContent {

    String getFilename();
    String getContentType();
    InputStream getInputStream() throws IOException;
    boolean isFile();
}
