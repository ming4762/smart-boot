package com.smart.service.auth.server.sso.config;

import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceBuilder;
import com.smart.framework.commons.core.constants.MapperPackageConstants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author ShiZhongMing
 * 2021/4/22 12:55
 * @since 1.0
 */
@Configuration
@MapperScan(basePackages = {
        MapperPackageConstants.MODULE_SSO_SERVER_MANAGER,
})
@EnableTransactionManagement
public class MybatisConfig {

    @Bean
    @ConfigurationProperties("spring.datasource")
    @Primary
    public DataSource systemDatasource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 创建事务管理器
     * @param dataSource 数据源
     * @return DataSourceTransactionManager
     */
    @Bean("systemTransactionManager")
    @Primary
    public DataSourceTransactionManager systemDataSourceTransactionManager(@Qualifier("systemDatasource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
