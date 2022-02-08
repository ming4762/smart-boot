package com.smart.monitor.client.core.registration;

import com.smart.monitor.client.core.properties.ClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ScheduledFuture;

/**
 * 客户端注册 注销监听器
 * 服务器启动后 启动注册服务
 * 服务关闭前 注销应用
 * @author shizhongming
 * 2021/3/21
 */
@Slf4j
public class RegistrationApplicationListener implements InitializingBean, DisposableBean {

    private final ApplicationRegistrar applicationRegistrar;

    private final ClientProperties clientProperties;


    @NonNull
    private final ThreadPoolTaskScheduler taskScheduler = taskScheduler();

    @Nullable
    private volatile ScheduledFuture<?> scheduledTask = null;

    public RegistrationApplicationListener(ApplicationRegistrar applicationRegistrar, ClientProperties clientProperties) {
        this.applicationRegistrar = applicationRegistrar;
        this.clientProperties = clientProperties;
    }

    /**
     * 系统启动事件
     */
    @Order()
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        this.startRegisterTask();
    }

    /**
     * 系统关闭事件
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @EventListener(ContextClosedEvent.class)
    public void onContextClosed() {
        this.stopRegisterTask();
        this.applicationRegistrar.deregister();
    }


    /**
     * 启动注册任务
     */
    public void startRegisterTask() {
        if (this.scheduledTask != null && !this.scheduledTask.isDone()) {
            return;
        }
        this.scheduledTask = this.taskScheduler.scheduleWithFixedDelay(this.applicationRegistrar :: register, this.clientProperties.getRegistration().getInterval());
        log.debug("registration task is running every {}ms", this.clientProperties.getRegistration().getInterval().toMillis());
    }

    /**
     * 关闭注册定时任务
     */
    public void stopRegisterTask() {
        if (this.scheduledTask != null && !this.scheduledTask.isDone()) {
            this.scheduledTask.cancel(true);
            log.debug("registration task is stop");
        }
    }

    @Override
    public void destroy() {
        this.taskScheduler.destroy();
    }

    @Override
    public void afterPropertiesSet() {
        this.taskScheduler.afterPropertiesSet();
    }

    private static ThreadPoolTaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.setRemoveOnCancelPolicy(true);
        taskScheduler.setThreadNamePrefix("registrationTask");
        return taskScheduler;
    }
}
