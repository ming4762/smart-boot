package com.smart.db.generator.constants;

import lombok.Getter;

/**
 * @author ShiZhongMing
 * 2021/5/8 10:26
 * @since 1.0
 */
public enum TableTypeEnum {

    /**
     * 10：单表
     * 20：主表
     * 30：附表
     */
    SINGLE("10"),
    MAIN("20"),
    ADDENDUM("30");

    @Getter
    private final String type;

    TableTypeEnum(String type) {
        this.type = type;
    }
}
