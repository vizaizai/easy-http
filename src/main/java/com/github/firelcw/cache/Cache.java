package com.github.firelcw.cache;

import com.github.firelcw.model.HttpRequest;
import com.github.firelcw.model.HttpRequestConfig;
import com.github.firelcw.model.HttpResponse;

/**
 * @author liaochongwei
 * @date 2020/11/19 10:28
 */
public interface Cache {
    HttpResponse load(HttpRequest request, HttpRequestConfig config);
}
