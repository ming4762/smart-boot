package com.smart.commons.core.beans;

/**
 * spring bean name提供器
 * @author ShiZhongMing
 * 2021/3/8 17:48
 * @since 1.0
 */
public interface BeanNameProvider {

    /**
     * 获取bean名字
     * @return beanName
     */
    String getBeanName();
}
