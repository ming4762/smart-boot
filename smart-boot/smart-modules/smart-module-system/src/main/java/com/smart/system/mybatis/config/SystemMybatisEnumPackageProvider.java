package com.smart.system.mybatis.config;

import com.google.common.collect.Sets;
import com.smart.crud.mybatis.spring.MybatisEnumPackageProvider;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author ShiZhongMing
 * 2022/6/6
 * @since 3.0.0
 */
@Component
public class SystemMybatisEnumPackageProvider implements MybatisEnumPackageProvider {
    @Override
    public Set<String> provideEnumPackages() {
        return Sets.newHashSet("com.smart.system.constants", "com.smart.commons.core.log");
    }
}
