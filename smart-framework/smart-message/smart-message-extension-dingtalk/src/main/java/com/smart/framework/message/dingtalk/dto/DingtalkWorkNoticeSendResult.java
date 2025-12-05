package com.smart.framework.message.dingtalk.dto;

import com.smart.module.api.message.dto.MessageSendResult;
import lombok.Getter;
import lombok.Setter;

/**
 * 钉钉工作通知发送结果
 * @author shizhongming
 * 2025/9/26 14:02
 * @since 5.0.0
 */
@Getter
@Setter
public class DingtalkWorkNoticeSendResult extends MessageSendResult {

    private String taskId;
}
