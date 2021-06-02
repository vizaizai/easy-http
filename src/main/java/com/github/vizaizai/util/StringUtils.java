package com.github.vizaizai.util;

/**
 * 字符串工具类
 * @author liaochongwei
 * @date 2021/6/2 16:16
 */
public class StringUtils {
    private StringUtils() {
    }

    /**
     * 计算长度
     * @param cs CharSequence
     * @return 长度
     */
    public static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    /**
     * 是否空白字符串
     * @param cs CharSequence
     * @return 是否空白
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
     * @return 是否为非空白
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }
}
