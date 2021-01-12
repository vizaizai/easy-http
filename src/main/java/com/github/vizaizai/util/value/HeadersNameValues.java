package com.github.vizaizai.util.value;

import com.github.vizaizai.util.Assert;

import java.util.List;

/**
 * 请求头nameValue
 * @author liaochongwei
 * @date 2020/12/22 16:20
 */
public class HeadersNameValues extends StringNameValues {

    public List<String> getHeaders(String name) {
        return this.getValues(name);
    }

    public void addHeaders(String name, List<String> values) {
        Assert.notNull(values, "values must be not null");
        for (String value : values) {
            this.add(name, value);
        }
    }
}
