package com.github.vizaizai.parser;


import com.github.vizaizai.annotation.Body;
import com.github.vizaizai.annotation.Headers;
import com.github.vizaizai.annotation.Param;
import com.github.vizaizai.annotation.Var;
import com.github.vizaizai.exception.EasyHttpException;
import com.github.vizaizai.util.TypeUtils;
import com.github.vizaizai.util.VUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

/**
 * 参数
 * @author liaochongwei
 * @date 2020/7/30 15:40
 */
public class Arg {

    /**
     * 参数源
     */
    private Object source;
    /**
     * 数据类型
     */
    private Type dataType;
    /**
     * 方法
     */
    private Method method;
    /**
     * 参数索引
     */
    private int index;
    /**
     * 是否为基础类型(基本数据类型+包装类型+void+String + Number或Number派生类)
     */
    private boolean baseType;
    /**
     * 参数类型
     */
    private String type;
    /**
     * 变量名
     */
    private String varName;
    /**
     * 参数名称
     */
    private String argName;

    public static Arg instance(Object source, Method method, int index) {
        return new Arg(source, method, index);
    }
    private Arg(Object source, Method method, int index) {
        this.source = source;
        this.method = method;
        this.index = index;
    }

    public void parse() {
        // 判断参数时简单参数还是对象参数
        Type argType = this.method.getParameters()[this.index].getParameterizedType();
        this.dataType =  argType;
        // 是否基础类型
        this.baseType = TypeUtils.isBaseType(argType, this.getSource());
        Annotation[] annotations = this.getParameterAnnotation();

        // 获取参数名称
        Parameter[] parameters = this.method.getParameters();
        this.argName = parameters.length > this.index ? parameters[this.index].getName() : "arg" + this.index;

        // 一个参数最多一个注解
        if (annotations.length > 1) {
            throw new EasyHttpException("There is at most one annotation on a parameter");
        }
        if (annotations.length == 0) {
            this.type = Param.TYPE;
            this.varName = this.varNameFor(null);
        }else {
            Annotation annotation = annotations[0];
            this.varName = this.varNameFor(annotation);

            if (annotation instanceof Var) {
                this.type = Var.TYPE;
            }else if (annotation instanceof Param) {
                this.type = Param.TYPE;
            }else if (annotation instanceof Body) {
                this.type = Body.TYPE;
            }else if (annotation instanceof Headers) {
                this.type = Headers.TYPE;
            }else {
                this.type = "non";
            }
        }

        // 规则校验1: @Var只能注解在简单类型上
        if (Var.TYPE.equals(this.type) && !this.baseType) {
            throw new EasyHttpException("@Var must annotate on basic parameter. eg: String,Integer...");
        }
        // 规则校验2: @Headers只能注解在JavaBean或者map上
        if (Headers.TYPE.equals(this.type) && this.baseType) {
            throw new EasyHttpException("@Headers must annotate on JavaBean or Map.");
        }

    }

    /**
     * 获取方法参数注解
     * @return Annotation[]
     */
    private Annotation[] getParameterAnnotation() {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations.length < this.index + 1) {
            return new Annotation[]{};
        }
        return parameterAnnotations[index];
    }

    /**
     * 获取变量名称(优先取注解上的名称)
     * @param annotation 注解
     * @return varName
     */
    private String varNameFor(Annotation annotation) {
        String name;
        if (annotation == null) {
            return this.argName;
        }
        if (annotation instanceof Body) {
            return ((Body) annotation).wrapRoot();
        }
        if (annotation instanceof Var) {
            name= ((Var) annotation).value();
        }else if (annotation instanceof Param) {
            name = ((Param) annotation).value();
        }else {
            name = "";
        }
        return VUtils.isNotBlank(name) ? name : this.argName;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }


    public Type getDataType() {
        return dataType;
    }

    public void setDataType(Type dataType) {
        this.dataType = dataType;
    }

    public boolean isBaseType() {
        return baseType;
    }

    public void setBaseType(boolean baseType) {
        this.baseType = baseType;
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

    public String getArgName() {
        return argName;
    }
}

