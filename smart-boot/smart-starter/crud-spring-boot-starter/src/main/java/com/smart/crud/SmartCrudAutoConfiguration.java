package com.smart.crud;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.smart.crud.mybatis.plus.ShortenIdGenerator;
import com.smart.crud.plus.configuration.LanguageDriverChangeConfigurationCustomizer;
import com.smart.crud.plus.fill.DelegateMetaObjectFill;
import com.smart.crud.plus.fill.SmartMetaObjectFill;
import com.smart.crud.plus.fill.impl.CreateUpdateMetaObjectFill;
import com.smart.crud.plus.fill.impl.LogicDeleteMetaObjectFill;
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
    public LanguageDriverChangeConfigurationCustomizer languageDriverChangeConfigurationCustomizer() {
        return new LanguageDriverChangeConfigurationCustomizer();
    }

    /**
     * 代理注入器
     * @param metaObjectHandlerList 其他注入器
     * @return DelegateMetaObjectFill
     */
    @Bean
    @Primary
    public DelegateMetaObjectFill delegateMetaObjectHandler(List<SmartMetaObjectFill> metaObjectHandlerList) {
        return new DelegateMetaObjectFill(metaObjectHandlerList);
    }

    /**
     * 添加修改注入器
     * @param userProvider userProvider
     * @return CreateUpdateMetaObjectFill
     */
    @Bean
    public CreateUpdateMetaObjectFill createUpdateMetaObjectFill(UserProvider userProvider) {
        return new CreateUpdateMetaObjectFill(userProvider);
    }

    /**
     * 逻辑删除属性注入器
     * @param userProvider userProvider
     * @return LogicDeleteMetaObjectFill
     */
    @Bean
    public LogicDeleteMetaObjectFill logicDeleteMetaObjectFill(UserProvider userProvider) {
        return new LogicDeleteMetaObjectFill(userProvider);
    }
}
