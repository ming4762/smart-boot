package com.smart.framework.commons.core.timezone;

import java.time.ZoneId;

/**
 * 基于ScopeValue的时区策略
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-05 10:15
 * @since 5.0.0
 */
public class ScopeValueTimezoneStrategy implements TimezoneStrategy {

    private final ScopedValue<ZoneId> currentZone = ScopedValue.newInstance();

    /**
     * 设置时区
     *
     * @param zoneId 时区
     * @param task   任务
     */
    @Override
    public void set(ZoneId zoneId, Runnable task) {
        ScopedValue.where(currentZone, zoneId).run(task);
    }

    /**
     * 获取时区
     *
     * @return 时区
     */
    @Override
    public ZoneId get() {
        return currentZone.orElse(ZoneId.systemDefault());
    }
}
