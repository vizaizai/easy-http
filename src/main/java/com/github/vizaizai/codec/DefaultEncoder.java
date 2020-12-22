package com.github.vizaizai.codec;

import com.alibaba.fastjson.JSON;
import com.github.vizaizai.util.Utils;
import com.github.vizaizai.util.value.StringNameValues;
import org.apache.commons.beanutils.BeanMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认编码
 * @author liaochongwei
 * @date 2020/7/31 9:53
 */
public class DefaultEncoder implements Encoder {

    @Override
    @SuppressWarnings("unchecked")
    public StringNameValues encodeNameValue(Object object) {
        if (object == null) {
            return null;
        }
        Map<Object,Object> map;
        if (object instanceof  Map) {
            map = (Map<Object, Object>)object;
        } else {
            map = new HashMap<>(new BeanMap(object));
            if (map.get("class") != null) {
                map.remove("class");
            }
        }
        return Utils.toNameValues(map);
    }

    @Override
    public String encodeString(Object object) {
        if (object == null) {
            return null;
        }
        return JSON.toJSONString(object);
    }
}
