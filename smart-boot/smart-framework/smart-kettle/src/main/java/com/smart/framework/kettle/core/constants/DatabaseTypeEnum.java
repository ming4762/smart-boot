package com.smart.framework.kettle.core.constants;

import com.smart.framework.commons.core.constants.LabelValueEnum;
import lombok.Getter;

/**
 * 数据库类型
 * @author ShiZhongMing
 * 2021/7/15 8:30
 * @since 1.0
 */
@Getter
public enum DatabaseTypeEnum implements LabelValueEnum {
    /**
     * mysql数据库
     */
    MYSQL("MySql"),
    ORACLE("Oracle"),
    SQL_SERVER("MS SQL Server");
    private final String label;

    DatabaseTypeEnum(String label) {
        this.label = label;
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
}
