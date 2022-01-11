package com.smart.starter.exception;

import com.smart.starter.exception.config.ExceptionMessageProcessorBeanConfig;
import com.smart.starter.exception.handler.DefaultExceptionMessageHandler;
import com.smart.starter.exception.handler.ExceptionMessageHandler;
import com.smart.starter.exception.handler.GlobalExceptionHandler;
import com.smart.starter.exception.notice.AsyncNoticeHandler;
import com.smart.starter.exception.notice.ConsoleExceptionNotice;
import com.smart.starter.exception.notice.ExceptionNotice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 异常处理启动器自动配置类
 * @author shizhongming
 * 2020/11/15 12:00 上午
 */
@Configuration
@Import(ExceptionMessageProcessorBeanConfig.class)
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
    @ConditionalOnMissingBean(ExceptionNotice.class)
    public ConsoleExceptionNotice consoleExceptionNotice() {
        return new ConsoleExceptionNotice();
    }

}
