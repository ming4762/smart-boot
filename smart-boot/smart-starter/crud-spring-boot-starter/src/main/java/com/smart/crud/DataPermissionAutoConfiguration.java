package com.smart.crud;

import com.smart.crud.datapermission.DataPermissionExecutorInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 数据权限自动配置类
 * @author ShiZhongMing
 * 2022/10/17
 * @since 1.0
 */
@Configuration
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class DataPermissionAutoConfiguration implements InitializingBean {

    private final List<SqlSessionFactory> sqlSessionFactoryList;

    public DataPermissionAutoConfiguration(List<SqlSessionFactory> sqlSessionFactoryList) {
        this.sqlSessionFactoryList = sqlSessionFactoryList;
    }

    @Override
    public void afterPropertiesSet() {
        DataPermissionExecutorInterceptor interceptor = new DataPermissionExecutorInterceptor();

        for (SqlSessionFactory factory : this.sqlSessionFactoryList) {
            org.apache.ibatis.session.Configuration configuration = factory.getConfiguration();
            if (!this.containsInterceptor(configuration, interceptor)) {
                configuration.addInterceptor(interceptor);
            }
        }
    }

    private boolean containsInterceptor(org.apache.ibatis.session.Configuration configuration, Interceptor interceptor) {
        try {
            return configuration.getInterceptors().contains(interceptor);
        } catch (Exception e) {
            return false;
        }
    }
}
