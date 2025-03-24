package com.smart.boot.autoconfigure.xxl;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2024/6/26 20:50
 * @since 3.0.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SmartXxlExecutorProperties.class)
@Slf4j
@ConditionalOnClass(XxlJobSpringExecutor.class)
public class SmartXxlExecutorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public XxlJobSpringExecutor xxlJobSpringExecutor(SmartXxlExecutorProperties properties) {
        log.info(">>>>>>>>>>> xxl-job com.smart.framework.tool.code.config init.");
        XxlJobSpringExecutor executor = new XxlJobSpringExecutor();

        executor.setAdminAddresses(properties.getAdmin().getAddresses());
        executor.setAccessToken(properties.getAdmin().getAccessToken());

        executor.setAppname(properties.getExecutor().getAppName());
        executor.setIp(properties.getExecutor().getIp());
        executor.setAddress(properties.getExecutor().getAddress());
        if (properties.getExecutor().getPort() != null) {
            executor.setPort(properties.getExecutor().getPort());
        }
        executor.setLogPath(properties.getExecutor().getLogPath());
        if (properties.getExecutor().getLogRetentionDays() != null) {
            executor.setLogRetentionDays(properties.getExecutor().getLogRetentionDays());
        }
        return executor;
    }
}
