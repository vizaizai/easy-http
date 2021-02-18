package com.github.vizaizai.interceptor;


import com.github.vizaizai.entity.HttpRequest;
import com.github.vizaizai.entity.HttpResponse;
import com.github.vizaizai.entity.body.RequestBodyType;
import com.github.vizaizai.entity.form.BodyContent;
import com.github.vizaizai.entity.form.FormBodyParts;
import com.github.vizaizai.logging.LoggerFactory;
import com.github.vizaizai.util.StreamUtils;
import com.github.vizaizai.util.Utils;
import com.github.vizaizai.util.value.HeadersNameValues;
import com.github.vizaizai.util.value.NameValue;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;

/**
 * http日志拦截器
 * @author liaochongwei
 * @date 2020/7/31 14:06
 */

public class LogInterceptor implements HttpInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LogInterceptor.class);
    private static final String[] TEXT_TYPES = new String[] {
            "text/xml","application/xm", "text/plain","application/json"
    };
    @Override
    public boolean preHandle(HttpRequest request) {
        request.setStartTime(System.currentTimeMillis());
        if (!log.isInfoEnabled()) {
           return true;
        }

        String method = request.getMethod() == null ? "" : request.getMethod().name();
        log.info("请求行: {} {}",method, request.getUrl());
        if (CollectionUtils.isNotEmpty(request.getHeaders())) {
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
            log.info("请求头: {}", sb);
        }
        if (CollectionUtils.isNotEmpty(request.getParams())) {
            log.info("查询参数: {}", Utils.asUrlEncoded(request.getParams()));
        }
        this.printResponseBody(request);
        return true;
    }

    @Override
    public void postHandle(HttpRequest request, HttpResponse response) {
        long endTime = System.currentTimeMillis();
        if (!log.isInfoEnabled()) {
            return;
        }
        log.info("请求响应: {} [{}]:{} ",request.getUrl(), response.getStatusCode(), text(response.getMessage()));
        this.printResponseBody(response);
        log.info("耗时: {}ms", endTime - request.getStartTime());
    }

    private static String text(Object o) {
        return o == null ? "" : o.toString();
    }

    /**
     * 打印请求体
     */
    private void printResponseBody(HttpRequest request) {
        if (request.getBody() == null || RequestBodyType.NONE.equals(request.getBody().getType())) {
            return;
        }
        RequestBodyType type = request.getBody().getType();
        try {
            if (RequestBodyType.RAW.equals(type)) { // 文本
                if (request.getBody().getContent() == null) {
                    return;
                }
                log.info("请求体: {}", request.getBody().getContent().asString(request.getEncoding()));
                return;
            }
            if (RequestBodyType.X_WWW_FROM_URL_ENCODED.equals(type)) {
                if (request.getBody().getContent() == null) {
                    return;
                }
                log.info("请求体: x-www-form-urlencoded[{}]", request.getBody().getContent().asString(request.getEncoding()));
                return;
            }

            if (RequestBodyType.FORM_DATA.equals(type)) { // form-data
                if (!(request.getBody().getSource() instanceof FormBodyParts)) {
                    return;
                }
                FormBodyParts parts = (FormBodyParts) request.getBody().getSource();
                StringBuilder logText = new StringBuilder("请求体: form-data[");
                for (NameValue<String, BodyContent> nameValue : parts) {
                    BodyContent bodyContent = nameValue.getValue();
                    String name = nameValue.getName();
                    logText.append(name);
                    logText.append("=");
                    if (bodyContent.isFile()) {
                        logText.append("\"FILE");
                        if (bodyContent.getFilename() != null) {
                            logText.append("<");
                            logText.append(bodyContent.getFilename());
                            logText.append(">");
                        }
                        logText.append("\"");
                    }else {
                        logText.append(StreamUtils.copyToString(bodyContent.getInputStream(), request.getEncoding()));
                    }
                    logText.append(" ");
                }
                if (logText.substring(logText.length() - 1).equals(" ")) {
                    logText.deleteCharAt(logText.length() - 1);
                }
                logText.append("]");
                log.info(logText.toString());
                return;
            }
            if (RequestBodyType.BINARY.equals(type)) {
                log.info("请求体: binary");
            }
        }catch (Exception ignored) {}
    }

    /**
     * 打印响应体
     */
    private void printResponseBody(HttpResponse response) {
        if (response.getBody()== null || !response.getBody().isRepeatable()) {
            return;
        }
        HeadersNameValues headers = response.getHeaders();
        if (CollectionUtils.isEmpty(headers)) {
            return;
        }
        List<String> contentTypeHeaders = headers.getHeaders(Utils.CONTENT_TYPE);
        if (CollectionUtils.isEmpty(contentTypeHeaders)) {
            return;
        }
        String contentType = contentTypeHeaders.get(0);
        for (String textType : TEXT_TYPES) {
           if ( contentType!= null && contentType.contains(textType)) {
               try {
                   log.info("响应体: {}", text(response.getBody().asString(response.getEncoding())));
               }catch (Exception ignored) {
               }
               return;
           }
        }

    }

}
