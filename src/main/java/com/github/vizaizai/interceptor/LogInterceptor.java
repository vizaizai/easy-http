package com.github.vizaizai.interceptor;


import com.github.vizaizai.entity.HttpRequest;
import com.github.vizaizai.entity.HttpResponse;
import com.github.vizaizai.entity.body.RequestBodyType;
import com.github.vizaizai.entity.form.BodyContent;
import com.github.vizaizai.entity.form.FormBodyParts;
import com.github.vizaizai.logging.LoggerFactory;
import com.github.vizaizai.util.StreamUtils;
import com.github.vizaizai.util.Utils;
import com.github.vizaizai.util.VUtils;
import com.github.vizaizai.util.value.HeadersNameValues;
import com.github.vizaizai.util.value.NameValue;
import org.slf4j.Logger;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

/**
 * http日志拦截器
 * @author liaochongwei
 * @date 2020/7/31 14:06
 */

public class LogInterceptor implements HttpInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LogInterceptor.class);
    private static final String[] TEXT_TYPES = new String[] { "html","xml", "text","json"};
    private final String itemPrefix;
    private final String itemSuffix;
    private final static String REQ_SEQ = "> ";
    private final static String RESP_SEQ = "< ";

    public LogInterceptor() {
        this.itemPrefix =  System.lineSeparator();
        this.itemSuffix = "";
    }

    public LogInterceptor(String itemPrefix, String itemSuffix) {
        this.itemPrefix = itemPrefix;
        this.itemSuffix = itemSuffix;
    }

    @Override
    public boolean preHandle(HttpRequest request) {
        request.setStartTime(System.currentTimeMillis());
        if (!log.isInfoEnabled()) {
           return true;
        }

        String method = request.getMethod() == null ? "" : request.getMethod().name();

        StringBuilder logText = new StringBuilder();
        // 请求行
        logText.append(itemPrefix);
        logText.append(REQ_SEQ);
        logText.append(MessageFormat.format("请求行: {0} {1}",method, request.getUrl()));
        logText.append(itemSuffix);

        // 请求头
        if (VUtils.isNotEmpty(request.getHeaders())) {
            StringBuilder sb = new StringBuilder();
            HeadersNameValues headers = request.getHeaders();
            Set<String> names = headers.names();
            for (String name : names) {
                sb.append(name).append("=");
                for (String value : headers.getHeaders(name)) {
                    sb.append(value).append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append(" ");
            }
            logText.append(itemPrefix);
            logText.append(REQ_SEQ);
            logText.append(MessageFormat.format("请求头: {0}", sb.toString()));
            logText.append(itemSuffix);
        }
        // 查询参数
        if (VUtils.isNotEmpty(request.getParams())) {
            logText.append(itemPrefix);
            logText.append(REQ_SEQ);
            logText.append(MessageFormat.format("查询参数: {0}", Utils.asUrlEncoded(request.getParams())));
            logText.append(itemSuffix);
        }
        // 请求体
        this.printResponseBody(request,logText);
        log.info(Utils.unicodeToString(logText.toString()));
        return true;
    }

    @Override
    public void postHandle(HttpRequest request, HttpResponse response) {
        long endTime = System.currentTimeMillis();
        if (!log.isInfoEnabled()) {
            return;
        }
        StringBuilder logText = new StringBuilder();
        // 响应头
        logText.append(itemPrefix);
        logText.append(RESP_SEQ);
        logText.append(MessageFormat.format("请求响应: {0} [{1}]:{2} ",request.getUrl(), response.getStatusCode(), text(response.getMessage())));
        logText.append(itemSuffix);

        // 响应体
        this.printResponseBody(response, logText);

        // 耗时
        logText.append(itemPrefix);
        logText.append(RESP_SEQ);
        logText.append(MessageFormat.format("耗时: {0}ms", endTime - request.getStartTime()));
        logText.append(itemSuffix);
        log.info(Utils.unicodeToString(logText.toString()));
    }

    private static String text(Object o) {
        return o == null ? "" : o.toString();
    }

    /**
     * 打印请求体
     */
    private void printResponseBody(HttpRequest request,StringBuilder logText) {
        if (request.getBody() == null || RequestBodyType.NONE.equals(request.getBody().getType())) {
            return;
        }
        RequestBodyType type = request.getBody().getType();
        try {
            if (RequestBodyType.RAW.equals(type)) { // 文本
                if (request.getBody().getContent() == null) {
                    return;
                }
                logText.append(itemPrefix);
                logText.append(REQ_SEQ);
                logText.append(MessageFormat.format("请求体: {0}", request.getBody().getContent().asString(request.getEncoding())));
                logText.append(itemSuffix);
                return;
            }
            if (RequestBodyType.X_WWW_FROM_URL_ENCODED.equals(type)) {
                if (request.getBody().getContent() == null) {
                    return;
                }
                logText.append(itemPrefix);
                logText.append(REQ_SEQ);
                logText.append(MessageFormat.format("请求体: x-www-form-urlencoded[{0}]", request.getBody().getContent().asString(request.getEncoding())));
                logText.append(itemSuffix);
                return;
            }

            if (RequestBodyType.FORM_DATA.equals(type)) { // form-data
                if (!(request.getBody().getSource() instanceof FormBodyParts)) {
                    return;
                }
                FormBodyParts parts = (FormBodyParts) request.getBody().getSource();
                logText.append(itemPrefix);
                logText.append(REQ_SEQ);
                logText.append("请求体: form-data[");
                for (NameValue<String, BodyContent> nameValue : parts) {
                    BodyContent bodyContent = nameValue.getValue();
                    String name = nameValue.getName();
                    logText.append(name);
                    if (bodyContent.isFile()) {
                        logText.append("(File)");
                        logText.append("=");
                        if (bodyContent.getFilename() != null) {
                            logText.append(bodyContent.getFilename());
                        }
                    }else {
                        logText.append("=");
                        logText.append(StreamUtils.copyToString(bodyContent.getInputStream(), request.getEncoding()));
                    }
                    logText.append(",");
                }
                if (logText.substring(logText.length() - 1).equals(",")) {
                    logText.deleteCharAt(logText.length() - 1);
                }
                logText.append("]");
                logText.append(MessageFormat.format(", 大小: {0}字节", parts.getLength(request.getEncoding())));
                logText.append(itemSuffix);
                return;
            }
            if (RequestBodyType.BINARY.equals(type)) {
                logText.append(itemPrefix);
                logText.append(REQ_SEQ);
                logText.append(MessageFormat.format("请求体: {0}字节", request.getBody().length(request.getEncoding())));
                logText.append(itemSuffix);
            }
        }catch (Exception ignored) {}
    }

    /**
     * 打印响应体
     */
    private void printResponseBody(HttpResponse response,StringBuilder logText) {
        if (response.getBody()== null || !response.getBody().isRepeatable()) {
            return;
        }
        HeadersNameValues headers = response.getHeaders();
        if (VUtils.isEmpty(headers)) {
            return;
        }
        List<String> contentTypeHeaders = headers.getHeaders(Utils.CONTENT_TYPE);
        if (VUtils.isEmpty(contentTypeHeaders)) {
            return;
        }
        String contentType = contentTypeHeaders.get(0);
        for (String textType : TEXT_TYPES) {
           if (contentType!= null && contentType.toLowerCase().contains(textType)) {
               try {
                   logText.append(itemPrefix);
                   logText.append(RESP_SEQ);
                   logText.append(MessageFormat.format("响应体: {0}", text(response.getBody().asString(response.getEncoding()))));
                   logText.append(itemSuffix);
               }catch (Exception ignored) {
               }
               return;
           }
        }

    }

}
