package com.smart.boot.autoconfigure.common;

import com.smart.framework.commons.core.utils.snowflake.DefaultSnowflakeWorkIdAllocator;
import com.smart.framework.commons.core.utils.snowflake.SnowflakeWorkIdAllocator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 雪花算法工作ID分配器配置类
 * @author shizhongming
 * 2025/9/30 15:21
 * @since 5.0.0
 */
@EnableConfigurationProperties(SmartWorkIdProperties.class)
@Configuration(proxyBeanMethods = false)
public class SnowflakeWorkIdAllocatorConfiguration {

    /**
     * 雪花算法工作ID分配器
     * @return SnowflakeWorkIdAllocator
     */
    @Bean
    @ConditionalOnMissingBean
    public SnowflakeWorkIdAllocator snowflakeWorkIdAllocator() {
        return new DefaultSnowflakeWorkIdAllocator();
    }
}
