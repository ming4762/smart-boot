package com.smart.starter.xxl.config;

import com.smart.starter.xxl.SmartXxlExecutorProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author shizhongming
 * 2024/6/26 20:52
 * @since 3.0.0
 */
@Slf4j
public class SmartXxlExecutorConfig {

    @Bean
    @ConditionalOnMissingBean
    public XxlJobSpringExecutor xxlJobSpringExecutor(SmartXxlExecutorProperties properties) {
        log.info(">>>>>>>>>>> xxl-job config init.");
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
