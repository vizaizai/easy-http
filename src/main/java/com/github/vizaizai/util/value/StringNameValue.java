package com.github.vizaizai.util.value;

import com.github.vizaizai.util.Assert;

/**
 * 符串nameValue
 * @author liaochongwei
 * @date 2020/12/22 14:59
 */
public class StringNameValue implements NameValue<String,String> {
    private final String name;
    private final String value;

    public StringNameValue(String name, String value) {
        Assert.notNull(name);
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public String toString() {
        if (this.value == null) {
            return this.name;
        } else {
            return this.name + "=" + this.value;
        }
    }
}
