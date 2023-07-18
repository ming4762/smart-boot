package com.smart.message.manager.sender;

import com.message.core.service.SmartMessageSender;
import com.smart.commons.core.utils.IdGenerator;
import com.smart.message.manager.constants.MessagePriorityEnum;
import com.smart.message.manager.constants.MessageSendStatusEnum;
import com.smart.message.manager.constants.MessageTypeEnum;
import com.smart.message.manager.model.SmartMessageSystemPO;
import com.smart.message.manager.model.SmartMessageSystemSendPO;
import com.smart.message.manager.service.SmartMessageSystemSendService;
import com.smart.message.manager.service.SmartMessageSystemService;
import com.smart.module.api.message.constants.MessageChannelEnum;
import com.smart.module.api.message.dto.MessageSendDTO;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
     * 获取支持的通道信息
     *
     * @return 支持的消息通道
     */
    @Override
    public MessageChannelEnum supportChannel() {
        return MessageChannelEnum.SYSTEM;
    }

    /**
     * 发送消息
     *
     * @param parameter 消息发送参数
     * @return 消息发送结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MessageSendDTO send(RemoteMessageSendParameter parameter) {
        SmartMessageSystemPO model = new SmartMessageSystemPO();
        Long messageId = IdGenerator.nextId();
        model.setId(messageId);
        model.setTitle(parameter.getTitle());
        model.setContent(parameter.getContent());
        model.setMessageType(MessageTypeEnum.SYSTEM_MESSAGE);
        model.setSendStatus(MessageSendStatusEnum.SEND);
        model.setPriority(MessagePriorityEnum.MIDDLE);
        model.setSendTime(LocalDateTime.now());

        model.setUserIds(new ArrayList<>(parameter.getToUserIds()));
        if (parameter.getBusiness() != null) {
            model.setBusinessIdent(parameter.getBusiness().getBusinessIdent());
            model.setBusinessId(parameter.getBusiness().getBusinessId());
            model.setBusinessData(parameter.getBusiness().getBusinessData());
        }
        this.smartMessageSystemService.save(model);

        // 保存阅读记录表
        List<SmartMessageSystemSendPO> messageSendList = parameter.getToUserIds().stream()
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
