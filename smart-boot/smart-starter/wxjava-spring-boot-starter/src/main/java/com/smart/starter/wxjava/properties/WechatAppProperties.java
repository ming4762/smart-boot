package com.smart.starter.wxjava.properties;

import com.smart.starter.wxjava.model.WechatAppConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 微信小程序配置文件
 * @author zhongming4762
 * 2023/4/4
 */
@ConfigurationProperties(prefix = "smart.wechat.miniapp")
@Getter
@Setter
public class WechatAppProperties {

    private List<WechatAppConfig> configs;
}
