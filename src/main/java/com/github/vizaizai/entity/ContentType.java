package com.github.vizaizai.entity;

/**
 * @author liaochongwei
 * @date 2020/7/31 11:00
 */
public class ContentType {

    private ContentType() {
    }

    /* 表单相关 */
    public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
    public static final String FORM_DATA = "multipart/form-data";

    /*文本*/
    public static final String APPLICATION_JSON = "application/json";
    public static final String TEXT_XML = "text/xml";
    public static final String APPLICATION_XML = "application/xml";

    public static final String TEXT_HTML = "text/html";
    public static final String TEXT_PLAIN = "text/plain";

    /*流*/
    public static final String STREAM = "application/octet-stream";


}
