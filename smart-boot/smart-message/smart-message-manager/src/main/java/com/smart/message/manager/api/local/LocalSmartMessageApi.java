package com.smart.message.manager.api.local;

import com.message.core.event.SmartMessageSendEvent;
import com.message.core.exception.SmartMessageException;
import com.message.core.parameter.SmsSendParameter;
import com.message.core.service.SmartMessageSender;
import com.message.core.service.SmartSmsService;
import com.smart.module.api.message.SmartMessageApi;
import com.smart.module.api.message.constants.MessageChannelEnum;
import com.smart.module.api.message.dto.MessageSendDTO;
import com.smart.module.api.message.dto.SmsSendDTO;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;
import com.smart.module.api.message.parameter.RemoteSmsSendParameter;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 本地消息接口
 * @author zhongming4762
 * 2023/6/6
 */
@Component
@Primary
public class LocalSmartMessageApi implements SmartMessageApi {

    private final SmartSmsService smartSmsService;

    private final ApplicationContext applicationContext;

    /**
     * 消息发送器
     */
    private final Map<MessageChannelEnum, List<SmartMessageSender>> smartMessageSenderMap;

    public LocalSmartMessageApi(SmartSmsService smartSmsService, List<SmartMessageSender> smartMessageSenderList, ApplicationContext applicationContext) {
        this.smartSmsService = smartSmsService;
        this.smartMessageSenderMap = smartMessageSenderList.stream()
                .collect(Collectors.groupingBy(SmartMessageSender::supportChannel));
        this.applicationContext = applicationContext;
    }

    /**
     * 发送短信
     *
     * @param parameter 发送短信参数
     * @return 返回结果
     */
    @Override
    public SmsSendDTO sendSms(RemoteSmsSendParameter parameter) {
        SmsSendDTO sendResult;
        SmsSendParameter sendParameter = new SmsSendParameter();
        BeanUtils.copyProperties(parameter, sendParameter);
        if (parameter.getChannelId() != null) {
            sendResult = this.smartSmsService.send(parameter.getChannelId(), sendParameter);
        } else if (StringUtils.hasText(parameter.getChannelCode())) {
            sendResult = this.smartSmsService.send(parameter.getChannelCode(), sendParameter);
        } else {
            sendResult = this.smartSmsService.send(sendParameter);
        }
        return sendResult;
    }

    /**
     * 发送消息
     *
     * @param parameter 消息发送参数
     * @return 消息发送结果
     */
    @Override
    public Map<MessageChannelEnum, List<MessageSendDTO>> send(RemoteMessageSendParameter parameter) {
        if (CollectionUtils.isEmpty(parameter.getMessageChannels())) {
            throw new SmartMessageException("未指定发送通道");
        }
        Set<MessageChannelEnum> noSupportChannelList = parameter.getMessageChannels().stream()
                .filter(item -> !this.smartMessageSenderMap.containsKey(item))
                .collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(noSupportChannelList)) {
            throw new SmartMessageException(String.format("不支持的消息通道：%s", String.join(",", noSupportChannelList.stream().map(MessageChannelEnum::getRemark).toList())));
        }
        Map<MessageChannelEnum, List<MessageSendDTO>> result = new EnumMap<>(MessageChannelEnum.class);
        parameter.getMessageChannels().forEach(item -> {
            List<SmartMessageSender> messageSenderList = this.smartMessageSenderMap.get(item);
            result.put(item, messageSenderList.stream().map(sender -> sender.send(parameter)).toList());
        });
        this.applicationContext.publishEvent(new SmartMessageSendEvent(parameter, result, this));
        return result;
    }
}
