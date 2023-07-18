package com.smart.sms.extensions.aliyun;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.message.core.exception.SmartSmsException;
import com.message.core.parameter.SmsSendParameter;
import com.smart.commons.core.utils.JsonUtils;
import com.smart.commons.validate.utils.ValidatorUtils;
import com.smart.module.api.message.constants.SmartSmsChannelEnum;
import com.smart.module.api.message.dto.SmsSendDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 阿里云短信服务类
 * @author zhongming4762
 * 2023/5/25
 */
public class SmartAliyunSmsChannelServiceImpl implements SmartAliyunSmsChannelService {

    private static final String DEFAULT_ENDPOINT = "dysmsapi.aliyuncs.com";

    private static final int UP_LIMIT_PHONE = 1000;

    /**
     * 短信发送成功的code
     */
    private static final String SUCCESS_CODE = "OK";

    private static final Map<String, ClientCache> CLIENT_CACHE_MAP = new ConcurrentHashMap<>();

    protected ClientCache getClientCache(String properties) {
        return CLIENT_CACHE_MAP.computeIfAbsent(properties, key -> {
            SmartSmsAliyunChannelProperties channelProperties = JsonUtils.parse(key, SmartSmsAliyunChannelProperties.class);
            Config config = new Config()
                    .setAccessKeyId(channelProperties.getAccessKey())
                    .setAccessKeySecret(channelProperties.getAccessSecret());
            // 设置访问域名
            config.endpoint = StringUtils.isNotBlank(channelProperties.getEndpoint()) ? channelProperties.getEndpoint() : DEFAULT_ENDPOINT;

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
     * 获取支持的通道
     *
     * @return SmartSmsSupportEnum
     */
    @Override
    public SmartSmsChannelEnum support() {
        return SmartSmsChannelEnum.ALIYUN;
    }

    /**
     * 发送短信接口
     *
     * @param channelProperties 通道参数
     * @param sendParameter     发送参数
     * @return 响应结果
     */
    @Override
    public SmsSendDTO send(String channelProperties, SmsSendParameter sendParameter) {
        // 验证参数
        ValidatorUtils.validate(sendParameter);
        if (sendParameter.getPhoneNumberList().size() > UP_LIMIT_PHONE) {
            throw new SmartSmsException("一次发送手机号过多，上限为1000个手机号码，请批量发送");
        }
        // 获取客户端信息
        Client client = this.getClientCache(channelProperties).getClient();

        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(
                        String.join(",", sendParameter.getPhoneNumberList())
                )
                .setSignName(sendParameter.getSignName())
                .setTemplateCode(sendParameter.getTemplate());
        if (!CollectionUtils.isEmpty(sendParameter.getTemplateParameter())) {
            sendSmsRequest.setTemplateParam(JsonUtils.toJsonString(sendParameter.getTemplateParameter()));
        }
        try {
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, new RuntimeOptions());
            if (SUCCESS_CODE.equals(sendSmsResponse.getBody().getCode())) {
                return new SmsSendDTO(sendSmsResponse.getBody().getRequestId(), JsonUtils.toJsonString(sendSmsResponse), null, null, null);
            }
            throw new SmartSmsException(JsonUtils.toJsonString(sendSmsResponse));
        } catch (Exception e) {
            throw new SmartSmsException(e);
        }
    }

}
