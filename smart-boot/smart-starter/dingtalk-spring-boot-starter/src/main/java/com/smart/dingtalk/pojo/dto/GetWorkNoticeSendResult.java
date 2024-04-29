package com.smart.dingtalk.pojo.dto;

import com.dingtalk.api.response.OapiMessageCorpconversationGetsendresultResponse;
import lombok.*;

import java.io.Serial;
import java.util.List;

/**
 * 获取工作通知消息的发送结果
 * @author shizhongming
 * 2024/4/29 13:50
 * @since 3.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetWorkNoticeSendResult extends AbstractDingtalkResult {
    @Serial
    private static final long serialVersionUID = -8510322509478816334L;

    /**
     * 无效的userId
     */
    private List<String> invalidUserIdList;

    /**
     * 因发送消息过于频繁或超量而被流控过滤后实际未发送的userId
     * 未被限流的接收者仍会被成功发送
     * 限流规则包括：
     * 给同一用户发相同内容消息一天仅允许一次。
     * 同一个应用给同一个用户发送消息：
     * 如果是第三方企业接入方式，给同一用户发消息一天不得超过100次。
     * 如果是企业接入方式，此上限为500。
     */
    private List<OapiMessageCorpconversationGetsendresultResponse.SendForbiddenModel> forbiddenUserIdList;

    /**
     * 发送失败的userId
     */
    private List<String> failedUserIdList;
}
