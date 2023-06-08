package com.smart.module.api.message;

import com.smart.module.api.message.dto.SmsSendDTO;
import com.smart.module.api.message.parameter.RemoteSmsSendParameter;

/**
 * 消息模块API
 * @author zhongming4762
 * 2023/5/31 17:57
 */
public interface SmartMessageApi {

    /**
     * 发送短信
     * @param parameter 发送短信参数
     * @return 返回结果
     */
    SmsSendDTO sendSms(RemoteSmsSendParameter parameter);
}
