package com.smart.crud;

import com.smart.crud.mybatis.plugin.CreateUpdateUserTimeMybatisInterceptor;
import com.smart.crud.service.UserProvider;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongming4762
 * 2022/12/17 7:58
 */
@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
public class CrudMybatisInterceptorAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public CreateUpdateUserTimeMybatisInterceptor createUpdateUserTimeMybatisInterceptor(UserProvider userProvider) {
        return new CreateUpdateUserTimeMybatisInterceptor(userProvider);
    }

}
