package com.smart.crud.mybatis.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2022/6/6 13:09
 * @since 1.0
 */
public class MybatisEnumPackageBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Set<String> enumPackages = Arrays.stream(beanFactory.getBeanNamesForType(MybatisEnumPackageProvider.class))
                .map(item -> beanFactory.getBean(item, MybatisEnumPackageProvider.class))
                .flatMap(item -> item.provideEnumPackages().stream())
                .collect(Collectors.toSet());
        String[] names = beanFactory.getBeanNamesForType(CrudMybatisSqlSessionFactoryBean.class);
        if (names.length > 0) {
            String name = names[0];
            if (name.startsWith(BeanFactory.FACTORY_BEAN_PREFIX)) {
                name = name.substring(1);
            }
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
            beanDefinition.getPropertyValues().add("typeEnumsPackages", enumPackages);
        }
    }
}
