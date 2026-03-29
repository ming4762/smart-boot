package com.smart.boot.crud;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.smart.framework.crud.plus.inner.LogicDeleteFieldInjectInnerInterceptor;
import com.smart.module.api.crud.SmartCrudUserApi;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * @author zhongming4762
 * 2022/12/17 7:58
 */
@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
public class CrudMybatisInterceptorConfiguration {

    @Bean
    @Order(0)
    public LogicDeleteFieldInjectInnerInterceptor logicDeleteFieldInjectInnerInterceptor(ObjectProvider<SmartCrudUserApi> smartCrudUserApi) {
        return new LogicDeleteFieldInjectInnerInterceptor(smartCrudUserApi);
    }

    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor(List<InnerInterceptor> innerInterceptorList) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        innerInterceptorList.forEach(interceptor::addInnerInterceptor);
        return interceptor;
    }

}
