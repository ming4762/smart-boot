package com.smart.framework.extension.dingtalk.constants.url;

import lombok.Getter;

/**
 * @author shizhongming
 * 2024/4/29 11:23
 * @since 3.0.0
 */
@Getter
public enum DingTalkUserApiUrlEnum implements DingTalkApiUrl {
    /**
     * 用户接口地址
     */
    GET_BY_MOBILE("https://oapi.dingtalk.com/topapi/v2/user/getbymobile", "根据手机号获取用户信息")
    ;

    private final String url;

    private final String remark;

    DingTalkUserApiUrlEnum(String url, String remark) {
        this.url = url;
        this.remark = remark;
    }
}
