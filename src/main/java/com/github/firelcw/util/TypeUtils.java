package com.github.firelcw.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Stream;

/**
 * 类型工具类
 * @author liaochongwei
 * @date 2020/7/31 11:34
 */
public class TypeUtils {
    public static final String[] SIMPLE_TYPES =
            new String[] {"integer","short", "byte","long","char",
                    "float", "double","boolean","string", "void"};

    private static final String CLASS_LANG_PREFIX = "java.lang.";
    private static final int CLASS_PREFIX_INDEX = 9;

    private static final String ASYNC_CLASS_1 = "java.util.concurrent.CompletableFuture";
    private static final String ASYNC_CLASS_2 = "java.util.concurrent.Future";

    private TypeUtils() {
    }


    /**
     * 获取简单类型
     * @param typeName
     * @return type
     */
    public static String getType(String typeName) {
        String typeLower = typeName.toLowerCase();
        return Stream.of(SIMPLE_TYPES)
                     .filter(typeLower::contains)
                     .findFirst().orElse(null);
    }
    /**
     * 是否为简单参数
     * @param typeName
     * @return isSimple
     */
    public static boolean isSimple(String typeName) {
        String typeLower = typeName.toLowerCase();
        return Stream.of(SIMPLE_TYPES).anyMatch(e->{
            if (typeLower.startsWith(CLASS_LANG_PREFIX)) {
                return typeLower.substring(CLASS_PREFIX_INDEX).contains(e);
            }
            return e.equals(typeLower);
        });
    }

    /**
     * 是否异步请求(要求返回值类型为 Future、CompletableFuture)
     * @param returnType
     * @return boolean
     */
    public static boolean isAsync(Type returnType) {
        String name = returnType.getTypeName();
        return name.startsWith(ASYNC_CLASS_1) || name.startsWith(ASYNC_CLASS_2);
    }

    /**
     * 获取需要编码的返回值类型
     * @param type
     * @return
     */
    public static Type getDecodeType(Type type) {
        if (!isAsync(type)) {
            return type;
        }
        if (type instanceof  ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return parameterizedType.getActualTypeArguments()[0];
        }
       return Object.class;
    }

    /**
     * 获取所有简单类型
     * @return allTypes
     */
    public static String[] getTypes() {
        return SIMPLE_TYPES;
    }

    public static String getIntType() {
        return SIMPLE_TYPES[0];
    }
    public static String getShortType() {
        return SIMPLE_TYPES[1];
    }
    public static String getByteType() {
        return SIMPLE_TYPES[2];
    }
    public static String getLongType() {
        return SIMPLE_TYPES[3];
    }
    public static String getCharType() {
        return SIMPLE_TYPES[4];
    }
    public static String getFloatType() {
        return SIMPLE_TYPES[5];
    }
    public static String getDoubleType() {
        return SIMPLE_TYPES[6];
    }
    public static String getBoolType() {
        return SIMPLE_TYPES[7];
    }
    public static String getStringType() {
        return SIMPLE_TYPES[8];
    }
    public static String getVoidType() {
        return SIMPLE_TYPES[9];
    }
}
