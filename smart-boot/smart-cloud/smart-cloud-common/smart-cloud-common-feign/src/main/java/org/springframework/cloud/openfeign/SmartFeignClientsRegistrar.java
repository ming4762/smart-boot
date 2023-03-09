package org.springframework.cloud.openfeign;

import com.smart.cloud.common.feign.annotation.EnableSmartFeignClients;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * feign bean注册器
 * @author zhongming4762
 * 2023/3/8
 */
public class SmartFeignClientsRegistrar extends FeignClientsRegistrar implements BeanClassLoaderAware {

    private ClassLoader beanClassLoader;

    @Override
    public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    /**
     * 重写
     * @param importingClassMetadata AnnotationMetadata
     * @return package 列表
     */
    @SuppressWarnings({"rawtypes", "SingleStatementInBlock"})
    @Override
    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(EnableSmartFeignClients.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : (Class[]) attributes.get("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }

        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        basePackages.add("com.smart.cloud.api.system.feign");
        return basePackages;
    }
}
