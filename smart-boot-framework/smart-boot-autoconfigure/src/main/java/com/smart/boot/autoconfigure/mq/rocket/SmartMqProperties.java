package com.smart.boot.autoconfigure.mq.rocket;

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

    private String prefix = "";

}
