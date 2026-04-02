package com.smart.boot.wechat;

import com.smart.framework.extension.wechat.model.WechatMiniappConfig;
import com.smart.framework.extension.wechat.model.WechatMpConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.List;

/**
 * 微信配置文件
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-01 14:42
 * @since 5.0.0
 */
@ConfigurationProperties(prefix = "smart.wechat")
@Getter
@Setter
public class SmartWechatProperties implements Serializable {

    private String keyPrefix = "smart:wechat";

    /**
     * 小程序配置
     */
    private MinappProperties minapp = new MinappProperties();

    /**
     * 公众号配置
     */
    private MpProperties mp = new MpProperties();

    @Getter
    @Setter
    public static final class MinappProperties implements Serializable {
        private List<WechatMiniappConfig> configs;
    }

    @Getter
    @Setter
    public static final class MpProperties implements Serializable {
        private List<WechatMpConfig> configs;
    }
}
