package com.smart.framework.message.sms.aliyun;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.smart.framework.message.core.constants.SmartMessageChannelType1Enum;
import com.smart.framework.message.core.constants.SmartMessageChannelType2Enum;
import com.smart.framework.message.core.exception.SmartSmsException;
import com.smart.framework.message.core.pojo.dto.SmartMessageToUserDTO;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.commons.validate.utils.ValidatorUtils;
import com.smart.module.api.message.dto.MessageSendDTO;
import com.smart.module.api.message.dto.SmsSendDTO;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 阿里云短信服务类
 * @author zhongming4762
 * 2023/5/25
 */
@Slf4j
public class SmartAliyunSmsChannelServiceImpl implements SmartAliyunSmsChannelService {

    private static final String DEFAULT_ENDPOINT = "dysmsapi.aliyuncs.com";

    private static final int UP_LIMIT_PHONE = 1000;

    /**
     * 短信发送成功的code
     */
    private static final String SUCCESS_CODE = "OK";

    private static final Map<String, ClientCache> CLIENT_CACHE_MAP = new ConcurrentHashMap<>();

    private ClientCache getClientCache(String properties) {
        return CLIENT_CACHE_MAP.computeIfAbsent(properties, key -> {
            SmartSmsAliyunChannelProperties channelProperties = JsonUtils.parse(key, SmartSmsAliyunChannelProperties.class);
            Config config = new Config()
                    .setAccessKeyId(channelProperties.getAccessKey())
                    .setAccessKeySecret(channelProperties.getAccessSecret());
            // 设置访问域名
            config.endpoint = StringUtils.hasText(channelProperties.getEndpoint()) ? channelProperties.getEndpoint() : DEFAULT_ENDPOINT;

            try {
                return new ClientCache(new Client(config), channelProperties);
            } catch (Exception e) {
                throw new SmartSmsException(e);
            }
        });
    }

    /**
     * 客户端缓存信息
     */
    @Getter
    @AllArgsConstructor
    private static class ClientCache {
        private Client client;

        private SmartSmsAliyunChannelProperties properties;
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
     * 获取支持的二级通道信息
     *
     * @return 支持的消息通道
     */
    @Nullable
    @Override
    public SmartMessageChannelType2Enum supportChannel2() {
        return SmartMessageChannelType2Enum.SMS_ALIYUN;
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
    public MessageSendDTO send(@Nullable String channelProperties, List<SmartMessageToUserDTO> toUserList, RemoteMessageSendParameter parameter) {
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
            throw new SmartSmsException("一次发送手机号过多，上限为1000个手机号码，请分批发送");
        }

        // 获取客户端信息
        Client client = this.getClientCache(channelProperties).getClient();
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(
                        String.join(",", mobiles)
                )
                .setSignName(smsSendParameter.getSignName())
                .setTemplateCode(smsSendParameter.getTemplate());
        if (!CollectionUtils.isEmpty(parameter.getTemplateData())) {
            sendSmsRequest.setTemplateParam(JsonUtils.toJsonString(parameter.getTemplateData()));
        }
        try {
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, new RuntimeOptions());
            if (SUCCESS_CODE.equals(sendSmsResponse.getBody().getCode())) {
                SmsSendDTO smsSendResult = new SmsSendDTO(sendSmsResponse.getBody().getRequestId(), JsonUtils.toJsonString(sendSmsResponse), null, null, null);
                return MessageSendDTO.builder()
                        .smsSendResult(smsSendResult)
                        .build();
            }
            throw new SmartSmsException(JsonUtils.toJsonString(sendSmsResponse));
        } catch (Exception e) {
            throw new SmartSmsException(e);
        }
    }
}
