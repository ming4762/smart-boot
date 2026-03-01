package com.smart.framework.extension.wechat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 微信小程序配置
 * @author zhongming4762
 * 2023/4/4
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class WechatMiniappConfig extends AbstractWechatConfig {

    /**
     * 消息格式，XML或者JSON
     */
    private String msgDataFormat;
}
