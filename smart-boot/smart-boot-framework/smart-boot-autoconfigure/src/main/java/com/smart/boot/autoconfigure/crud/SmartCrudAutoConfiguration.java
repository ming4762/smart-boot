package com.smart.boot.autoconfigure.crud;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.smart.framework.crud.mybatis.plus.ShortenIdGenerator;
import com.smart.framework.crud.plus.fill.DelegateMetaObjectFill;
import com.smart.framework.crud.plus.fill.SmartMetaObjectFill;
import com.smart.framework.crud.plus.fill.impl.CreateDeptMetaObjectFill;
import com.smart.framework.crud.plus.fill.impl.CreateUpdateMetaObjectFill;
import com.smart.framework.crud.plus.fill.impl.LogicDeleteMetaObjectFill;
import com.smart.framework.crud.service.BaseService;
import com.smart.framework.crud.service.UserProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * @author zhongming4762
 * 2022/12/17 8:04
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(BaseService.class)
@Import({
        CrudMybatisInterceptorConfiguration.class,
        MybatisInterceptorAddConfiguration.class,
        MybatisPlusEnhanceConfiguration.class,
        SmartDesensitizeAutoConfiguration.class,
        SmartDataPermissionAutoConfiguration.class
})
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

    /**
     * 添加时注入部门信息
     * @param userProvider userProvider
     * @return CreateDeptMetaObjectFill
     */
    @Bean
    public CreateDeptMetaObjectFill createDeptMetaObjectFill(UserProvider userProvider) {
        return new CreateDeptMetaObjectFill(userProvider);
    }
}
