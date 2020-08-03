package com.github.firelcw.parser;

import com.github.firelcw.annotation.Delete;
import com.github.firelcw.annotation.Get;
import com.github.firelcw.annotation.Post;
import com.github.firelcw.annotation.Put;
import com.github.firelcw.exception.EasyHttpException;
import com.github.firelcw.model.HttpMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Annotation[] methodAnnotations = this.target.getAnnotations();
        if (methodAnnotations.length != 1) {
            throw new EasyHttpException("The number of annotations for a method can only be 1");
        }
        Annotation methodAnnotation = methodAnnotations[0];
        if (methodAnnotations[0] instanceof Get) {
            this.path = ((Get) methodAnnotation).value();
            this.httpMethod = HttpMethod.GET;

        }else if (methodAnnotations[0] instanceof Post) {
            Post post = ((Post) methodAnnotation);
            this.path = post.value();
            this.contentType = post.contentType();
            this.httpMethod = HttpMethod.POST;

        }else if (methodAnnotations[0] instanceof Put) {
            Put put = ((Put) methodAnnotation);
            this.path = put.value();
            this.contentType = put.contentType();
            this.httpMethod = HttpMethod.PUT;

        }else if (methodAnnotations[0] instanceof Delete) {
            path = ((Delete) methodAnnotation).value();
            this.httpMethod = HttpMethod.DELETE;

        }else {
            throw new EasyHttpException("incorrect type of annotation");
        }
       // 计算路径变量
        this.calVarCount(this.path);

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
}
