package com.github.vizaizai.hander;

import com.github.vizaizai.annotation.Body;
import com.github.vizaizai.annotation.Headers;
import com.github.vizaizai.annotation.Param;
import com.github.vizaizai.annotation.Var;
import com.github.vizaizai.client.AbstractClient;
import com.github.vizaizai.codec.Encoder;
import com.github.vizaizai.entity.*;
import com.github.vizaizai.entity.body.RequestBody;
import com.github.vizaizai.entity.body.RequestBodyType;
import com.github.vizaizai.entity.form.BodyContent;
import com.github.vizaizai.entity.form.FormBodyParts;
import com.github.vizaizai.entity.form.FormData;
import com.github.vizaizai.interceptor.InterceptorExecutor;
import com.github.vizaizai.parser.Arg;
import com.github.vizaizai.parser.ArgsParser;
import com.github.vizaizai.parser.InterfaceParser;
import com.github.vizaizai.parser.MethodParser;
import com.github.vizaizai.proxy.ProxyContext;
import com.github.vizaizai.util.Assert;
import com.github.vizaizai.util.TypeUtils;
import com.github.vizaizai.util.Utils;
import com.github.vizaizai.util.VUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 请求处理器
 * @author liaochongwei
 * @date 2020/7/30 17:11
 */
public class RequestHandler implements Handler<HttpResponse>{
    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";
    /**
     * 请求基本路径
     */
    private String url;
    /**
     * 接口解析器
     */
    private InterfaceParser interfaceParser;
    /**
     * 方法解析器
     */
    private MethodParser methodParser;
    /**
     * 参数解析器
     */
    private ArgsParser argsParser;
    /**
     * 编码器
     */
    private Encoder encoder;
    /**
     * 请求参数
     */
    private HttpRequest request;
    /**
     * 拦截执行器
     */
    private InterceptorExecutor interceptorExecutor;
    /**
     * 请求配置
     */
    private HttpRequestConfig config;
    /**
     * 请求客户端
     */
    private AbstractClient client;
    /**
     * 重试设置
     */
    private RetrySettings retrySettings;

    /**
     * 创建RequestHandler
     * @param proxyContext
     * @return RequestHandler
     */
    public static RequestHandler create(ProxyContext<?> proxyContext, Method method, Object[] args) {
        // 接口解析
        InterfaceParser interfaceParser = InterfaceParser.doParse(proxyContext.getTargetClazz());
        // 方法解析
        MethodParser methodParser = MethodParser.doParse(method, proxyContext);
        // 参数解析
        List<Arg> argList = new LinkedList<>();
        for (int i = 0; args != null && i < args.length; i++) {
            argList.add(Arg.instance(args[i],method, i));
        }
        ArgsParser argsParser = ArgsParser.doParse(argList);

        RequestHandler handler = new RequestHandler();
        handler.url = proxyContext.getUrl();
        handler.encoder = proxyContext.getEncoder();
        handler.client(proxyContext.getClient(),proxyContext.getRequestConfig());

        handler.interfaceParser = interfaceParser;
        handler.argsParser = argsParser;
        handler.methodParser = methodParser;
        handler.retrySettings = proxyContext.getRetrySettings();

        // 拦截执行器
        handler.interceptorExecutor = InterceptorExecutor.create(proxyContext.getInterceptors());
        // 添加方法级别的拦截器
        handler.interceptorExecutor.addInterceptors(methodParser.getInterceptors());

        // 初始化请求
        handler.initRequest();
        return handler;
    }

    /**
     * 添加请求配置
     * @param config
     */
    private void client(AbstractClient client, HttpRequestConfig config) {
        this.config = config;
        this.client = client;
    }

    private void initRequest() {
        // 处理http://
        this.handleUrl();

        this.request = new HttpRequest();

        // 设置配置
        this.request.setConfig(this.config);

        // 字符编码
        this.request.setEncoding(this.config.getEncoding());

        // 是否异步
        this.request.setAsync(this.getMethodParser().isAsync());

        // 设置请求方式
        this.request.setMethod(methodParser.getHttpMethod());

        // 处理请求体类型
        RequestBodyType bodyType = this.handleBodyType();

        // 处理请求路径
        this.handlePath();

        // 处理请求headers
        this.handleHeaders();

        // 处理重试设置
        this.handleRetry();

        // 参数解析列表不为空，则需要解析方法参数
        if (!argsParser.isEmpty()) {
            // 处理请求query参数
            this.handleParam();
            // 处理请求body参数
            this.handleBody(bodyType);

        }
    }

