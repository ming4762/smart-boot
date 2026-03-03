package com.smart.framework.auth.extensions.wechat.monitor;

import com.smart.framework.auth.core.service.AuthCache;
import com.smart.framework.extension.wechat.pojo.dto.WechatMessageResultDTO;
import com.smart.framework.rocketmq.constants.SmartMqDestinationConstants;
import com.smart.framework.rocketmq.consumer.SmartBaseConsumer;
import com.smart.framework.rocketmq.model.SmartMqMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;

/**
 * 微信登录扫码MQ消息监控器
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-02 10:19
 * @since 5.0.0
 */
@RocketMQMessageListener(
        topic = SmartMqDestinationConstants.WECHAT_EVENT,
        selectorExpression = SmartMqDestinationConstants.WECHAT_EVENT_MESSAGE_TAG,
        consumerGroup = "smart-auth-wechat-login-scan"
)
@Slf4j
public class WechatLoginScanMqMonitor extends AbstractWechatLoginScanMonitor implements SmartBaseConsumer<WechatMessageResultDTO> {


    public WechatLoginScanMqMonitor(AuthCache authCache) {
        super(authCache);
    }

    /**
     * 业务消息处理，子类实现
     *
     * @param message 消息对象
     */
    @Override
    public void doOnMessage(SmartMqMessage<WechatMessageResultDTO> message) {
        WechatMessageResultDTO wechatScanResult = message.getPayload();
        // 获取scene
        this.handleScanResult(wechatScanResult);
    }
}
