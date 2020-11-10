package com.github.firelcw.parser;

import com.github.firelcw.annotation.*;
import com.github.firelcw.exception.EasyHttpException;
import com.github.firelcw.model.HttpMethod;
import com.github.firelcw.util.Utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
     * 方法级别的headers
     */
    private Map<String,String> headers;

    public MethodParser() {
    }

    public MethodParser(Method target) {
        this.target = target;
        this.parse();
    }
    public void parse(Method method) {
        this.target = method;
        this.parse();
    }
    private void parse() {
        Annotation[] annotations = this.target.getAnnotations();

        List<Annotation> methodAnnotations = this.selectMethodAnnotations(annotations);
        // 一个请求只能指定一种请求方式
        if (methodAnnotations.size() != 1) {
            throw new EasyHttpException("A request can specify only one request method");
        }
        Annotation methodAnnotation = methodAnnotations.get(0);
        if (methodAnnotation instanceof  Mapping) {
            Mapping mapping = ((Mapping) methodAnnotation);
            this.path = mapping.value();
            this.contentType = mapping.contentType();
            this.httpMethod = mapping.httpMethod();

        }else if (methodAnnotation instanceof Get) {
            this.path = ((Get) methodAnnotation).value();
            this.httpMethod = HttpMethod.GET;

        }else if (methodAnnotation instanceof Post) {
            Post post = ((Post) methodAnnotation);
            this.path = post.value();
            this.contentType = post.contentType();
            this.httpMethod = HttpMethod.POST;

        }else if (methodAnnotation instanceof Put) {
            Put put = ((Put) methodAnnotation);
            this.path = put.value();
            this.contentType = put.contentType();
            this.httpMethod = HttpMethod.PUT;

        }else {
            Delete delete = ((Delete) methodAnnotation);
            this.path = ((Delete) methodAnnotation).value();
            this.contentType = delete.contentType();
            this.httpMethod = HttpMethod.DELETE;
        }
        // 请求头注解
        this.headers = Utils.getHeaders(annotations);
        // 计算路径变量
        this.calVarCount(this.path);

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

    public Map<String, String> getHeaders() {
        return headers;
    }
}
