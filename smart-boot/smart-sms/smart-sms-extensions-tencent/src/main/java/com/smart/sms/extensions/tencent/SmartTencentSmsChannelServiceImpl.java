package com.smart.sms.extensions.tencent;

import com.smart.commons.core.utils.JsonUtils;
import com.smart.commons.validate.utils.ValidatorUtils;
import com.smart.sms.core.constants.SmartSmsChannelEnum;
import com.smart.sms.core.exception.SmartSmsException;
import com.smart.sms.core.parameter.SmsSendParameter;
import com.smart.sms.core.result.SmsSendResult;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 腾讯云短信服务类
 * @author zhongming4762
 * 2023/5/25
 */
public class SmartTencentSmsChannelServiceImpl implements SmartTencentSmsChannelService {


    private static final Map<String, ClientCache> CLIENT_CACHE_MAP = new ConcurrentHashMap<>();

    private static final int UP_LIMIT_PHONE = 200;

    protected ClientCache getClientCache(String properties) {
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
     * 获取支持的通道
     *
     * @return SmartSmsSupportEnum
     */
    @Override
    public SmartSmsChannelEnum support() {
        return SmartSmsChannelEnum.TENCENT;
    }

    /**
     * 发送短信接口
     *
     * @param channelProperties 通道参数
     * @param sendParameter     发送参数
     * @return 响应结果
     */
    @Override
    public SmsSendResult send(String channelProperties, SmsSendParameter sendParameter) {
        // 验证参数
        ValidatorUtils.validate(sendParameter);
        if (sendParameter.getPhoneNumberList().size() > UP_LIMIT_PHONE) {
            throw new SmartSmsException("一次发送手机号过多，上限为200个手机号码，请批量发送");
        }
        ClientCache clientCache = this.getClientCache(channelProperties);

        SendSmsRequest request = new SendSmsRequest();
        request.setSmsSdkAppId(clientCache.getProperties().getAppid());

        request.setSignName(sendParameter.getSignName());
        request.setTemplateId(sendParameter.getTemplate());

        // 获取参数
        if (StringUtils.isNotBlank(sendParameter.getTemplateParameter())) {
            Map<String, String> templateParameter = JsonUtils.parse(sendParameter.getTemplateParameter(), Map.class);
            request.setTemplateParamSet(templateParameter.values().toArray(new String[0]));
        }
        // 设置手机号
        request.setPhoneNumberSet(sendParameter.getPhoneNumberList().toArray(new String[0]));
        try {
            SendSmsResponse smsResponse = clientCache.getClient().SendSms(request);

            return new SmsSendResult(smsResponse.getRequestId(), JsonUtils.toJsonString(smsResponse));
        } catch (TencentCloudSDKException e) {
            throw new SmartSmsException(e);
        }
    }
}
