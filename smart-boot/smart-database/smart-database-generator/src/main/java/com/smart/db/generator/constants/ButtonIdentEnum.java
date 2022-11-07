package com.smart.db.generator.constants;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * 按钮配置类型
 * @author shizhongming
 * 2021/5/11 9:23 下午
 */
public enum ButtonIdentEnum implements IEnum<String> {
    /**
     * 按钮类型
     */
    LEFT_BUTTON("10", "左侧按钮"),
    RIGHT_BUTTON("20", "右侧按钮"),
    ROW_BUTTON("30", "行按钮"),
    ;

    private final String value;

    @Getter
    private final String name;

    ButtonIdentEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }
}
