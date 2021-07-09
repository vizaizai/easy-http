package com.github.vizaizai.parser;

import com.github.vizaizai.annotation.Mapping;
import com.github.vizaizai.entity.HttpMethod;
import com.github.vizaizai.entity.MappingInfo;
import com.github.vizaizai.entity.RetrySettings;
import com.github.vizaizai.entity.body.RequestBodyType;
import com.github.vizaizai.exception.EasyHttpException;
import com.github.vizaizai.hander.mapping.Mappings;
import com.github.vizaizai.hander.mapping.PathConverter;
import com.github.vizaizai.interceptor.DefaultInterceptorGenerator;
import com.github.vizaizai.interceptor.HttpInterceptor;
import com.github.vizaizai.interceptor.InterceptorGenerator;
import com.github.vizaizai.proxy.ProxyContext;
import com.github.vizaizai.util.TypeUtils;
import com.github.vizaizai.util.Utils;
import com.github.vizaizai.util.value.HeadersNameValues;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 方法解析
 * @author liaochongwei
 * @date 2020/7/30 15:24
 */
public class MethodParser {
    /**
     * 请求路径
     */
    private String path;
    /**
     * 请求方式
     */
    private HttpMethod httpMethod;
    /**
     * 参数变量个数
     */
    private Integer varCount;
    /**
     * 目标方法
     */
    private Method target;
    /**
     * contentType
     */
    private String contentType;
    /**
     * 请求体类型
     */
    private RequestBodyType bodyType;
    /**
     * 方法级别的headers
     */
    private HeadersNameValues headers;
    /**
     * 是否异步
     */
    private boolean async;
    /**
     * 方法上的拦截器
     */
    private List<HttpInterceptor> interceptors;
    /**
     * 重试设置
     */
    private RetrySettings retrySettings;


    public static MethodParser doParse(Method target, ProxyContext<?> proxyContext) {
        MethodParser methodParser = new MethodParser(target);
        methodParser.parse(proxyContext);
        return methodParser;
    }
    private MethodParser(Method target) {
        this.target = target;
    }
    private void parse(ProxyContext<?> proxyContext) {
        Annotation[] annotations = this.target.getAnnotations();

        List<Annotation> methodAnnotations = this.selectMethodAnnotations(annotations);
        if (methodAnnotations.isEmpty()) {
            throw new EasyHttpException("Request method must be not null");
        }
        // 一个请求只能指定一种请求方式
        if (methodAnnotations.size() > 1) {
            throw new EasyHttpException("Request method must be unique");
        }
        Annotation methodAnnotation = methodAnnotations.get(0);

        Class<? extends HttpInterceptor>[] interceptorClasses;

        // 解析映射注解上的参数
        MappingInfo mappingInfo = Mappings.getMappingInfo(methodAnnotation);
        this.path = mappingInfo.getPath(proxyContext.getPathConverter());
        this.contentType = mappingInfo.getContentType();
        this.httpMethod = mappingInfo.getHttpMethod();
        this.retrySettings = mappingInfo.getRetrySettings();
        this.bodyType = mappingInfo.getBodyType();
        interceptorClasses = mappingInfo.getInterceptors();

        // 添加拦截器
        this.addInterceptorsFromPath(interceptorClasses, proxyContext.getInterceptorGenerator());
        // 请求头注解
        this.headers = Utils.getHeaders(annotations);
        // 计算路径变量
        this.calVarCount(this.path);
        // 是否异步请求
        this.async = TypeUtils.isAsync(this.target.getGenericReturnType());
    }

    /**
     * 添加路径上的拦截器
     * @param classes classes
     */
    private void addInterceptorsFromPath(Class<? extends HttpInterceptor>[] classes, InterceptorGenerator interceptorGenerator){
        if (classes == null || classes.length == 0) {
            return;
        }
        if (interceptorGenerator == null) {
            interceptorGenerator = DefaultInterceptorGenerator.getGenerator();
        }

        for (Class<? extends HttpInterceptor> clazz : classes) {
            if (this.interceptors == null) {
                this.interceptors = new ArrayList<>();
            }
            this.interceptors.add(interceptorGenerator.get(clazz));
        }
    }
    /**
     * 选择请求方式注解
     * @param annotations
     */
    private List<Annotation> selectMethodAnnotations(Annotation[] annotations) {
        if (annotations == null || annotations.length == 0) {
            return Collections.emptyList();
        }
       return Stream.of(annotations)
                    .filter(this::isHttpMethodAnnotation)
                    .collect(Collectors.toList());
    }

    /**
     * 判断是否为http请求方式注解
     * @param annotation
     * @return boolean
     */
    private boolean isHttpMethodAnnotation(Annotation annotation) {
        String name = annotation.annotationType().getSimpleName();
        if (annotation instanceof  Mapping) {
            Mapping mapping = (Mapping) annotation;
            name = mapping.httpMethod().name();
        }
        HttpMethod[] values = HttpMethod.values();
        for (HttpMethod value : values) {
            if (value.name().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 计算路径变量个数
     * @param path
     */
    private void calVarCount(String path) {
        Pattern p = Pattern.compile("\\{\\w+}", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(path);
        int count = 0;
        while(m.find()){
            count ++;
        }
        this.varCount = count;

    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Integer getVarCount() {
        return varCount;
    }

    public void setVarCount(Integer varCount) {
        this.varCount = varCount;
    }

    public Method getTarget() {
        return target;
    }

    public void setTarget(Method target) {
        this.target = target;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public RequestBodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(RequestBodyType bodyType) {
        this.bodyType = bodyType;
    }

    public HeadersNameValues getHeaders() {
        return headers;
    }

    public boolean isAsync() {
        return async;
    }

    public List<HttpInterceptor> getInterceptors() {
        return interceptors;
    }

    public RetrySettings getRetrySettings() {
        return retrySettings;
    }
}
