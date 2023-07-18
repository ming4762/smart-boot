package com.smart.message.manager.constants;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * 通知用户类型，10：全部用户，20：指定用户，30：业务用户
 * @author zhongming4762
 * 2023/7/6 17:13
 */
@Getter
public enum ReceiveUserTypeEnum implements IEnum<String> {

    /**
     * 通知用户类型，10：全部用户，20：指定用户，30：业务用户
     */
    ALL_USER("10", "全部用户"),
    SPECIFY_USER("20", "指定用户"),
    BUSINESS_USER("30", "业务用户")
    ;
    private final String value;

    private final String remark;

    ReceiveUserTypeEnum(String value, String remark) {
        this.value = value;
        this.remark = remark;
    }
}
