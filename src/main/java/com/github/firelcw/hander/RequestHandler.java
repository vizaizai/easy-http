package com.github.firelcw.hander;

import com.github.firelcw.annotation.Body;
import com.github.firelcw.annotation.Headers;
import com.github.firelcw.annotation.Query;
import com.github.firelcw.annotation.Var;
import com.github.firelcw.client.AbstractClient;
import com.github.firelcw.codec.Encoder;
import com.github.firelcw.model.HttpRequest;
import com.github.firelcw.model.HttpRequestConfig;
import com.github.firelcw.model.HttpResponse;
import com.github.firelcw.parser.ArgParser;
import com.github.firelcw.parser.MethodParser;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * 请求处理器
 * @author liaochongwei
 * @date 2020/7/30 17:11
 */
public class RequestHandler {
    /**
     * 请求基本路径
     */
    private String url;
    /**
     * 方法解析器
     */
    private MethodParser methodParser;
    /**
     * 参数解析器
     */
    private List<ArgParser> argParsers;
    /**
     * 编码器
     */
    private Encoder encoder;

    /**
     * 请求参数
     */
    private HttpRequest request;
    /**
     * 请求配置
     */
    private HttpRequestConfig config;
    /**
     * 请求客户端
     */
    private AbstractClient client;
    /**
     * 添加请求配置
     * @param config
     */
    public void client(AbstractClient client, HttpRequestConfig config) {
        this.config = config;
        this.client = client;
        client.setConfig(config);
    }

    private void initRequest() {
        // 校验方法参数
        this.checkArgs();
        // 处理http://
        this.handleUrl();
        this.request = new HttpRequest();
        // 设置请求方式
        this.request.setMethod(methodParser.getHttpMethod());
        // 设置ContentType
        this.request.setContentType(methodParser.getContentType());
        // 处理请求路径
        this.handlePath();
        // 参数解析列表不为空，则需要解析方法参数
        if (CollectionUtils.isNotEmpty(argParsers)) {
            // 处理请求query参数
            this.handleQuery();
            // 处理请求body参数
            this.handleBody();
            // 处理请求headers
            this.handleHeaders();

        }
    }

    public HttpResponse handle() {
        if (this.request == null) {
            this.initRequest();
        }
        return client.request(this.request);
    }


    /**
     * 处理path
     */
    private void handlePath() {
        String path = this.methodParser.getPath();
        for (ArgParser argParser : this.argParsers) {
            // var替换路径
            if (Var.TYPE.equals(argParser.getType()) && this.methodParser.getVarCount() > 0) {
                path = this.replacePath(path, argParser.getVarName(),argParser.getTarget().toString());
            }
        }
        this.request.setUrl(this.url + path);
    }

    /**
     * 处理query参数
     */
    private void handleQuery() {
        for (ArgParser argParser : this.argParsers) {
            if (Query.TYPE.equals(argParser.getType()) && !argParser.isSimple()) {
                this.request.addQueryParams(encoder.encodeMap(argParser.getTarget()));
                return;
            }
        }
    }

    /**
     * 处理查询body参数
     */
    private void handleBody() {
        for (ArgParser argParser : this.argParsers) {
            if (Body.TYPE.equals(argParser.getType()) && !argParser.isSimple()) {
                this.request.setBody(encoder.encodeString(argParser.getTarget()));
                return;
            }
        }
    }

    /**
     * 处理headers
     */
    private void handleHeaders() {
        for (ArgParser argParser : this.argParsers) {
            if (Headers.TYPE.equals(argParser.getType()) && !argParser.isSimple()) {
                this.request.addHeaders(encoder.encodeMap(argParser.getTarget()));
                return;
            }
        }
    }

    private void checkArgs() {

        if (this.url == null) {
            throw new IllegalArgumentException("The url is null");
        }
        if (this.methodParser == null) {
            throw new IllegalArgumentException("The method is null");
        }

        if (this.encoder == null) {
            throw new IllegalArgumentException("no default encoder");
        }
        // 1. 只能包含一个@Query
        // 2. 只能包含一个@Body
        // 3. 只能包含一个@Headers
        int has1 = 0;
        int has2 = 0;
        int has3 = 0;
        for (ArgParser argParser : this.argParsers) {
            if (Query.TYPE.equals(argParser.getType())) {
                has1 ++;
                if (has1 > 1) {
                    throw new IllegalArgumentException("Only one @Query can be included");
                }
            }
            if (Body.TYPE.equals(argParser.getType())) {
                has2 ++;
                if (has2 > 1) {
                    throw new IllegalArgumentException("Only one @Body can be included");
                }
            }
            if (Headers.TYPE.equals(argParser.getType())) {
                has3 ++;
                if (has3 > 1) {
                    throw new IllegalArgumentException("Only one @Headers can be included");
                }
            }
        }


    }
    private void handleUrl() {
        if (!url.startsWith( "http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
    }
    private String replacePath(String path, String varName, String varValue) {
        return path.replace("{" + varName + "}", varValue);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MethodParser getMethodParser() {
        return methodParser;
    }

    public void setMethodParser(MethodParser methodParser) {
        this.methodParser = methodParser;
    }

    public List<ArgParser> getArgParsers() {
        return argParsers;
    }

    public void setArgParsers(List<ArgParser> argParsers) {
        this.argParsers = argParsers;
    }

    public Encoder getEncoder() {
        return encoder;
    }

    public void setEncoder(Encoder encoder) {
        this.encoder = encoder;
    }

    public HttpRequest getRequest() {
        if (request == null ) {
            this.initRequest();
        }
        return request;
    }

    public HttpRequestConfig getConfig() {
        return config;
    }
}
