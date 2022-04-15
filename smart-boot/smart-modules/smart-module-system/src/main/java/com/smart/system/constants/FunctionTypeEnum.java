package com.smart.system.constants;

import com.smart.commons.validate.enums.IValidateEnum;
import lombok.Getter;

/**
 * @author shizhongming
 * 2020/9/22 8:56 下午
 */
public enum FunctionTypeEnum implements IValidateEnum {
    /**
     * 目录、菜单、功能
     */
    CATALOG("10"),
    MENU("20"),
    FUNCTION("30")
    ;

    @Getter
    private final String value;

    FunctionTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public Object getValidateValue() {
        return this.value;
    }
}
