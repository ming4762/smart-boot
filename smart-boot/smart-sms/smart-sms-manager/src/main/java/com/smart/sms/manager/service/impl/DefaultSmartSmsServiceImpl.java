package com.smart.sms.manager.service.impl;

import com.smart.commons.core.exception.SystemException;
import com.smart.commons.core.utils.JsonUtils;
import com.smart.sms.core.constants.SmartSmsChannelEnum;
import com.smart.sms.core.exception.SmartSmsSendException;
import com.smart.sms.core.parameter.SmsSendParameter;
import com.smart.sms.core.result.SmsSendResult;
import com.smart.sms.core.service.SmartSmsChannelService;
import com.smart.sms.core.service.SmartSmsService;
import com.smart.sms.manager.model.SmartSmsChannelManagerPO;
import com.smart.sms.manager.model.SmartSmsLogPO;
import com.smart.sms.manager.service.SmartSmsChannelManagerService;
import com.smart.sms.manager.service.SmartSmsLogService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhongming4762
 * 2023/5/30
 */
@Slf4j
public class DefaultSmartSmsServiceImpl implements SmartSmsService {

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
    public SmsSendResult send(SmsSendParameter sendParameter) {
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
    public SmsSendResult send(String channelCode, SmsSendParameter sendParameter) {
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
    public SmsSendResult send(Long channelId, SmsSendParameter sendParameter) {
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
    protected SmsSendResult send(SmartSmsChannelManagerPO channelData, SmsSendParameter sendParameter) {
        try {
            SmsSendResult sendResult = this.doSend(channelData, sendParameter);
            this.saveSendLog(channelData, sendParameter, sendResult, null);
            return sendResult;
        } catch (Exception e) {
            log.error("短信发送失败", e);
            this.saveSendLog(channelData, sendParameter, null, e);
            throw e;
        }
    }

    private void saveSendLog(SmartSmsChannelManagerPO channelData, SmsSendParameter sendParameter, SmsSendResult sendResult, Exception e) {
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
    protected SmsSendResult doSend(SmartSmsChannelManagerPO channelData, SmsSendParameter sendParameter) {
        // 获取短信通道服务类
        SmartSmsChannelService smsChannelService = this.smartSmsChannelServiceMap.get(channelData.getChannelType());
        if (smsChannelService == null) {
            throw new SystemException("系统发生未知错误，不支持的通道类型，通道类型：" + channelData.getChannelType());
        }
        return smsChannelService.send(channelData.getChannelProperties(), sendParameter);
    }
}
