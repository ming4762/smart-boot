package com.smart.crud;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.smart.crud.mybatis.plus.ShortenIdGenerator;
import com.smart.crud.plus.handlers.CreateUpdateMetaObjectHandler;
import com.smart.crud.plus.handlers.DelegateMetaObjectHandler;
import com.smart.crud.service.UserProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * @author zhongming4762
 * 2022/12/17 8:04
 */
@org.springframework.context.annotation.Configuration
public class SmartCrudAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(IdentifierGenerator.class)
    public ShortenIdGenerator shortenIdGenerator() {
        return new ShortenIdGenerator();
    }

    @Bean
    public CreateUpdateMetaObjectHandler metaObjectHandler(UserProvider userProvider) {
        return new CreateUpdateMetaObjectHandler(userProvider);
    }

    @Bean
    @Primary
    public DelegateMetaObjectHandler delegateMetaObjectHandler(List<MetaObjectHandler> metaObjectHandlerList) {
        return new DelegateMetaObjectHandler(metaObjectHandlerList);
    }
}
