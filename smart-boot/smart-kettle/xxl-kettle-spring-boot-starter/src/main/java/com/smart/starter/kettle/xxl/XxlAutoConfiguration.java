package com.smart.starter.kettle.xxl;

import com.smart.kettle.core.service.KettleService;
import com.smart.starter.kettle.xxl.handler.XxlKettleExecuteHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2021/7/19 6:29 下午
 */
@Configuration(proxyBeanMethods = false)
public class XxlAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public XxlKettleExecuteHandler xxlKettleExecuteHandler(KettleService kettleService) {
        return new XxlKettleExecuteHandler(kettleService);
    }
}
