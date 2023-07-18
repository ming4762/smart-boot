package com.smart.message.manager.service.impl;

import com.message.core.exception.SmartMessageException;
import com.message.core.exception.SmartSmsSendException;
import com.message.core.parameter.SmsSendParameter;
import com.message.core.service.SmartMessageSender;
import com.message.core.service.SmartSmsChannelService;
import com.message.core.service.SmartSmsService;
import com.smart.commons.core.exception.SystemException;
import com.smart.commons.core.utils.JsonUtils;
import com.smart.commons.validate.utils.ValidatorUtils;
import com.smart.message.manager.model.SmartSmsChannelManagerPO;
import com.smart.message.manager.model.SmartSmsLogPO;
import com.smart.message.manager.service.SmartSmsChannelManagerService;
import com.smart.message.manager.service.SmartSmsLogService;
import com.smart.module.api.message.constants.MessageChannelEnum;
import com.smart.module.api.message.constants.SmartSmsChannelEnum;
import com.smart.module.api.message.dto.MessageSendDTO;
import com.smart.module.api.message.dto.SmsSendDTO;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhongming4762
 * 2023/5/30
 */
@Slf4j
public class DefaultSmartSmsServiceImpl implements SmartSmsService, SmartMessageSender {

    private final SmartSmsChannelManagerService smartSmsChannelManagerService;

    private final Map<SmartSmsChannelEnum, SmartSmsChannelService> smartSmsChannelServiceMap;

    private final SmartSmsLogService smartSmsLogService;

    public DefaultSmartSmsServiceImpl(SmartSmsChannelManagerService smartSmsChannelManagerService, List<SmartSmsChannelService> smartSmsChannelServiceList, SmartSmsLogService smartSmsLogService) {
        this.smartSmsChannelManagerService = smartSmsChannelManagerService;
        this.smartSmsChannelServiceMap = smartSmsChannelServiceList.stream()
                .collect(Collectors.toMap(SmartSmsChannelService::support, item -> item));
        this.smartSmsLogService = smartSmsLogService;
    }

    /**
     * 发送短信，使用默认的通道
     *
     * @param sendParameter 发送参数
     * @return 发送结果
     */
    @Override
    public SmsSendDTO send(SmsSendParameter sendParameter) {
        SmartSmsChannelManagerPO defaultChannel = this.smartSmsChannelManagerService.getDefault();
        if (defaultChannel == null) {
            throw new SmartSmsSendException("短信发送失败，无默认的短信发送通道");
        }
        return this.send(defaultChannel, sendParameter);
    }

    /**
     * 发送短信，指定短信通道编码
     *
     * @param channelCode   通道编码
     * @param sendParameter 发送参数
     * @return 发送结果
     */
    @Override
    public SmsSendDTO send(String channelCode, SmsSendParameter sendParameter) {
        SmartSmsChannelManagerPO channelData = this.smartSmsChannelManagerService.getByCode(channelCode);
        if (channelData == null) {
            throw new SmartSmsSendException("短息发送失败，获取短信通道失败，请检查通道编号是否正常，通道编号：" + channelCode);
        }
        return this.send(channelData, sendParameter);
    }

    /**
     * 发送短信，指定通道ID
     *
     * @param channelId     通道ID
     * @param sendParameter 发送参数
     * @return 发送结果
     */
    @Override
    public SmsSendDTO send(Long channelId, SmsSendParameter sendParameter) {
        SmartSmsChannelManagerPO channelData = this.smartSmsChannelManagerService.getById(channelId);
        if (channelData == null) {
            throw new SmartSmsSendException("短息发送失败，获取短信通道失败，请检查通道ID是否正确，通道ID：" + channelId);
        }
        if (Boolean.FALSE.equals(channelData.getUseYn())) {
            throw new SmartSmsSendException("短息发送失败，短信通道为启用，通道ID：" + channelId);
        }
        return this.send(channelData, sendParameter);
    }

    /**
     * 发送短信信息
     * @param channelData 通道数据
     * @param sendParameter 发送参数
     * @return 发送结果
     */
    protected SmsSendDTO send(SmartSmsChannelManagerPO channelData, SmsSendParameter sendParameter) {
        try {
            SmsSendDTO sendResult = this.doSend(channelData, sendParameter);
            this.saveSendLog(channelData, sendParameter, sendResult, null);
            return sendResult;
        } catch (Exception e) {
            log.error("短信发送失败", e);
            this.saveSendLog(channelData, sendParameter, null, e);
            throw e;
        }
    }

    private void saveSendLog(SmartSmsChannelManagerPO channelData, SmsSendParameter sendParameter, SmsSendDTO sendResult, Exception e) {
        try {
            SmartSmsLogPO log = new SmartSmsLogPO();
            log.setChannelId(channelData.getId());
            log.setIsSuccess(e == null);
            log.setSendParameter(JsonUtils.toJsonString(sendParameter));
            log.setSendResult(sendResult == null ? null : JsonUtils.toJsonString(sendResult));
            log.setErrorMessage(e == null ? null : e.getMessage());
            this.smartSmsLogService.save(log);
        } catch (Exception exception) {
            log.warn("保存短信发送日志发生错误", exception);
        }
    }

    /**
     * 执行发送短信
     * @param channelData 通道信息
     * @param sendParameter 发送参数
     * @return 发送结果
     */
    protected SmsSendDTO doSend(SmartSmsChannelManagerPO channelData, SmsSendParameter sendParameter) {
        // 获取短信通道服务类
        SmartSmsChannelService smsChannelService = this.smartSmsChannelServiceMap.get(channelData.getChannelType());
        if (smsChannelService == null) {
            throw new SystemException("系统发生未知错误，不支持的通道类型，通道类型：" + channelData.getChannelType());
        }
        SmsSendDTO sendResult = smsChannelService.send(channelData.getChannelProperties(), sendParameter);

        sendResult.setChannelId(channelData.getId());
        sendResult.setChannelCode(channelData.getChannelCode());
        sendResult.setChannelType(channelData.getChannelType());
        return sendResult;
    }

    /**
     * 获取支持的通道信息
     *
     * @return 支持的消息通道
     */
    @Override
    public MessageChannelEnum supportChannel() {
        return MessageChannelEnum.SMS;
    }

    /**
     * 发送消息
     *
     * @param parameter 消息发送参数
     * @return 消息发送结果
     */
    @Override
    public MessageSendDTO send(RemoteMessageSendParameter parameter) {
        if (parameter.getSmsSendParameter() == null) {
            throw new SmartMessageException("发送短信参数不能为空，参数：" + JsonUtils.toJsonString(parameter));
        }
        ValidatorUtils.validate(parameter.getSmsSendParameter());
        SmsSendParameter smsSendParameter = new SmsSendParameter();
        smsSendParameter.setPhoneNumberList(parameter.getSmsSendParameter().getPhoneNumberList());
        smsSendParameter.setSignName(parameter.getSmsSendParameter().getSignName());
        smsSendParameter.setTemplate(parameter.getSmsSendParameter().getTemplate());
        smsSendParameter.setTemplateParameter(parameter.getTemplateData());
        SmsSendDTO smsSendResult;
        if (StringUtils.hasText(parameter.getSmsSendParameter().getChannelCode())) {
            smsSendResult = this.send(parameter.getSmsSendParameter().getChannelCode(), smsSendParameter);
        } else {
            smsSendResult = this.send(smsSendParameter);
        }
        MessageSendDTO result = new MessageSendDTO();
        result.setSmsSendResult(smsSendResult);
        return result ;
    }
}
