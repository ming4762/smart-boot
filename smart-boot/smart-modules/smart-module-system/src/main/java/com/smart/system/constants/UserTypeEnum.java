package com.smart.system.constants;

import com.smart.commons.validate.enums.IValidateEnum;
import lombok.Getter;

/**
 * @author shizhongming
 * 2020/6/2 4:51 下午
 */
@Getter
public enum UserTypeEnum implements IValidateEnum {

    /**
     * 系统用户
     */
    SYSTEM_USER("10", "系统用户"),
    BUSINESS_USER("20", "业务用户");

    private final String value;

    private final String name;

    UserTypeEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public Object getValidateValue() {
        return this.value;
    }
}
