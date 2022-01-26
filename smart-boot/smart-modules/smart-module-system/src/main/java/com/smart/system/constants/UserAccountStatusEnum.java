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
public enum UserAccountStatusEnum implements IEnum<String> {

    /**
     * 10:正常
     * 20:锁定
     */
    NORMAL("10"),
    /**
     * 多次登录失败锁定
     */
    LOGIN_FAIL_LOCKED("20"),
    /**
     * 超出指定时间未登录锁定
     */
    LONG_TIME_LOCKED("30")
    ;

    @EnumValue
    private final String value;

    UserAccountStatusEnum(String value) {
        this.value = value;
    }
}
