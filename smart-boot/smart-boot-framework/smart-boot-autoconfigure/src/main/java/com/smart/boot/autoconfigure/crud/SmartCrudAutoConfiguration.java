package com.smart.boot.autoconfigure.crud;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.smart.framework.crud.mybatis.plus.ShortenIdGenerator;
import com.smart.framework.crud.plus.fill.DelegateMetaObjectFill;
import com.smart.framework.crud.plus.fill.SmartMetaObjectFill;
import com.smart.framework.crud.plus.fill.impl.CreateDeptMetaObjectFill;
import com.smart.framework.crud.plus.fill.impl.CreateUpdateMetaObjectFill;
import com.smart.framework.crud.plus.fill.impl.LogicDeleteMetaObjectFill;
import com.smart.framework.crud.service.BaseService;
import com.smart.module.api.crud.SmartCrudUserApi;
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
     * @param smartCrudUserApi smartCrudUserApi
     * @return CreateUpdateMetaObjectFill
     */
    @Bean
    public CreateUpdateMetaObjectFill createUpdateMetaObjectFill(SmartCrudUserApi smartCrudUserApi) {
        return new CreateUpdateMetaObjectFill(smartCrudUserApi);
    }

    /**
     * 逻辑删除属性注入器
     * @param smartCrudUserApi smartCrudUserApi
     * @return LogicDeleteMetaObjectFill
     */
    @Bean
    public LogicDeleteMetaObjectFill logicDeleteMetaObjectFill(SmartCrudUserApi smartCrudUserApi) {
        return new LogicDeleteMetaObjectFill(smartCrudUserApi);
    }

    /**
     * 添加时注入部门信息
     * @param smartCrudUserApi smartCrudUserApi
     * @return CreateDeptMetaObjectFill
     */
    @Bean
    public CreateDeptMetaObjectFill createDeptMetaObjectFill(SmartCrudUserApi smartCrudUserApi) {
        return new CreateDeptMetaObjectFill(smartCrudUserApi);
    }
}
