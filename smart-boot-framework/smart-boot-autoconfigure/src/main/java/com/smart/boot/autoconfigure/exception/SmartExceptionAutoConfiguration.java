package com.smart.boot.autoconfigure.exception;

import com.smart.framework.exception.handler.DefaultExceptionMessageHandler;
import com.smart.framework.exception.handler.ExceptionMessageHandler;
import com.smart.framework.exception.handler.GlobalExceptionHandler;
import com.smart.framework.exception.notice.AsyncNoticeHandler;
import com.smart.framework.exception.notice.ConsoleExceptionNotice;
import com.smart.framework.exception.notice.EventExceptionNotice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 异常处理启动器自动配置类
 * @author shizhongming
 * 2020/11/15 12:00 上午
 */
@Configuration
@Import(ExceptionMessageProcessorBeanConfiguration.class)
@ConditionalOnClass(GlobalExceptionHandler.class)
public class SmartExceptionAutoConfiguration {

    /**
     * 创建异步通知执行器
     * @return 异步通知执行器
     */
    @Bean
    public AsyncNoticeHandler asyncNoticeHandler(ApplicationContext applicationContext) {
        return new AsyncNoticeHandler();
    }

    /**
     * 创建异常信息处理类
     * @return 异常信息处理类
     */
    @Bean
    @ConditionalOnMissingBean(ExceptionMessageHandler.class)
    public ExceptionMessageHandler defaultExceptionMessageHandler() {
        return new DefaultExceptionMessageHandler();
    }

    /**
     * 创建全局异常拦截器
     * @param asyncNoticeHandler 创建异步通知执行器
     * @return 创建全局异常拦截器
     */
    @Bean
    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    public GlobalExceptionHandler globalExceptionHandler(AsyncNoticeHandler asyncNoticeHandler, ExceptionMessageHandler exceptionMessageHandler) {
        return new GlobalExceptionHandler(asyncNoticeHandler, exceptionMessageHandler);
    }

    /**
     * 默认的异常通知类
     * @return 控制台异常通知
     */
    @Bean
    public ConsoleExceptionNotice consoleExceptionNotice() {
        return new ConsoleExceptionNotice();
    }

    @Bean
    public EventExceptionNotice eventExceptionNotice(ApplicationEventPublisher applicationEventPublisher) {
        return new EventExceptionNotice(applicationEventPublisher);
    }

}
