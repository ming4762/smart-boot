package com.smart.commons.core.beans;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.lang.NonNull;

/**
 * @author ShiZhongMing
 * 2021/3/8 17:29
 * @since 1.0
 */
public abstract class AbstractBeanNameProvider implements BeanNameAware, BeanNameProvider {

    private String beanName;

    /**
     * 获取bean名字
     * @return bean名字
     */
    @Override
    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public void setBeanName(@NonNull String name) {
        this.beanName = name;
    }
}
