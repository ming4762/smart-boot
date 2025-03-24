package com.smart.converter.office.constants;

import com.jacob.activeX.ActiveXComponent;

/**
 * OFFICE APP 枚举
 * @author ShiZhongMing
 * 2021/8/25 16:09
 * @since 1.0
 */
public enum OfficeAppConstants {

    /**
     * OFFICE APP 枚举
     */
    EXCEL("Excel.Application"),
    WORD("Word.Application"),
    POWER_POINT("PowerPoint.Application")
    ;

    /**
     * 创建APP
     * @return APP对象
     */
    public ActiveXComponent createApp() {
        return new ActiveXComponent(this.value);
    }

    private final String value;


    OfficeAppConstants(String value) {
        this.value = value;
    }
}
