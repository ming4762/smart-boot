package com.smart.db.analysis;

import com.smart.db.analysis.converter.*;
import com.smart.db.analysis.executor.DbExecutorProvider;
import com.smart.db.analysis.executor.MysqlDatabaseExecutor;
import com.smart.db.analysis.executor.OracleDatabaseExecutor;
import com.smart.db.analysis.executor.SqlServerDatabaseExecutor;
import com.smart.db.analysis.pool.DbConnectionProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2020/7/29 8:45 下午
 */
@Configuration
public class DatabaseMessageAutoConfiguration {

    /**
     * 创建默认的 converterProvider
     * @return converterProvider
     */
    @Bean
    @ConditionalOnMissingBean(ConverterProvider.class)
    public ConverterProvider converterProvider() {
        return new DefaultConverterProvider();
    }

    @Bean
    public ConverterInitializer converterInitializer(ConverterProvider converterProvider) {
        return new ConverterInitializer(converterProvider);
    }

    /**
     * 创建默认的DbJavaTypeConverter
     * @return DbJavaTypeConverter
     */
    @Bean
    @ConditionalOnMissingBean(DbJavaTypeConverter.class)
    public DbJavaTypeConverter dbJavaTypeConverter() {
        return new DefaultDbJavaTypeConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public MysqlDatabaseExecutor mysqlDatabaseExecutor(DbJavaTypeConverter dbJavaTypeConverter) {
        return new MysqlDatabaseExecutor(dbJavaTypeConverter);
    }

    @Bean
    @ConditionalOnMissingBean
    public OracleDatabaseExecutor oracleDatabaseExecutor(DbJavaTypeConverter dbJavaTypeConverter) {
        return new OracleDatabaseExecutor(dbJavaTypeConverter);
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlServerDatabaseExecutor sqlServerDatabaseExecutor(DbJavaTypeConverter dbJavaTypeConverter) {
        return new SqlServerDatabaseExecutor(dbJavaTypeConverter);
    }

    /**
     * 创建 DbExecutorProvider
     * @param applicationContext ApplicationContext
     * @return DbExecutorProvider
     */
    @Bean
    public DbExecutorProvider dbExecutorProvider(ApplicationContext applicationContext) {
        return new DbExecutorProvider(applicationContext);
    }

    /**
     * 创建数据库连接提供器
     * @return DbConnectionProvider
     */
    @Bean
    public DbConnectionProvider dbConnectionProvider() {
        return new DbConnectionProvider();
    }
}
