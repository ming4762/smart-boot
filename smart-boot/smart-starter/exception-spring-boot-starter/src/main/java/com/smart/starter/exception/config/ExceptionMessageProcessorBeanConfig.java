package com.smart.starter.exception.config;

import com.smart.starter.exception.processor.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2021/3/3 14:54
 * @since 1.0
 */
@Configuration
public class ExceptionMessageProcessorBeanConfig {

    @Bean
    public DefaultNoHandlerFoundExceptionMessageProcessor defaultNoHandlerFoundExceptionMessageProcessor() {
        return new DefaultNoHandlerFoundExceptionMessageProcessor();
    }

    @Bean
    public DefaultExceptionHandler defaultExceptionHandler() {
        return new DefaultExceptionHandler();
    }

    @Bean
    public DefaultHttpRequestMethodNotSupportedExceptionMessageProcessor defaultHttpRequestMethodNotSupportedExceptionMessageProcessor() {
        return new DefaultHttpRequestMethodNotSupportedExceptionMessageProcessor();
    }

    @Bean
    public DefaultHttpMessageNotReadableExceptionMessageProcessor defaultHttpMessageNotReadableExceptionMessageProcessor() {
        return new DefaultHttpMessageNotReadableExceptionMessageProcessor();
    }

    @Bean
    public DefaultMethodArgumentTypeMismatchExceptionMessageProcessor defaultMethodArgumentTypeMismatchExceptionMessageProcessor() {
        return new DefaultMethodArgumentTypeMismatchExceptionMessageProcessor();
    }

    @Bean
    public DefaultI18nExceptionMessageProcessor defaultI18nExceptionMessageProcessor() {
        return new DefaultI18nExceptionMessageProcessor();
    }

    @Bean
    public DefaultBaseExceptionProcessor defaultBaseExceptionProcessor() {
        return new DefaultBaseExceptionProcessor();
    }

    @Bean
    public DefaultHttpMediaTypeNotSupportedExceptionProcessor defaultHttpMediaTypeNotSupportedExceptionProcessor() {
        return new DefaultHttpMediaTypeNotSupportedExceptionProcessor();
    }

    @Bean
    public DefaultAccessDeniedExceptionProcessor defaultAccessDeniedExceptionProcessor() {
        return new DefaultAccessDeniedExceptionProcessor();
    }

    @Bean
    public DefaultBusinessExceptionProcessor defaultBusinessExceptionProcessor() {
        return new DefaultBusinessExceptionProcessor();
    }

    @Bean
    public DefaultBindExceptionProcessor defaultBindExceptionProcessor() {
        return new DefaultBindExceptionProcessor();
    }

    @Bean
    public DefaultMethodArgumentNotValidExceptionProcessor defaultMethodArgumentNotValidExceptionProcessor() {
        return new DefaultMethodArgumentNotValidExceptionProcessor();
    }

    @Bean
    public DefaultConstraintViolationExceptionProcessor defaultConstraintViolationExceptionProcessor() {
        return new DefaultConstraintViolationExceptionProcessor();
    }

    @Bean
    public DefaultAsyncRequestTimeoutExceptionProcessor defaultAsyncRequestTimeoutExceptionProcessor() {
        return new DefaultAsyncRequestTimeoutExceptionProcessor();
    }
}
