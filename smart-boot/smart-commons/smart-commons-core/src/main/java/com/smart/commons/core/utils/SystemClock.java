package com.smart.commons.core.utils;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 时间戳获取类，解决linux下 System.currentTimeMillis() 性能问题
 * @author shizhongming
 * 2024/7/7 1:32
 * @since 3.0.0
 */
public class SystemClock {

    private final long period;
    private final AtomicLong now;

    private SystemClock(long period) {
        this.period = period;
        this.now = new AtomicLong(System.currentTimeMillis());
        refreshTimeMillis();
    }

    /**
     * 获取当前的毫秒数
     * @return 毫秒数
     */
    public static long now() {
        return getInstance().now.get();
    }

    private static SystemClock getInstance() {
        return SystemClockEnum.SYSTEM_CLOCK.getInstance();
    }

    /**
     * 每毫秒刷新当前时间戳
     */
    private void refreshTimeMillis() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = this.createThreadPoolTaskScheduler();
        threadPoolTaskScheduler.initialize();
        threadPoolTaskScheduler.scheduleAtFixedRate(
                () -> now.set(System.currentTimeMillis()),
                Duration.ofMillis(period)
        );
    }

    private ThreadPoolTaskScheduler createThreadPoolTaskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.setRemoveOnCancelPolicy(false);
        taskScheduler.setThreadNamePrefix("System Clock");
        return taskScheduler;
    }


    private enum SystemClockEnum {
        /**
         * 枚举单例模式
         */
        SYSTEM_CLOCK;

        private final SystemClock systemClock;

        SystemClockEnum() {
            systemClock = new SystemClock(1L);
        }

        private SystemClock getInstance() {
            return systemClock;
        }
    }
}
