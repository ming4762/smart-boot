package com.smart.dingtalk.constants;

import lombok.Getter;

/**
 * 钉钉接口URL枚举
 * @author shizhongming
 * 2024/4/26 21:11
 * @since 3.0.0
 */
@Getter
public enum DingtalkApiUrlEnum {

    /**
     * 钉钉工作通知
     */
    WORK_NOTICE("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2", "钉钉工作通知"),
    GET_USER_BY_MOBILE("https://oapi.dingtalk.com/topapi/v2/user/getbymobile", "根据手机号获取用户信息")
    ;

    private final String url;

    private final String remark;

    DingtalkApiUrlEnum(String url, String remark) {
        this.url = url;
        this.remark = remark;
    }
}
