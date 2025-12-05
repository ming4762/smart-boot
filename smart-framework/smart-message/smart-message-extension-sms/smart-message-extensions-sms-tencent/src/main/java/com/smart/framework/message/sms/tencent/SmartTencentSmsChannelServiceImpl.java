package com.smart.framework.message.sms.tencent;

import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.commons.validate.utils.ValidatorUtils;
import com.smart.framework.message.core.constants.SmartMessageChannelType1Enum;
import com.smart.framework.message.core.constants.SmartMessageChannelType2Enum;
import com.smart.framework.message.core.exception.SmartSmsException;
import com.smart.framework.message.core.pojo.dto.SmartMessageToUserDTO;
import com.smart.module.api.message.constants.SmartSmsChannelEnum;
import com.smart.module.api.message.dto.MessageSendResult;
import com.smart.module.api.message.dto.SmsSendResult;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 腾讯云短信服务类
 * @author zhongming4762
 * 2023/5/25
 */
@Slf4j
public class SmartTencentSmsChannelServiceImpl implements SmartTencentSmsChannelService {


    private static final Map<String, ClientCache> CLIENT_CACHE_MAP = new ConcurrentHashMap<>();

    private static final int UP_LIMIT_PHONE = 200;

    private ClientCache getClientCache(String properties) {
        return CLIENT_CACHE_MAP.computeIfAbsent(properties,  key -> {
            SmartSmsTencentChannelProperties channelProperties = JsonUtils.parse(key, SmartSmsTencentChannelProperties.class);

            Credential credential = new Credential(channelProperties.getAccessKey(), channelProperties.getAccessSecret());
            SmsClient smsClient = new SmsClient(credential, channelProperties.getRegion().getRegion());

            return new ClientCache(smsClient, channelProperties);
        });
    }

    @Getter
    @AllArgsConstructor
    private static class ClientCache {

        private SmsClient client;

        private SmartSmsTencentChannelProperties properties;
    }

    /**
     * 获取支持的二级通道信息
     *
     * @return 支持的消息通道
     */
    @Nullable
    @Override
    public SmartMessageChannelType2Enum supportChannel2() {
        return SmartMessageChannelType2Enum.SMS_TENCENT;
    }

    /**
     * 获取支持的一级通道信息
     *
     * @return 支持的消息通道
     */
    @NonNull
    @Override
    public SmartMessageChannelType1Enum supportChannel1() {
        return SmartMessageChannelType1Enum.SMS;
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
        RemoteMessageSendParameter.SmsSendParameter smsSendParameter = parameter.getSmsSendParameter();
        if (smsSendParameter == null) {
            throw new SmartSmsException("smsSendParameter is null");
        }
        // 验证参数
        ValidatorUtils.validate(smsSendParameter);
        // 验证手机号为空的
        String noMobileUsers = toUserList.stream()
                .filter(item -> !StringUtils.hasText(item.getMobile()))
                .map(SmartMessageToUserDTO::getFullName)
                .collect(Collectors.joining(","));
        if (StringUtils.hasText(noMobileUsers)) {
            log.warn("用户【{}】没有设置手机号，发送消息失败", noMobileUsers);
        }
        Set<String> mobiles = toUserList.stream()
                .map(SmartMessageToUserDTO::getMobile)
                .collect(Collectors.toSet());
        if (mobiles.size() > UP_LIMIT_PHONE) {
            throw new SmartSmsException("一次发送手机号过多，上限为200个手机号码，请分批发送");
        }

        ClientCache clientCache = this.getClientCache(channelProperties);

        SendSmsRequest request = new SendSmsRequest();
        request.setSmsSdkAppId(clientCache.getProperties().getAppid());

        request.setSignName(smsSendParameter.getSignName());
        request.setTemplateId(smsSendParameter.getTemplate());

        // 获取参数
        if (parameter.getTemplateData() != null) {
            if (!(parameter.getTemplateData() instanceof List<?> parameterList)) {
                throw new SmartSmsException("Tencent sms templateData must be List");
            }
            request.setTemplateParamSet(parameterList.toArray(new String[0]));
        }
        // 设置手机号
        request.setPhoneNumberSet(mobiles.toArray(new String[0]));
        try {
            SendSmsResponse smsResponse = clientCache.getClient().SendSms(request);

            return SmsSendResult.builder()
                    .requestId(smsResponse.getRequestId())
                    .responseData(JsonUtils.toJsonString(smsResponse))
                    // TODO：待赋值
                    .channelId(null)
                    .channelCode(null)
                    .channelType(SmartSmsChannelEnum.SMS_TENCENT)
                    .build();
        } catch (TencentCloudSDKException e) {
            throw new SmartSmsException(e);
        }
    }
}
