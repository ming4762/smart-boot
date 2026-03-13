package com.smart.boot.autoconfigure.mq.rocket;

import com.smart.framework.rocketmq.constants.SmartMqDestinationConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * MQ 配置属性
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-01 12:09
 * @since 5.0.0
 */
@Getter
@Setter
@ConfigurationProperties("smart.mq")
public class SmartMqProperties implements Serializable {

    /**
     * MQ topic前缀，用于区分不同环境
     */
    private String prefix = "";

    private String eventTopic = SmartMqDestinationConstants.SMART_EVENT;
    private String eventTag = SmartMqDestinationConstants.SMART_EVENT_TAG;

}
