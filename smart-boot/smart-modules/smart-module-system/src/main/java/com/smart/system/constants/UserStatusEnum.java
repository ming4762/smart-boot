package com.smart.system.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * 用户状态枚举
 * @author ShiZhongMing
 * 2021/12/30
 * @since 1.0.7
 */
@Getter
public enum UserStatusEnum implements IEnum<String> {

    /**
     * 10:正常
     * 20:锁定
     */
    NORMAL("10"),
    LOCKED("20")
    ;

    @EnumValue
    private final String value;

    UserStatusEnum(String value) {
        this.value = value;
    }
}
