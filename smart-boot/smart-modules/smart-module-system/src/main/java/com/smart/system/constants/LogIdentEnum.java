package com.smart.system.constants;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * @author zhongming4762
 * 2023/2/6 20:15
 */
@Getter
public enum LogIdentEnum implements IEnum<String> {

    /**
     * 登录日志
     * 接口日志
     */
    LOGIN_LOG("10", "接口日志"),
    INTERFACE_LOG("20", "接口日志")
    ;

    private final String value;

    private final String remark;

    LogIdentEnum(String value, String remark) {
        this.value = value;
        this.remark = remark;
    }
}
