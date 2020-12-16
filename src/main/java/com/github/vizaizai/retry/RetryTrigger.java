package com.github.vizaizai.retry;

import com.github.vizaizai.hander.Context;

/**
 * 重试触发器
 * @author liaochongwei
 * @date 2020/12/16 10:59
 */
@FunctionalInterface
public interface RetryTrigger {
    /**
     * 是否重试
     * @param context http上下文
     * @return boolean
     */
    boolean retryable(Context context);
}
