package com.github.vizaizai.entity.form;

import com.github.vizaizai.util.Assert;
import com.github.vizaizai.util.value.NameValue;

/**
 * @author liaochongwei
 * @date 2021/2/5 11:10
 */
public class FormDataNameValue implements NameValue<String, BodyContent> {
    private final String name;
    private final BodyContent value;

    public FormDataNameValue(String name, BodyContent value) {
        Assert.notNull("The name must be not null");
        this.name = name;
        this.value = value;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public BodyContent getValue() {
        return value;
    }
}
