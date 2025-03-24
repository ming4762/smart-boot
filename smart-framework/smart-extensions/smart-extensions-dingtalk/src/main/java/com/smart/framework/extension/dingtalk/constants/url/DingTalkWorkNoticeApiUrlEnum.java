package com.smart.framework.extension.dingtalk.constants.url;

import lombok.Getter;

/**
 * @author shizhongming
 * 2024/4/29 11:23
 * @since 3.0.0
 */
@Getter
public enum DingTalkWorkNoticeApiUrlEnum implements DingTalkApiUrl {
    /**
     * 钉钉工作通知
     */
    ASYNC_SEND("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2", "钉钉工作通知"),
    RECALL("https://oapi.dingtalk.com/topapi/message/corpconversation/recall", "撤回工作通知消息"),
    GET_SEND_RESULT("https://oapi.dingtalk.com/topapi/message/corpconversation/getsendresult", "获取工作通知消息的发送结果"),
    ;

    private final String url;

    private final String remark;

    DingTalkWorkNoticeApiUrlEnum(String url, String remark) {
        this.url = url;
        this.remark = remark;
    }
}
