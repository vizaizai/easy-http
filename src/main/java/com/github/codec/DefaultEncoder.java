package com.github.codec;

import com.alibaba.fastjson.JSON;
import org.apache.commons.beanutils.BeanMap;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认编码
 * @author liaochongwei
 * @date 2020/7/31 9:53
 */
public class DefaultEncoder implements Encoder {

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String> encodeMap(Object object) {
        if (object == null) {
            return null;
        }
        Map<Object,Object> map;
        Map<String,String> result = new HashMap<>();

        if (object instanceof  Map) {
            map = (Map<Object, Object>)object;
        }else {
            map = new BeanMap(object);
        }
        map.forEach((k, v)-> result.put(k.toString(), v.toString()));
        return result;
    }

    @Override
    public String encodeString(Object object) {
        if (object == null) {
            return null;
        }
        return JSON.toJSONString(object);
    }
}
