package com.smart.framework.commons.core.timezone;

import java.time.ZoneId;

/**
 * 基于ThreadLocal的时区策略
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-05 10:16
 * @since 5.0.0
 */
public class ThreadLocalTimezoneStrategy implements TimezoneStrategy {

    private final ThreadLocal<ZoneId> currentZone = ThreadLocal.withInitial(ZoneId::systemDefault);

    /**
     * 设置时区
     *
     * @param zoneId 时区
     * @param task   任务
     */
    @Override
    public void set(ZoneId zoneId, Runnable task) {
        try {
            currentZone.set(zoneId);
            task.run();
        } finally {
            currentZone.remove(); // 确保清理
        }
    }

    /**
     * 获取时区
     *
     * @return 时区
     */
    @Override
    public ZoneId get() {
        return currentZone.get();
    }
}
