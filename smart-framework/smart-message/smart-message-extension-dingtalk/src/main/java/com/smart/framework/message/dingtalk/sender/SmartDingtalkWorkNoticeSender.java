package com.smart.framework.message.dingtalk.sender;

import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.extension.dingtalk.DingtalkApi;
import com.smart.framework.extension.dingtalk.constants.DingtalkMessageTypeEnum;
import com.smart.framework.extension.dingtalk.pojo.dto.WorkNoticeAsyncSendResult;
import com.smart.framework.extension.dingtalk.pojo.parameter.WorkNoticeAsyncSendParameter;
import com.smart.framework.extension.dingtalk.pojo.parameter.message.MarkdownMessageParameter;
import com.smart.framework.extension.dingtalk.pojo.parameter.message.TextMessageParameter;
import com.smart.framework.message.core.constants.SmartMessageChannelType1Enum;
import com.smart.framework.message.core.constants.SmartMessageChannelType2Enum;
import com.smart.framework.message.core.pojo.dto.SmartMessageToUserDTO;
import com.smart.framework.message.core.service.SmartMessageSender;
import com.smart.framework.message.dingtalk.SmartMessageDingtalkChannelProperties;
import com.smart.framework.message.dingtalk.dto.DingtalkWorkNoticeSendResult;
import com.smart.module.api.message.dto.MessageSendResult;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 钉钉工作通知
 * @author shizhongming
 * 2024/4/26 17:14
 * @since 3.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class SmartDingtalkWorkNoticeSender implements SmartMessageSender {

    private final DingtalkApi dingtalkApi;

    /**
     * 获取支持的一级通道信息
     *
     * @return 支持的消息通道
     */
    @NonNull
    @Override
    public SmartMessageChannelType1Enum supportChannel1() {
        return SmartMessageChannelType1Enum.DINGTALK;
    }

    /**
     * 获取支持的二级通道信息
     *
     * @return 支持的消息通道
     */
    @Nullable
    @Override
    public SmartMessageChannelType2Enum supportChannel2() {
        return SmartMessageChannelType2Enum.DINGTALK_WORK_NOTICE;
    }

    /**
     * 发送消息
     *
     * @param channelProperties 通道参数
     * @param toUserList        用户列表
     * @param parameter         消息发送参数
     * @return 消息发送结果
     */
    @Override
    public MessageSendResult send(@Nullable String channelProperties, List<SmartMessageToUserDTO> toUserList, RemoteMessageSendParameter parameter) {
        SmartMessageDingtalkChannelProperties properties = JsonUtils.parse(channelProperties, SmartMessageDingtalkChannelProperties.class);
        // 切换到指定应用
        dingtalkApi.switchover(properties.getAppKey());

        String noMobileUsers = toUserList.stream()
                .filter(item -> !StringUtils.hasText(item.getMobile()))
                .map(SmartMessageToUserDTO::getFullName)
                .collect(Collectors.joining(","));
        if (StringUtils.hasText(noMobileUsers)) {
            log.warn("【{}】没有设置手机号，无法发送钉钉消息", noMobileUsers);
        }
        List<String> userIdList = toUserList.stream()
                .filter(item -> StringUtils.hasText(item.getMobile()))
                .map(item -> dingtalkApi.userApi().getByMobile(item.getMobile()).getUserId())
                .toList();
        if (CollectionUtils.isEmpty(userIdList)) {
            log.warn("没有需要发送的用户");
            return null;
        }
        boolean isMarkdown = this.isMarkdown(parameter);
        WorkNoticeAsyncSendParameter sendParameter = WorkNoticeAsyncSendParameter.builder()
                .agentId(properties.getAgentId())
                .userIdList(userIdList)
                // 只支持markdown和text消息类型
                .messageType(isMarkdown ? DingtalkMessageTypeEnum.MARKDOWN :DingtalkMessageTypeEnum.TEXT)
                .messageParameter(
                        isMarkdown ? new MarkdownMessageParameter(parameter.getTitle(), parameter.getContent())
                                : new TextMessageParameter(parameter.getContent())
                        )
                .build();
        WorkNoticeAsyncSendResult result = this.dingtalkApi.workNoticeApi().syncSend(sendParameter);
        DingtalkWorkNoticeSendResult sendResult = new DingtalkWorkNoticeSendResult();
        sendResult.setTaskId(result.getTaskId());
        return sendResult;
    }
}
