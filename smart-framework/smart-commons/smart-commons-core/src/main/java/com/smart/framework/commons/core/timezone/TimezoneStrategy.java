package com.smart.framework.commons.core.timezone;

import java.time.ZoneId;

/**
 * 时区策略接口
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-05 10:13
 * @since 5.0.0
 */
public interface TimezoneStrategy {
    /**
     * 设置时区
     * @param zoneId 时区
     * @param task 任务
     */
    void set(ZoneId zoneId, Runnable task);

    /**
     * 获取时区
     * @return 时区
     */
    ZoneId get();
}
