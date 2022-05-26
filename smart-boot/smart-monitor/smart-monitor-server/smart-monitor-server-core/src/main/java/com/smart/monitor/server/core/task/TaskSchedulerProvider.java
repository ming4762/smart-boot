package com.smart.monitor.server.core.task;

import lombok.Getter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 提供定时任务
 * @author ShiZhongMing
 * 2021/3/22 13:57
 * @since 1.0
 */
public class TaskSchedulerProvider implements InitializingBean, DisposableBean {

    @Getter
    private final ThreadPoolTaskScheduler taskScheduler = taskScheduler();


    private static ThreadPoolTaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(20);
        taskScheduler.setRemoveOnCancelPolicy(false);
        taskScheduler.setThreadNamePrefix("Monitor Task");
        return taskScheduler;
    }

    @Override
    public void afterPropertiesSet() {
        this.taskScheduler.afterPropertiesSet();
    }

    @Override
    public void destroy() {
        this.taskScheduler.destroy();
    }
}
