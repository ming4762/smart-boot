package com.smart.framework.message.dingtalk.constants;

import lombok.Getter;

/**
 * dingding api url 枚举
 * @author shizhongming
 * 2024/4/26 17:18
 * @since 3.0.0
 */
@Getter
public enum DingdingApiUrlEnum {

    /**
     * 钉钉工作通知
     */
    WORK_NOTICE("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2", "钉钉工作通知"),
    ;

    private final String url;

    private final String remark;

    DingdingApiUrlEnum(String url, String remark) {
        this.url = url;
        this.remark = remark;
    }
}
