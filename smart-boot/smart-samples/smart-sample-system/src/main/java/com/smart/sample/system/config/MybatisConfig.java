package com.smart.sample.system.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.github.pagehelper.PageInterceptor;
import com.google.common.collect.Lists;
import com.smart.commons.core.constants.MapperPackageConstants;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * @author ShiZhongMing
 * 2021/4/22 12:55
 * @since 1.0
 */
@Configuration
@MapperScan(basePackages = {
        MapperPackageConstants.MODULE_SYSTEM,
        MapperPackageConstants.DATABASE_GENERATOR
}, sqlSessionTemplateRef = "systemSqlSessionTemplate")
public class MybatisConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.system")
    public DataSource systemDatasource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 创建session工厂
     * @param dataSource 数据源
     * @return session工厂
     * @throws Exception Exception
     */
    @Bean("systemSqlSessionFactory")
    public SqlSessionFactory systemSqlSessionFactory(@Qualifier("systemDatasource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setDataSource(dataSource);
        mybatisSqlSessionFactoryBean.setMapperLocations(matchMapperLocations());
        mybatisSqlSessionFactoryBean.setPlugins(this.createPageHelperPlugins());

        // 设置通用枚举扫描路径
        List<String> enumPackageList = Lists.newArrayList();
        // 系统模块枚举路径
        // 代码生成器模块枚举路径
        mybatisSqlSessionFactoryBean.setTypeEnumsPackage(
                String.join(";", enumPackageList)
        );
        return mybatisSqlSessionFactoryBean.getObject();
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

    @Bean(name = "systemSqlSessionTemplate")
    public SqlSessionTemplate systemSqlSessionTemplate(@Qualifier("systemSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    private Resource[] matchMapperLocations() throws IOException {
        return new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/**/*.xml");
    }

    private Interceptor createPageHelperPlugins() {
        final PageInterceptor interceptor = new PageInterceptor();
        final Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        interceptor.setProperties(properties);
        return interceptor;
    }
}
