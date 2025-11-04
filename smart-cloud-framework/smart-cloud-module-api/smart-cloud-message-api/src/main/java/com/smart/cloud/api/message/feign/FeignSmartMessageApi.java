package com.smart.cloud.api.message.feign;

import com.smart.cloud.common.core.constants.CloudServiceNameConstants;
import com.smart.module.api.message.constants.SmartMessageApiUrlConstants;
import com.smart.module.api.message.dto.MessageSendResult;
import com.smart.module.api.message.dto.SmsSendResult;
import com.smart.module.api.message.parameter.RemoteSmsSendParameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

/**
 * 消息模块Feign调用接口
 * feign不支持文件和复杂json同时作为一个对象传递，因此需要拆分成多个参数传递
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/4 14:53
 * @since 5.0.0
 */
@FeignClient(value = CloudServiceNameConstants.MESSAGE_SERVICE, contextId = "remoteSmartMessageApi")
public interface FeignSmartMessageApi {

    /**
     * 发送短信
     *
     * @param parameter 发送短信参数
     * @return 返回结果
     */
    @PostMapping(SmartMessageApiUrlConstants.SMS_SEND)
    SmsSendResult sendSms(RemoteSmsSendParameter parameter);

    /**
     * 发送消息
     *
     * @param parameter 消息发送参数json字符串
     * @return 消息发送结果
     */
    @PostMapping(value = SmartMessageApiUrlConstants.SEND, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    List<MessageSendResult> send(Map<String,Object> parameter);
}