    @Override
    public HttpResponse execute() throws IOException {
        // 执行过滤
        this.doInterceptor();
        client.setConfig(this.request.getConfig());
        return client.request(this.request);
    }
    /**
     * 执行过滤器
     */
    private void doInterceptor() {
        // 排除
        interceptorExecutor.exclude(this.request.getUrl(), this.request.getMethod());
        // 排序
        interceptorExecutor.ordered();
        // 执行前置过滤器
        interceptorExecutor.doPreInterceptors(this.request);
    }
    /**
     * 处理path
     */
    private void handlePath() {
        String path = this.methodParser.getPath();
        Map<String,String> pathParams = new HashMap<>();
        List<Arg> args = argsParser.getArgs(Var.TYPE);
        for (Arg arg : args) {
            // var替换路径
            if (this.methodParser.getVarCount() > 0) {
                String key = Utils.urlEncode(arg.getVarName(), this.request.getEncoding().name());
                String value = Utils.urlEncode(Utils.toText(arg.getSource()),this.request.getEncoding().name());
                pathParams.put(key,value);
            }
        }
        String formatPath = Utils.formatPlaceholder(path, pathParams);
        if(VUtils.isNotBlank(formatPath) && (formatPath.startsWith(HTTP) || formatPath.startsWith(HTTPS))) {
            this.request.setUrl(formatPath);
            return;
        }
        this.request.setUrl(this.url + formatPath);
    }

    /**
     * 处理Param
     */
    private void handleParam() {
        List<Arg> args = argsParser.getArgs(Param.TYPE);
        for (Arg arg : args) {
            if (arg.isBaseType() && VUtils.isBlank(arg.getVarName())) {
                throw new IllegalArgumentException("The value of @Param is empty");
            }
            request.addParams(Utils.encodeNameValue(arg.getVarName(),arg.getSource(),arg.getDataType()));
        }
    }

    /**
     * 处理body参数
     */
    private void handleBody(RequestBodyType type) {
        List<Arg> args = argsParser.getArgs(Body.TYPE);
        Charset charset = request.getEncoding();
        Arg arg = args.isEmpty() ? null : args.get(0);
        switch (type) {
            case RAW:
                Assert.notNull(arg);
                // 存在warpRoot
                if (VUtils.isNotBlank(arg.getVarName())) {
                    Map<String,Object> wrap = new HashMap<>(1);
                    wrap.put(arg.getVarName(), arg.getSource());
                    this.request.setBody(RequestBody.create(encoder.encode(wrap,arg.getDataType()), arg.getSource(),type));
                    break;
                }
                if (arg.isBaseType()) {
                    this.request.setBody(RequestBody.create(Utils.toString(arg.getSource()),charset,type));
                    break;
                }
                request.setBody(RequestBody.create(encoder.encode(arg.getSource(),arg.getDataType()),arg.getSource(),type));
                break;
            case X_WWW_FROM_URL_ENCODED:
                request.setBody(RequestBody.create(this.request.getParams(),charset,type));
                request.setEmptyParams();
                break;
            case FORM_DATA:
                Assert.notNull(arg);
                FormData formData = (FormData) arg.getSource();
                FormBodyParts parts = formData == null ? new FormBodyParts() : formData.getFormBodyParts();
                request.setContentType(Utils.getMultiContentType(parts.getBoundary()));
                request.setBody(RequestBody.create(parts,type));
                break;
            case BINARY:
                Assert.notNull(arg);
                BodyContent content = (BodyContent) arg.getSource();
                request.setBody(RequestBody.create(content,type));
                break;
            default:
                break;
        }
    }

    /**
     * 处理headers
     */
    private void handleHeaders() {
        // 添加接口级别的headers
        this.request.addHeaders(this.interfaceParser.getHeaders());

        // 添加方法级别的headers
        this.request.addHeaders(this.methodParser.getHeaders());

        List<Arg> args = argsParser.getArgs(Headers.TYPE);
        // 参数级别的headers
        for (Arg arg : args) {
            if (!arg.isBaseType()) {
                this.request.addHeaders(Utils.encodeNameValue(arg.getVarName(),arg.getSource(),arg.getDataType()));
            }
        }
    }

    /**
     * 处理重试设置
     */
    private void handleRetry() {
        // 方法上的设置
        RetrySettings methodSettings = this.methodParser.getRetrySettings();
        if (methodSettings == null) {
            return;
        }
        // 全局默认设置
        if (this.retrySettings == null) {
            this.retrySettings = new RetrySettings();
        }
        // 方法上的重试设置优先级更高
        if (methodSettings.getEnable() != null) {
            this.retrySettings.setEnable(methodSettings.getEnable());
        }

        if (methodSettings.getMaxAttempts() != null && methodSettings.getMaxAttempts() > 0) {
            this.retrySettings.setMaxAttempts(methodSettings.getMaxAttempts());
        }

        if (methodSettings.getIntervalTime() != null && methodSettings.getIntervalTime() > -1) {
            this.retrySettings.setIntervalTime(methodSettings.getIntervalTime());
        }

    }

