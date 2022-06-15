package com.smart.starter.exception.handler;

import com.smart.commons.core.message.ExceptionResult;
import com.smart.commons.core.message.Result;
import com.smart.starter.exception.processor.ExceptionMessageProcessor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的异常信息处理类
 * @author shizhongming
 * 2021/1/24 12:56 下午
 */
@Slf4j
public class DefaultExceptionMessageHandler implements ExceptionMessageHandler, InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final Map<Type, ExceptionMessageProcessor<? extends Exception>> messageProcessorMap = new ConcurrentHashMap<>();

    @Override
    public Object message(Exception e, long exceptionNo, HttpServletRequest request) {
        ExceptionMessageProcessor processor =  messageProcessorMap.get(e.getClass());
        Object result;
        if (Objects.isNull(processor)) {
            processor = messageProcessorMap.get(Exception.class);
        }
        if (Objects.nonNull(processor)) {
            result = processor.message(e, exceptionNo, request);
        } else  {
            log.error("系统发生未知异常", e);
            result = Result.failure("系统发生未知异常", e.toString());
        }
        if (result instanceof Result<?>) {
            result = ExceptionResult.build((Result<?>)result, exceptionNo);
        }
        return result;
    }


    @Override
    public void afterPropertiesSet() {
        // 获取所有的ExceptionMessageProcessor对象
        final Map<String, ExceptionMessageProcessor> processorMap = applicationContext.getBeansOfType(ExceptionMessageProcessor.class);
        // 存储类型
        final Map<Type, List<ExceptionMessageProcessor>> classListMap = new HashMap<>(8);
        processorMap.values().forEach(processor -> {
            final Type type = processor.processorType();
            if (!classListMap.containsKey(type)) {
                classListMap.put(type, new ArrayList<>());
            }
            classListMap.get(type).add(processor);
        });
        // 遍历获取序号最小的一个
        classListMap.forEach((type, exceptionMessageProcessors) -> {
            if (exceptionMessageProcessors.size() == 1) {
                messageProcessorMap.put(type, exceptionMessageProcessors.get(0));
            } else {
                ExceptionMessageProcessor exceptionMessageProcessor = exceptionMessageProcessors.stream().min((item1, item2) -> Integer.min(item1.order(), item2.order())).get();
                log.warn("异常类型：【{}】存在{}个处理器，优先使用order最小的处理其：【{}】", type.getTypeName(), exceptionMessageProcessors.size(), exceptionMessageProcessor.getClass().getName());
                messageProcessorMap.put(type, exceptionMessageProcessor);
            }
        });
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
