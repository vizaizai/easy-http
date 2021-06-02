package com.github.vizaizai.util;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
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
    public static final String[] BASE_TYPES_8 =
            new String[] { "int","short", "byte","long","char","float", "double","boolean" };
    public static final String[] VOID_TYPES =
            new String[] { "void","java.lang.Void"};

    private TypeUtils() {
    }


    /**
     * 是否为数组
     * @param type Type
     * @return true or false
     */
    public static boolean isArrayType(final Type type) {
        return type instanceof GenericArrayType || type instanceof Class<?> && ((Class<?>) type).isArray();
    }

    /**
     * 对象声明类型和对象本身共同判断是否为基础类型
     * @param type
     * @param source
     * @return true or false
     */
    public static boolean isBaseType(Type type, Object source) {
        if (type instanceof Class) {
            return isBaseType(type);
        }
        // 如果是通配符类型、参数变量类型或者Object，则可以通过参数本身的class对象来确定类型
        if (type instanceof TypeVariable || type instanceof WildcardType || equals(type, Object.class)) {
            if (source == null) {
                return false;
            }
            return isBaseType(source.getClass());
        }
        return false;
    }

    /**
     * 是否为基本类型
     * @param type
     * @return true or false
     */
    public static boolean isBaseType(Type type) {
        // 8大基本数据类型
        boolean isBase8 = Stream.of(BASE_TYPES_8).anyMatch(e -> e.equals(type.getTypeName()));
        if (isBase8) {
            return true;
        }
        // void java.lang.Void
        boolean isVoid = Stream.of(VOID_TYPES).anyMatch(e -> e.equals(type.getTypeName()));
        if (isVoid) {
            return true;
        }
        // Character | Boolean | String
        if (equals(Character.class, type) || equals(Boolean.class,type)
                || equals(String.class, type)) {
            return true;
        }

        // Number或者Number的子类
        if (type instanceof Class) {
            return isThisType(Number.class, (Class<?>) type);
        }
        return false;
    }


    /**
     * 是否为给定类或给定类的派生类
     * @param thisType 给定类
     * @param otherType 待确认类
     * @return true or false
     */
    public static boolean isThisType(Class<?> thisType, Class<?> otherType) {
        if (equals(thisType, otherType)) {
            return true;
        }
        Class<?> superclass = otherType.getSuperclass();
        // 如果父类不是Object
        if (superclass != null && !equals(superclass,Object.class)) {
            return isThisType(thisType, superclass);
        }
        return false;
    }

    /**
     * 是否异步请求(要求返回值类型为 Future、CompletableFuture)
     * @param returnType
     * @return boolean
     */
    public static boolean isAsync(Type returnType) {
        Class<?> rawType = getRawType(returnType);
        return equals(rawType, CompletableFuture.class) || equals(rawType, Future.class);
    }

    /**
     * 获取需要编码的返回值类型
     * @param type
     * @return Type
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
     * 获取原始类型
     * @param type
     * @return class
     */
    public static Class<?> getRawType(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;

        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (!(rawType instanceof Class)) {
                throw new IllegalArgumentException();
            }
            return (Class<?>) rawType;

        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();

        } else if (type instanceof TypeVariable) { // 类型变量，如: List<T> 无法确定类型，通过实际对象获取
            return Object.class;

        } else if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);

        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
                    + "GenericArrayType, but <" + type + "> is of type "
                    + className);
        }
    }

    /**
     * 比较两个泛型是否相等
     * @param a 泛型A
     * @param b 泛型B
     * @return boolean
     */
    public static boolean equals(Type a, Type b) {
        if (a == b) {
            return true; // Also handles (a == null && b == null).

        } else if (a instanceof Class) {
            return a.equals(b); // Class already specifies equals().

        } else if (a instanceof ParameterizedType) { //普通参数化类型，如：List<String>
            if (!(b instanceof ParameterizedType)) {
                return false;
            }
            ParameterizedType pa = (ParameterizedType) a;
            ParameterizedType pb = (ParameterizedType) b;
            return Objects.equals(pa.getOwnerType(), pb.getOwnerType())
                    && pa.getRawType().equals(pb.getRawType())
                    && Arrays.equals(pa.getActualTypeArguments(), pb.getActualTypeArguments());

        } else if (a instanceof GenericArrayType) { // 参数化数组类型
            if (!(b instanceof GenericArrayType)) {
                return false;
            }
            GenericArrayType ga = (GenericArrayType) a;
            GenericArrayType gb = (GenericArrayType) b;
            return equals(ga.getGenericComponentType(), gb.getGenericComponentType());

        } else if (a instanceof WildcardType) {
            if (!(b instanceof WildcardType)) {
                return false;
            }
            WildcardType wa = (WildcardType) a;
            WildcardType wb = (WildcardType) b;
            return Arrays.equals(wa.getUpperBounds(), wb.getUpperBounds())
                    && Arrays.equals(wa.getLowerBounds(), wb.getLowerBounds());

        } else if (a instanceof TypeVariable) {
            if (!(b instanceof TypeVariable)) {
                return false;
            }
            TypeVariable<?> va = (TypeVariable<?>) a;
            TypeVariable<?> vb = (TypeVariable<?>) b;
            return va.getGenericDeclaration() == vb.getGenericDeclaration()
                    && va.getName().equals(vb.getName());

        } else {
            return false; // This isn't a type we support!
        }
    }
    /*  精准类型判断 */
    public static boolean isInt(Type type) {
        return equals(int.class, type) || equals(Integer.class, type);
    }
    public static boolean isShort(Type type) {
        return equals(short.class, type) || equals(Short.class, type);
    }
    public static boolean isByte(Type type) {
        return equals(byte.class, type) || equals(Byte.class, type);
    }
    public static boolean isLong(Type type) {
        return equals(long.class, type) || equals(Long.class, type);
    }
    public static boolean isChar(Type type) {
        return equals(char.class, type) || equals(Character.class, type);
    }
    public static boolean isFloat(Type type) {
        return equals(float.class, type) || equals(Float.class, type);
    }
    public static boolean isDouble(Type type) {
        return equals(double.class, type) || equals(Double.class, type);
    }
    public static boolean isBool(Type type) {
        return equals(boolean.class, type) || equals(Boolean.class, type);
    }
    public static boolean isVoid(Type type) {
        return equals(void.class, type) || equals(Void.class, type);
    }
    public static boolean isNumber(Type type) {
        return equals(Number.class, type);
    }
    public static boolean isBigDecimal(Type type) {
        return equals(BigDecimal.class, type);
    }
    public static boolean isBigInteger(Type type) {
        return equals(BigInteger.class, type);
    }

}
