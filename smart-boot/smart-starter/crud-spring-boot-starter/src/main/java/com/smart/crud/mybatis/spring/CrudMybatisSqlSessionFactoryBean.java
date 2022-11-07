package com.smart.crud.mybatis.spring;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ShiZhongMing
 * 2022/6/2
 * @since 3.0.0
 */
public class CrudMybatisSqlSessionFactoryBean extends MybatisSqlSessionFactoryBean {

    private final Set<String> typeEnumsPackages;

    public CrudMybatisSqlSessionFactoryBean() {
        this.typeEnumsPackages = new HashSet<>();
    }

    public void addEnumPackage(String enumsPackage) {
        this.typeEnumsPackages.add(enumsPackage);
    }

    public void addEnumPackage(Collection<String> enumsPackages) {
        this.typeEnumsPackages.addAll(enumsPackages);
    }

    public void setTypeEnumsPackages(Set<String> typeEnumsPackages) {
        this.addEnumPackage(typeEnumsPackages);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!typeEnumsPackages.isEmpty()) {
            super.setTypeEnumsPackage(String.join(";", this.typeEnumsPackages));
        }
        super.afterPropertiesSet();
    }
}
