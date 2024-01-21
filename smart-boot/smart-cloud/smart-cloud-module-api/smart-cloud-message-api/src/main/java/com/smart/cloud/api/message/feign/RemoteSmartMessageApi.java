package com.smart.cloud.api.message.feign;

import com.smart.cloud.common.core.constants.CloudServiceNameConstants;
import com.smart.module.api.message.SmartMessageApi;
import com.smart.module.api.message.constants.MessageChannelEnum;
import com.smart.module.api.message.constants.SmartMessageApiUrlConstants;
import com.smart.module.api.message.dto.MessageSendDTO;
import com.smart.module.api.message.dto.SmsSendDTO;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;
import com.smart.module.api.message.parameter.RemoteSmsSendParameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

/**
 * 消息模块远程调用接口
 * @author zhongming4762
 * 2023/6/6
 */
@FeignClient(value = CloudServiceNameConstants.MESSAGE_SERVICE, contextId = "smartMessageApi")
public interface RemoteSmartMessageApi extends SmartMessageApi {

    /**
     * 发送短信
     *
     * @param parameter 发送短信参数
     * @return 返回结果
     */
    @Override
    @PostMapping(SmartMessageApiUrlConstants.SMS_SEND)
    SmsSendDTO sendSms(RemoteSmsSendParameter parameter);

    /**
     * 发送消息
     *
     * @param parameter 消息发送参数
     * @return 消息发送结果
     */
    @Override
    @PostMapping(SmartMessageApiUrlConstants.SEND)
    Map<MessageChannelEnum, List<MessageSendDTO>> send(RemoteMessageSendParameter parameter);
}
