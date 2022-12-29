package com.smart.crud;

import com.google.common.collect.ImmutableList;
import com.smart.crud.mybatis.plugin.CreateUpdateUserTimeMybatisInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author zhongming4762
 * 2022/12/17 8:04
 */
@AutoConfigureAfter({MybatisAutoConfiguration.class, CrudMybatisInterceptorAutoConfiguration.class})
@org.springframework.context.annotation.Configuration
public class SmartCrudAutoConfiguration implements InitializingBean, ApplicationContextAware {

    private static final List<Class<? extends Interceptor>> INTERCEPTOR_CLASS_LIST = ImmutableList.of(CreateUpdateUserTimeMybatisInterceptor.class);

    private final List<SqlSessionFactory> sqlSessionFactoryList;

    private ApplicationContext applicationContext;

    public SmartCrudAutoConfiguration(List<SqlSessionFactory> sqlSessionFactoryList) {
        this.sqlSessionFactoryList = sqlSessionFactoryList;
    }

    @Override
    public void afterPropertiesSet() {
        this.initInterceptor();
    }

    private void initInterceptor() {
        if (CollectionUtils.isEmpty(this.sqlSessionFactoryList)) {
            return;
        }
        // 获取所有拦截器
        INTERCEPTOR_CLASS_LIST.stream()
                .map(item -> this.applicationContext.getBean(item))
                .forEach(item -> this.sqlSessionFactoryList.forEach(sqlSessionFactory -> {
                    Configuration configuration = sqlSessionFactory.getConfiguration();
                    if (!this.containsInterceptor(configuration, item)) {
                        configuration.addInterceptor(item);
                    }
                }));
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private boolean containsInterceptor(Configuration configuration, Interceptor interceptor) {
        try {
            return configuration.getInterceptors().contains(interceptor);
        } catch (Exception e) {
            return false;
        }
    }
}