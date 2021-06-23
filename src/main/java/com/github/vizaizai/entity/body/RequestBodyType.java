package com.github.vizaizai.entity.body;

import com.github.vizaizai.entity.ContentType;
import com.github.vizaizai.util.VUtils;

/**
 * 请求体类型
 * @author liaochongwei
 * @date 2021/2/7 15:49
 */
public enum RequestBodyType {
    /**
     * 无
     */
    NONE,
    /**
     * form-data：表单提交(可包含文件)
     */
    FORM_DATA,
    /**
     * x-www-form-urlencoded：默认表单提交
     */
    X_WWW_FROM_URL_ENCODED,
    /**
     * 普通文本(如JSON、XML)
     */
    RAW,
    /**
     * 二进制文件
     */
    BINARY,
    /**
     * 自动
     */
    AUTO,
    ;
    public boolean check(String contentType) {
        switch (this) {
            case NONE:
               return VUtils.isBlank(contentType);
            case FORM_DATA:
                return contentType != null && contentType.contains(ContentType.FORM_DATA);
            case X_WWW_FROM_URL_ENCODED:
                return contentType != null && contentType.contains(ContentType.APPLICATION_FORM_URLENCODED);
            default:
                return true;
        }
    }
}
