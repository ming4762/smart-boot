package com.smart.message.manager.api.remote;

import com.smart.message.manager.api.local.LocalSmartMessageApi;
import com.smart.module.api.message.SmartMessageApi;
import com.smart.module.api.message.constants.SmartMessageApiUrlConstants;
import com.smart.module.api.message.dto.MessageSendDTO;
import com.smart.module.api.message.dto.SmsSendDTO;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;
import com.smart.module.api.message.parameter.RemoteSmsSendParameter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息远程调用接口
 * @author zhongming4762
 * 2023/6/6
 */
@RestController
@RequestMapping
public class RemoteSmartMessageApiController implements SmartMessageApi {

    private final LocalSmartMessageApi localSmartMessageApi;

    public RemoteSmartMessageApiController(LocalSmartMessageApi localSmartMessageApi) {
        this.localSmartMessageApi = localSmartMessageApi;
    }

    /**
     * 发送短信
     *
     * @param parameter 发送短信参数
     * @return 返回结果
     */
    @Override
    @PostMapping(SmartMessageApiUrlConstants.SMS_SEND)
    public SmsSendDTO sendSms(@RequestBody RemoteSmsSendParameter parameter) {
        return this.localSmartMessageApi.sendSms(parameter);
    }

    /**
     * 发送消息
     *
     * @param parameter 消息发送参数
     * @return 消息发送结果
     */
    @Override
    @PostMapping(SmartMessageApiUrlConstants.SEND)
    public MessageSendDTO send(@RequestBody RemoteMessageSendParameter parameter) {
        return this.localSmartMessageApi.send(parameter);
    }
}
