package com.github.vizaizai.hander;

import com.github.vizaizai.entity.RetrySettings;
import com.github.vizaizai.exception.EasyHttpException;
import com.github.vizaizai.retry.DefaultRule;
import com.github.vizaizai.retry.RetryTrigger;
import com.github.vizaizai.retry.core.Retry;
import com.github.vizaizai.retry.mode.Modes;

import java.time.temporal.ChronoUnit;

/**
 * @author liaochongwei
 * @date 2020/12/16 16:34
 */
public class RetryHandler implements Handler<Object>{
    private final Context context;

    public RetryHandler(Context context) {
        this.context = context;
    }

    @Override
    public Object execute() {
        RetrySettings retrySettings = context.getRetrySettings();
        // 如果开启重试，注入重试任务
        HttpHandler httpHandler = (HttpHandler) context;
        if (!enableRetry(retrySettings)) {
            throw new EasyHttpException("Can't retry");
        }
        // 最大重试次数
        int max = retrySettings.getMaxAttempts() == null ? 3 : retrySettings.getMaxAttempts();
        // 间隔时间（ms）
        int intervalTime = retrySettings.getIntervalTime() == null ? 10 : retrySettings.getIntervalTime();
        // 触发规则
        RetryTrigger retryTrigger = retrySettings.getRetryTrigger() == null ? new DefaultRule() : retrySettings.getRetryTrigger();
        Retry<Object> retry = Retry.inject(() -> {
            Object result = httpHandler.doHttp();
            // 执行触发器，判断是否重试
            if (retryTrigger.retryable(context)) {
                throw new EasyHttpException(context.getResponse().getCause());
            }
            return result;
        });
        return retry.max(max)
                .mode(Modes.arithmetic(intervalTime, 0, ChronoUnit.MILLIS))
                .retryFor(EasyHttpException.class)
                .execute();
    }

    /**
     * 是否开启重试
     * @param retrySettings
     * @return boolean
     */
    public static boolean enableRetry(RetrySettings retrySettings) {
        return retrySettings != null && Boolean.TRUE.equals(retrySettings.getEnable());
    }
}
