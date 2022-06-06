package com.smart.crud;

import com.smart.crud.mybatis.spring.MybatisEnumPackageBeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2022/6/6
 * @since 3.0.0
 */
@Configuration(proxyBeanMethods = false)
public class SmartCrudAutoConfiguration {

    @Bean
    public MybatisEnumPackageBeanFactoryPostProcessor mybatisEnumPackageBeanFactoryPostProcessor() {
        return new MybatisEnumPackageBeanFactoryPostProcessor();
    }
}
