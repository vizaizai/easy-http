package com.github.vizaizai.retry;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 时间限制
 * 同一个URL,重试全部失败后,3分钟之内不再重试
 * @author liaochongwei
 * @date 2020/12/16 14:13
 */
public class RetryLimiter {

    private RetryLimiter() {
    }

    private static final Map<String, LocalDateTime> CACHE = new ConcurrentHashMap<>();

    /**
     * 是否限制
     * @param url
     * @return true-limit  false-not limit
     */
    public static boolean limit(String url) {
        LocalDateTime lastTime = CACHE.get(url);
        if (lastTime == null) {
            return false;
        }
        return lastTime.until(LocalDateTime.now(), ChronoUnit.SECONDS) < 60;
    }

    public static void add(String url) {
        CACHE.put(url, LocalDateTime.now());
    }
    public static void delete(String url) {
        CACHE.remove(url);

    }

}
