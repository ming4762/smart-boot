package com.smart.framework.crud.desensitization;

import com.smart.framework.crud.desensitization.handler.*;
import lombok.Getter;

/**
 * 脱敏类型
 * @author shizhongming
 * 2025/1/9 20:32
 * @since 5.0.0
 */
@Getter
public enum DesensitizeType {
    /**
     * 脱敏类型
     */
    ENCODE(EncodeDesensitizeHandler.class, "转码"),
    ID_CARD(IdCardDesensitizeHandler.class, "身份证"),
    PHONE(PhoneDesensitizeHandler.class, "手机号"),
    EMAIL(EmailDesensitizeHandler.class, "邮箱"),
    BANK_CARD(BankCardDesensitizeHandler.class, "银行卡"),
    ;

    private final Class<? extends DesensitizeHandler> handler;

    private final String remark;

    DesensitizeType(Class<? extends DesensitizeHandler> handler, String remark) {
        this.remark = remark;
        this.handler = handler;
    }
}
