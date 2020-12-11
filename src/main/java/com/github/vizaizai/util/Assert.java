package com.github.vizaizai.util;

import java.util.Collection;

/**
 * 断言工具
 * @author liaochongwei
 * @date 2020/12/10 10:18
 */
public class Assert {
    private Assert() {
    }
    public static void isTrue(boolean expression) {
        isTrue(expression,"");
    }
    public static void isTrue(boolean expression, String message) {
        isTrue(expression, new IllegalArgumentException(message));
    }
    public static void isTrue(boolean expression, RuntimeException exception) {
        if (!expression) {
            throw exception;
        }
    }

    public static void isFalse(boolean expression) {
        isTrue(!expression);
    }
    public static void isFalse(boolean expression, String message) {
        isTrue(!expression, message);
    }
    public static void isFalse(boolean expression, RuntimeException exception) {
        isTrue(!expression, exception);
    }

    public static void isNull(Object object) {
        isNull(object,"[Assertion failed] - the argument must be null");
    }
    public static void isNull(Object object, String message) {
        isNull(object, new IllegalArgumentException(message));
    }
    public static void isNull(Object object, RuntimeException exception) {
        if (object != null) {
            throw exception;
        }
    }

    public static void notNull(Object object) {
        notNull(object,"[Assertion failed]- this argument is required");
    }
    public static void notNull(Object object, String message) {
        notNull(object, new IllegalArgumentException(message));
    }
    public static void notNull(Object object, RuntimeException exception) {
        if (object == null) {
            throw exception;
        }
    }

    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection,"the collection must be not empty");
    }
    public static void notEmpty(Collection<?> collection, String message) {
        notEmpty(collection, new IllegalArgumentException(message));
    }
    public static void notEmpty(Collection<?> collection, RuntimeException exception) {
        if (collection == null || collection.isEmpty()) {
            throw exception;
        }
    }


    public static void isEmpty(Collection<?> collection) {
        isEmpty(collection,"");
    }
    public static void isEmpty(Collection<?> collection, String message) {
        isEmpty(collection, new IllegalArgumentException(message));
    }
    public static void isEmpty(Collection<?> collection, RuntimeException exception) {
        if (!(collection == null || collection.isEmpty())) {
            throw exception;
        }
    }
}