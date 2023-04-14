package com.smart.starter.wxjava.properties;

import com.smart.starter.wxjava.model.WechatMpConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/4/7 15:52
 */
@ConfigurationProperties(prefix = "smart.wechat.mp")
@Getter
@Setter
public class WechatMpProperties {

    private List<WechatMpConfig> configs;
}
