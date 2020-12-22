package com.github.vizaizai.util.value;

import java.util.List;

/**
 * 请求头nameValue
 * @author liaochongwei
 * @date 2020/12/22 16:20
 */
public class HeadersNameValues extends StringNameValues {

    public List<String> getHeaders(String name) {
        return super.getValues(name);
    }
}
