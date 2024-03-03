package com.smart.cloud.message.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.smart.commons.core.constants.MapperPackageConstants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author ShiZhongMing
 * 2021/4/22 12:55
 * @since 1.0
 */
@Configuration
@MapperScan(basePackages = {
        MapperPackageConstants.MODULE_MESSAGE,
})
public class MybatisConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.message")
    @Primary
    public DataSource messageDatasource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 创建事务管理器
     * @param dataSource 数据源
     * @return DataSourceTransactionManager
     */
    @Bean("messageDataSourceTransactionManager")
    @Primary
    public DataSourceTransactionManager messageDataSourceTransactionManager(@Qualifier("messageDatasource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
