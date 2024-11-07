package com.smart.boot.autoconfigure.kettle;

import com.smart.framework.kettle.core.service.KettleService;
import com.smart.framework.kettle.core.xxl.XxlKettleExecuteHandler;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2021/7/19 6:29 下午
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({XxlJobSpringExecutor.class, KettleService.class})
public class SmartKettleXxlAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public XxlKettleExecuteHandler xxlKettleExecuteHandler(KettleService kettleService) {
        return new XxlKettleExecuteHandler(kettleService);
    }
}
