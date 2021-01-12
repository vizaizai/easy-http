package com.github.vizaizai.parser;


import com.github.vizaizai.annotation.Body;
import com.github.vizaizai.annotation.Headers;
import com.github.vizaizai.annotation.Query;
import com.github.vizaizai.annotation.Var;
import com.github.vizaizai.exception.EasyHttpException;
import com.github.vizaizai.util.TypeUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 参数解析
 * @author liaochongwei
 * @date 2020/7/30 15:40
 */
public class ArgParser {

    /**
     * 参数源
     */
    private Object source;
    /**
     * 参数clazz
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
     * 变量名
     */
    private String varName;
    /**
     * 参数名称
     */
    private String argName;

    public static ArgParser doParse(Object source, Method method, int index) {
        return new ArgParser(source, method, index);
    }
    private ArgParser(Object source, Method method, int index) {
        this.source = source;
        this.method = method;
        this.index = index;
        this.parse();
    }

    private void parse() {
        // 判断参数时简单参数还是对象参数
        Class<?> argClazz = source.getClass();
        this.argClass =  argClazz;
        // 是否为简单参数
        this.isSimple = TypeUtils.isSimple(argClazz.getTypeName());
        Annotation[] annotations = this.getParameterAnnotation();

        // 获取参数名称
        Parameter[] parameters = this.method.getParameters();
        this.argName = parameters.length > this.index ? parameters[this.index].getName() : "arg" + this.index;

        // 一个参数最多一个注解
        if (annotations.length > 1) {
            throw new EasyHttpException("At most one annotation on a parameter");
        }
        if (annotations.length == 0) {
            this.type = Query.TYPE;
            this.varName = this.varNameFor(null);
        }else {
            Annotation annotation = annotations[0];
            this.varName = this.varNameFor(annotation);

            if (annotation instanceof Var) {
                this.type = Var.TYPE;
            }else if (annotation instanceof Query) {
                this.type = Query.TYPE;
            }else if (annotation instanceof Body) {
                this.type = Body.TYPE;
            }else if (annotation instanceof Headers) {
                this.type = Headers.TYPE;
            }else {
                this.type = "non";
            }
        }

        // 规则校验1: @Var只能注解在简单类型上
        if (Var.TYPE.equals(this.type) && !this.isSimple) {
            throw new EasyHttpException("@var only can annotate on a simple parameter");
        }
        // 规则校验2: @Headers只能注解在复杂类型上
        if (Headers.TYPE.equals(this.type) && this.isSimple) {
            throw new EasyHttpException("@Headers only can annotate on a complex parameter");
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
        }else if (annotation instanceof Query) {
            name = ((Query) annotation).value();
        }else {
            name = "";
        }
        return StringUtils.isNotBlank(name) ? name : this.argName;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
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

    public String getArgName() {
        return argName;
    }
}

