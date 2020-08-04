package com.github.firelcw.codec;

import java.util.Map;

/**
 * @author liaochongwei
 * @date 2020/7/31 9:30
 */
public interface Encoder {
    /**
     * 编码成map(用于params 和 headers)
     * @param object 待编码对象
     * @return Map
     */
     Map<String,String> encodeMap(Object object);

    /**
     * 编码成String（用于body）
     * @param object
     * @return String
     */
     String encodeString(Object object);
}
