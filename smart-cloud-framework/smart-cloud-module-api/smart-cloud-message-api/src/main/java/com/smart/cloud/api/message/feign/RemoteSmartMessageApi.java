package com.smart.cloud.api.message.feign;

import com.smart.framework.commons.core.utils.BeanUtils;
import com.smart.module.api.message.SmartMessageApi;
import com.smart.module.api.message.dto.MessageSendResult;
import com.smart.module.api.message.dto.SmsSendResult;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;
import com.smart.module.api.message.parameter.RemoteSmsSendParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息模块远程调用接口
 * @author zhongming4762
 * 2023/6/6
 */
@Component
@RequiredArgsConstructor
public class RemoteSmartMessageApi implements SmartMessageApi {

    private final FeignSmartMessageApi feignSmartMessageApi;


    /**
     * 发送短信
     *
     * @param parameter 发送短信参数
     * @return 返回结果
     */
    @Override
    public SmsSendResult sendSms(RemoteSmsSendParameter parameter) {
        return this.feignSmartMessageApi.sendSms(parameter);
    }

    /**
     * 发送消息
     *
     * @param parameter 消息发送参数
     * @return 消息发送结果
     */
    @Override
    public List<MessageSendResult> send(RemoteMessageSendParameter parameter) {
        Map<String, Object> flattenBean = BeanUtils.flattenBean(parameter);
        // 创建新的Map存储处理后的键值对
        Map<String, Object> processedMap = HashMap.newHashMap(flattenBean.size());

        for (Map.Entry<String, Object> entry : flattenBean.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            // 跳过null值
            if (value == null) {
                continue;
            }
            // 如果是枚举类型，使用toString值
            if (value instanceof Enum<?>) {
                processedMap.put(key, value.toString());
            } else {
                processedMap.put(key, value);
            }
        }
        return this.feignSmartMessageApi.send(processedMap);
    }
}
