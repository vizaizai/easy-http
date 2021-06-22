package com.github.vizaizai.entity;

import com.github.vizaizai.util.VUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 排除路径
 * @author liaochongwei
 * @date 2020/8/11 10:01
 */
public class ExcludePath {
    private final String path;
    private List<HttpMethod> methods;

    private ExcludePath(String path, HttpMethod ...methods) {
        this.path = path;
        if (methods != null) {
            this.methods = Arrays.asList(methods);
        }
    }
    public static ExcludePath instance(String path, HttpMethod ...methods) {
       return new ExcludePath(path, methods);
    }

    public boolean match(String url, HttpMethod method) {
        boolean containPath;
        if (this.path.endsWith("/**")) {
            String prefix = this.path.substring(0, this.path.indexOf("/**")); //路径前缀
            // 包含前缀即可
            containPath = url.contains(prefix);
        }else {
            containPath = url.contains(path);
        }
        if (!containPath) {
            return false;
        }
        if (VUtils.isNotEmpty(this.methods)) {
            return this.methods.stream().anyMatch(e -> e.equals(method));
        }
        return true;
    }

    public String getPath() {
        return path;
    }

    public List<HttpMethod> getMethods() {
        return methods;
    }
}
