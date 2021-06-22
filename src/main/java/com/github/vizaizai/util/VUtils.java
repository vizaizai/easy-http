package com.github.vizaizai.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 常用工具集合
 * @author liaochongwei
 * @date 2021/6/22 11:44
 */
public class VUtils {

    /**
     * 默认初始大小
     */
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    /**
     * 默认增长因子，当Map的size达到 容量*增长因子时，开始扩充Map
     */
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private VUtils() {
    }

    /*====================================StringUtils===========================================*/

    /**
     * 计算长度
     * @param cs CharSequence
     * @return int
     */
    public static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    /**
     * 是否空白字符串
     * @param cs CharSequence
     * @return boolean
     */
    public static boolean isBlank(final CharSequence cs) {
        final int strLen = length(cs);
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否为非空白字符串
     * @param cs CharSequence
     * @return boolean
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * 是否为空字符串
     * @param cs
     * @return boolean
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
    /**
     * 是否为非空字符串
     * @param cs
     * @return boolean
     */
    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    /*======================================CollectionUtils==========================================*/

    /**
     * 判断集合是否为空
     * @param coll
     * @return boolean
     */
    public static boolean isEmpty(Collection coll) {
        return (coll == null || coll.isEmpty());
    }

    /**
     * 判断集合是否不为空
     * @param coll
     * @return boolean
     */
    public static boolean isNotEmpty(Collection coll) {
        return !isEmpty(coll);
    }


    /*======================================MapUtils==========================================*/
    /**
     * map是否为空
     * @param map
     * @return boolean
     */
    public static boolean isEmpty(Map<?,?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * map是否为非空
     * @param map
     * @return boolean
     */
    public static boolean isNotEmpty(Map<?,?> map) {
        return !isEmpty(map);
    }

    /**
     * 新建一个HashMap
     *
     * @param <K> Key类型
     * @param <V> Value类型
     * @return HashMap对象
     */
    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<>();
    }

    /**
     * 新建一个HashMap
     *
     * @param <K>     Key类型
     * @param <V>     Value类型
     * @param size    初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75 + 1
     * @param isOrder Map的Key是否有序，有序返回 {@link LinkedHashMap}，否则返回 {@link HashMap}
     * @return HashMap对象
     * @since 3.0.4
     */
    public static <K, V> Map<K, V> newHashMap(int size, boolean isOrder) {
        int initialCapacity = (int) (size / DEFAULT_LOAD_FACTOR) + 1;
        return isOrder ? new LinkedHashMap<>(initialCapacity) : new HashMap<>(initialCapacity);
    }

    /**
     * 新建一个HashMap
     *
     * @param <K>  Key类型
     * @param <V>  Value类型
     * @param size 初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75 + 1
     * @return HashMap对象
     */
    public static <K, V> Map<K, V> newHashMap(int size) {
        return newHashMap(size, false);
    }


}
