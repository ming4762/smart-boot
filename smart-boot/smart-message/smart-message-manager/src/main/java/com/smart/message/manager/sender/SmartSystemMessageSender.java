package com.smart.message.manager.sender;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.message.core.constants.SmartMessageChannelType1Enum;
import com.message.core.pojo.dto.SmartMessageToUserDTO;
import com.message.core.service.SmartMessageSender;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.utils.SmartIdGenerator;
import com.smart.message.manager.constants.MessageSendStatusEnum;
import com.smart.message.manager.constants.MessageTypeEnum;
import com.smart.message.manager.model.SmartMessageSystemPO;
import com.smart.message.manager.model.SmartMessageSystemSendPO;
import com.smart.message.manager.service.SmartMessageSystemSendService;
import com.smart.message.manager.service.SmartMessageSystemService;
import com.smart.module.api.message.dto.MessageSendDTO;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统消息发送器
 * @author zhongming4762
 * 2023/7/14 18:26
 */
@Component
public class SmartSystemMessageSender implements SmartMessageSender {

    private final SmartMessageSystemSendService smartMessageSystemSendService;

    private final SmartMessageSystemService smartMessageSystemService;

    public SmartSystemMessageSender(SmartMessageSystemSendService smartMessageSystemSendService, SmartMessageSystemService smartMessageSystemService) {
        this.smartMessageSystemSendService = smartMessageSystemSendService;
        this.smartMessageSystemService = smartMessageSystemService;
    }

    /**
     * 获取支持的一级通道信息
     *
     * @return 支持的消息通道
     */
    @NonNull
    @Override
    public SmartMessageChannelType1Enum supportChannel1() {
        return SmartMessageChannelType1Enum.SYSTEM;
    }


    /**
     * 发送消息
     *
     * @param parameter 消息发送参数
     * @return 消息发送结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessageSendDTO send(@Nullable String channelProperties, List<SmartMessageToUserDTO> toUserList, RemoteMessageSendParameter parameter) {
        List<Long> userIdList = toUserList.stream()
                .map(SmartMessageToUserDTO::getUserId)
                .toList();
        SmartMessageSystemPO model = new SmartMessageSystemPO();
        boolean isAdd = true;
        if (parameter.getMessageId() != null) {
            SmartMessageSystemPO smartMessageSystem = this.smartMessageSystemService.getOne(
                    new QueryWrapper<SmartMessageSystemPO>().lambda()
                            .select(SmartMessageSystemPO::getId)
                            .eq(SmartMessageSystemPO::getId, parameter.getMessageId())
            );
            if (smartMessageSystem != null) {
                isAdd = false;
            }
        }
        Long messageId = isAdd ? SmartIdGenerator.nextId() : parameter.getMessageId();
        if (isAdd) {
            model.setId(messageId);
            model.setTitle(parameter.getTitle());
            model.setContent(parameter.getContent());
            model.setMessageType(MessageTypeEnum.SYSTEM_MESSAGE);
            model.setSendStatus(MessageSendStatusEnum.SEND);
            model.setPriority(parameter.getPriority());
            model.setSendTime(LocalDateTime.now());

            model.setUserIds(userIdList);
            if (parameter.getBusiness() != null) {
                model.setBusinessIdent(parameter.getBusiness().getBusinessIdent());
                model.setBusinessId(parameter.getBusiness().getBusinessId());
                model.setBusinessData(parameter.getBusiness().getBusinessData());
            }
            this.smartMessageSystemService.save(model);
        } else {
            LambdaUpdateWrapper<SmartMessageSystemPO> updateWrapper = new UpdateWrapper<SmartMessageSystemPO>().lambda()
                    .set(SmartMessageSystemPO::getSendStatus, MessageSendStatusEnum.SEND.getValue())
                    .set(SmartMessageSystemPO::getSendUserId, AuthUtils.getNonNullCurrentUserId())
                    .set(SmartMessageSystemPO::getSendTime, LocalDateTime.now())
                    // TODO:待完善 使用公共函数转
                    .set(SmartMessageSystemPO::getUserIds, String.join(",", userIdList.stream().map(Object::toString).toList()));
            // 更新消息表
            this.smartMessageSystemService.update(updateWrapper);
        }
        // 保存阅读记录表
        List<SmartMessageSystemSendPO> messageSendList = userIdList.stream()
                .map(userId -> {
                    SmartMessageSystemSendPO messageSystemSend = new SmartMessageSystemSendPO();
                    messageSystemSend.setMessageId(messageId);
                    messageSystemSend.setUserId(userId);
                    return messageSystemSend;
                }).toList();
        this.smartMessageSystemSendService.saveBatch(messageSendList);
        MessageSendDTO result = new MessageSendDTO();
        result.setSystemMessageSendResult(new MessageSendDTO.SystemMessageSendDTO(messageId));
        return result;
    }
}
