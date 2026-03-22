package com.smart.framework.commons.core.timezone;

import com.smart.framework.commons.core.constants.HttpHeaderConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.time.ZoneId;

/**
 * 时区上下文
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-05 10:18
 * @since 5.0.0
 */
public final class SmartTimezoneContext {

    private SmartTimezoneContext() {
        throw new UnsupportedOperationException("SmartTimezoneContext is a utility class and cannot be instantiated");
    }

    private static final TimezoneStrategy STRATEGY;

    static {
        STRATEGY = detectStrategy();
    }

    /**
     * 检测时区策略
     * @return 时区策略
     */
    private static TimezoneStrategy detectStrategy() {
        try {
            Class.forName("java.lang.ScopedValue");
            return new ScopeValueTimezoneStrategy();
        } catch (ClassNotFoundException _) {
            return new ThreadLocalTimezoneStrategy();
        }
    }

    /**
     * 设置时区并执行任务
     * @param zoneId 时区
     * @param task   任务
     */
    public static void run(ZoneId zoneId, Runnable task) {
        STRATEGY.set(zoneId, task);
    }

    /**
     * 获取当前线程的时区
     * @return 时区
     */
    public static ZoneId get() {
        return STRATEGY.get();
    }

    /**
     * 从请求头中获取时区
     * @param request 请求
     * @return 时区
     */
    public static ZoneId getFromHeader(HttpServletRequest request) {
        String userTimezone = request.getHeader(HttpHeaderConstants.USER_TIMEZONE);
        if (StringUtils.hasText(userTimezone)) {
            return ZoneId.of(userTimezone);
        }
        return null;
    }
}
