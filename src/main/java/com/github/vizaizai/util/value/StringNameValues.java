package com.github.vizaizai.util.value;

/**
 *
 * @author liaochongwei
 * @date 2020/12/22 14:53
 */
public class StringNameValues extends NameValues<String,String> {
    public void add(String name, String value) {
        this.add(new StringNameValue(name, value));
    }
}
