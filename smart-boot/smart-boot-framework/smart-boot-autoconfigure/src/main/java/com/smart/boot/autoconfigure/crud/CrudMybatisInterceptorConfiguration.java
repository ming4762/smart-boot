package com.smart.boot.autoconfigure.crud;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.smart.framework.crud.plus.inner.LogicDeleteFieldInjectInnerInterceptor;
import com.smart.framework.crud.service.UserProvider;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author zhongming4762
 * 2022/12/17 7:58
 */
@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
public class CrudMybatisInterceptorConfiguration {

    @Bean
    public LogicDeleteFieldInjectInnerInterceptor logicDeleteFieldInjectInnerInterceptor(UserProvider userProvider) {
        return new LogicDeleteFieldInjectInnerInterceptor(userProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor(List<InnerInterceptor> innerInterceptorList) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        innerInterceptorList.forEach(interceptor::addInnerInterceptor);
        return interceptor;
    }

}
