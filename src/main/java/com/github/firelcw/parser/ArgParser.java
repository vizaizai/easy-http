package com.github.firelcw.parser;


import com.github.firelcw.annotation.Data;
import com.github.firelcw.annotation.Headers;
import com.github.firelcw.annotation.Params;
import com.github.firelcw.annotation.Var;
import com.github.firelcw.exception.EasyHttpException;
import com.github.firelcw.util.TypeUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 参数解析
 * @author liaochongwei
 * @date 2020/7/30 15:40
 */
public class ArgParser {

    /**
     * 目标参数
     */
    private Object target;
    /**
     * 参数类型
     */
    private Class<?> argClass;
    /**
     * 方法
     */
    private Method method;
    /**
     * 参数索引
     */
    private int index;
    /**
     * 是否为简单类型
     */
    private boolean isSimple;
    /**
     * 参数类型
     */
    private String type;
    /**
     * 路径变量名
     */
    private String varName;

    public ArgParser(Object target, Method method, int index) {
        this.target = target;
        this.method = method;
        this.index = index;
        this.parse();
    }

    private void parse() {

        // 判断参数时简单参数还是复合参数
        Class<?> argClazz = target.getClass();
        this.argClass =  argClazz;
        // 是否为简单参数
        this.isSimple = TypeUtils.isSimple(argClazz.getTypeName());
        Annotation[] annotations = this.getParameterAnnotation();

        // 一个参数最多一个注解
        if (annotations.length > 1) {
            throw new EasyHttpException("The number of annotations for a arg  is at most 1");
        }
        if (annotations.length == 0) {
            this.type = Params.TYPE;
        }else {
            Annotation annotation = annotations[0];
            if (annotation instanceof Var) {
                this.varName = ((Var) annotation).value();
                this.type = Var.TYPE;
            }else if (annotation instanceof Params) {
                this.type = Params.TYPE;
            }else if (annotation instanceof Data) {
                this.type = Data.TYPE;
            }else if (annotation instanceof Headers) {
                this.type = Headers.TYPE;
            }else {
                this.type = "non";
            }
        }

        // 规则校验1: 简单参数只能用@Var
        if (this.isSimple && !this.type.equals(Var.TYPE)) {
            throw new EasyHttpException("You can only use @var for simple argument");
        }
        // 贵州校验2： 复杂参数不能使用@Var
        if (!this.isSimple && this.type.equals(Var.TYPE)) {
            throw new EasyHttpException("You can't use @var for complex argument");
        }
    }

    /**
     * 获取方法参数注解
     * @return
     */
    private Annotation[] getParameterAnnotation() {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations.length < this.index + 1) {
            return new Annotation[]{};
        }
        return parameterAnnotations[index];
    }
    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Class<?> getArgClass() {
        return argClass;
    }

    public void setArgClass(Class<?> argClass) {
        this.argClass = argClass;
    }

    public boolean isSimple() {
        return isSimple;
    }

    public void setSimple(boolean simple) {
        isSimple = simple;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

