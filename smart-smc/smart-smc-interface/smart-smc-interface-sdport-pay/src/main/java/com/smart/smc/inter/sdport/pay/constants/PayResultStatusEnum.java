package com.smart.smc.inter.sdport.pay.constants;

import lombok.Getter;

/**
 * @author shizhongming
 * 2024/10/30 15:07
 * @since 1.0.0
 */
@Getter
public enum PayResultStatusEnum {
    /**
     * 用户不存在
     */
    USER_NOT_FOUND("RRB-01000125", "用户不存在")
    ;

    private final String code;

    private final String description;

    PayResultStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
