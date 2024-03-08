package com.smart.auth.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.DefaultSecurityFilterChain;

import java.util.Objects;
import java.util.Optional;

/**
 * @author shizhongming
 * 2023/10/25 15:41
 * @since 3.0.0
 */
@Slf4j
public class SmartSecurityConfigurerAdapter<H extends HttpSecurityBuilder<H>> extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, H> {

    /**
     * 从容器中获取类
     * @param clazz 类型class
     * @param t 优先选中的值
     * @return T
     * @param <T> T
     */
    protected <T> T getBean(Class<T> clazz, T t) {
        if (Objects.nonNull(t)) {
            return t;
        }
        ApplicationContext applicationContext = this.getBuilder().getSharedObject(ApplicationContext.class);
        try {
            return Optional.ofNullable(applicationContext).map(item -> item.getBean(clazz)).orElse(null);
        } catch (NoSuchBeanDefinitionException e) {
            log.warn("获取bean发生错误: " + e.getMessage());
            return null;
        }
    }

    protected <T> T getBean(Class<T> clazz) {
        return this.getBean(clazz, null);
    }
}
