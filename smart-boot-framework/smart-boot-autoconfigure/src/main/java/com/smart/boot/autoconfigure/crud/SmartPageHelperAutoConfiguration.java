package com.smart.boot.autoconfigure.crud;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInterceptor;
import com.github.pagehelper.autoconfigure.PageHelperStandardProperties;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author shizhongming
 * 2025/3/20 11:46
 * @since 5.0.0
 */
@Configuration
@ConditionalOnClass(PageHelper.class)
@ConditionalOnBean({SqlSessionFactory.class, PageHelperStandardProperties.class})
public class SmartPageHelperAutoConfiguration {

    /**
     * 确保数据权限或其他 MyBatis‑Plus 拦截器能够在分页逻辑之前完成对 SQL 的处理，从而避免分页 count 查询等问题
     * @param standardProperties standardProperties
     * @return PageInterceptor
     */
    @ConditionalOnMissingBean(PageHelper.class)
    @Bean
    @Order(Integer.MIN_VALUE)
    public PageInterceptor pageInterceptor(PageHelperStandardProperties standardProperties) {
        PageInterceptor pageInterceptor = new PageInterceptor();
        pageInterceptor.setProperties(standardProperties.getProperties());
        return pageInterceptor;
    }
}
