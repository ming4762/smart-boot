package com.smart.module.system.constants;

import com.smart.framework.commons.core.constants.LabelValueEnum;

/**
 * 租户数据隔离级别
 * @author shizhongming
 * 2024/3/29 13:43
 * @since 3.0.0
 */
public enum SysTenantIsolationStrategyEnum implements LabelValueEnum {

    FIELD("字段隔离"),
    TABLE("表隔离"),
    DATABASE("数据库隔离")
    ;

    private final String remark;

    SysTenantIsolationStrategyEnum(String remark) {
        this.remark = remark;
    }

    /**
     * 获取value
     *
     * @return value
     */
    @Override
    public String getValue() {
        return this.name();
    }

    /**
     * 获取label
     *
     * @return label
     */
    @Override
    public String getLabel() {
        return this.remark;
    }
}
