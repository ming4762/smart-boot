package com.smart.crud.mybatis.spring;

import java.util.Set;

/**
 * @author ShiZhongMing
 * 2022/6/6
 * @since 3.0.0
 */
public interface MybatisEnumPackageProvider {

    /**
     * 获取枚举类包
     * @return 枚举类包
     */
    Set<String> provideEnumPackages();
}
