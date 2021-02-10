package com.github.vizaizai.entity;

import com.github.vizaizai.retry.RetryTrigger;

/**
 * 重试设置
 * @author liaochongwei
 * @date 2020/12/15 15:40
 */
public class RetrySettings {
    /**
     * 是否开启
     */
    private Boolean enable;
    /**
     * 最大重试次数
     */
    private Integer maxAttempts;
    /**
     * 间隔时间(ms)
     */
    private Integer intervalTime;
    /**
     * 触发的重试条件
     */
    private RetryTrigger retryTrigger;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Integer getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public Integer getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Integer intervalTime) {
        this.intervalTime = intervalTime;
    }

    public RetryTrigger getRetryTrigger() {
        return retryTrigger;
    }

    public void setRetryTrigger(RetryTrigger retryTrigger) {
        this.retryTrigger = retryTrigger;
    }
}