    private void handleUrl() {
        if (this.url == null) {
            this.url = "";
        }
        if (VUtils.isNotBlank(url) &&
                !url.startsWith(HTTP) && !url.startsWith(HTTPS)) {
            url = HTTP + url;
        }
    }


    /**
     * 处理请求体类型
     */
    private RequestBodyType handleBodyType() {
        // 设置ContentType
        request.setContentType(methodParser.getContentType());
        RequestBodyType bodyType = methodParser.getBodyType();
        if ( bodyType == null || RequestBodyType.AUTO.equals(bodyType)) {
            // 分析请求体类型
            bodyType =  this.analysisBodyType();
        }
        // 重设contentType
        if (VUtils.isBlank(request.getContentType())) {
            this.setContentType(bodyType);
        }
        // 校验bodyType
        Assert.isTrue(bodyType.check(request.getContentType()),"The body's type is error.");
        return bodyType;

    }
    /**
     * 动态分析请求体类型
     */
    private RequestBodyType analysisBodyType() {
        // 优先根据contentType判断bodyType
        String contentType = methodParser.getContentType();
        if (VUtils.isNotBlank(contentType)) {
            if (contentType.contains(ContentType.APPLICATION_FORM_URLENCODED)) {
                // form-urlEncode
                return RequestBodyType.X_WWW_FROM_URL_ENCODED;
            }
            if (contentType.contains(ContentType.FORM_DATA)) {
                // form-data
                return RequestBodyType.FORM_DATA;
            }
            if (contentType.contains(ContentType.APPLICATION_JSON)
                    || contentType.contains(ContentType.APPLICATION_XML)
                    || contentType.contains(ContentType.TEXT_XML)) {
                // 文本
               return RequestBodyType.RAW;
            }
        }
        int paramCount = argsParser.getCount(Param.TYPE);
        int bodyCount = argsParser.getCount(Body.TYPE);
        // 只存在@Param，GET请求bodyType为NONE，其它请求bodyType为X_WWW_FROM_URL_ENCODED
        if (paramCount > 0 && bodyCount == 0) {
            if (request.getMethod().equals(HttpMethod.GET)) {
                return RequestBodyType.NONE;
            }
            return RequestBodyType.X_WWW_FROM_URL_ENCODED;
        }
        if (bodyCount == 0) {
            return RequestBodyType.NONE;
        }
        return this.getBodyTypeFromSource(argsParser.getArgs(Body.TYPE).get(0));
    }

    /**
     * 根据源对象类型获取bodyType
     * @param source body参数对象
     */
    private RequestBodyType getBodyTypeFromSource(Arg source) {
        Type sourceDataType = source.getDataType();
        RequestBodyType bodyType;
        if (TypeUtils.equals(FormData.class, sourceDataType)) {
            // FormData-> form-data
            bodyType = RequestBodyType.FORM_DATA;
        }else if (sourceDataType instanceof Class &&  TypeUtils.isThisType(BodyContent.class, (Class<?>) sourceDataType)) {
            // 文件->二进制
            bodyType = RequestBodyType.BINARY;
        }else {
            // 文本
            bodyType = RequestBodyType.RAW;
        }
        return bodyType;
    }
    /**
     * 设置contentType
     * @param bodyType
     */
    private void setContentType(RequestBodyType bodyType) {
        switch (bodyType) {
            case X_WWW_FROM_URL_ENCODED:
                request.setContentType(ContentType.APPLICATION_FORM_URLENCODED);
                break;
            case RAW:
                request.setContentType(ContentType.APPLICATION_JSON);
                break;
            case FORM_DATA:
                request.setContentType(ContentType.FORM_DATA);
                break;
            case BINARY:
                List<Arg> args = argsParser.getArgs(Body.TYPE);
                if (VUtils.isEmpty(args)) {
                    request.setContentType(ContentType.STREAM);
                    break;
                }
                BodyContent bodyContent = (BodyContent) args.get(0).getSource();
                if (bodyContent == null) {
                    request.setContentType(ContentType.STREAM);
                    break;
                }
                request.setContentType(bodyContent.getContentType());
                break;
            default:
                break;
        }
    }

    public String getUrl() {
        return url;
    }

    public MethodParser getMethodParser() {
        return methodParser;
    }

    public ArgsParser getArgsParser() {
        return argsParser;
    }

    public Encoder getEncoder() {
        return encoder;
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

    public InterfaceParser getInterfaceParser() {
        return interfaceParser;
    }

    public AbstractClient getClient() {
        return client;
    }

    public InterceptorExecutor getInterceptorExecutor() {
        return interceptorExecutor;
    }

    public RetrySettings getRetrySettings() {
        return retrySettings;
    }
}
