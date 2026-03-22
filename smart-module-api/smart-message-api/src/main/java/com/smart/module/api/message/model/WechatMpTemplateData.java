package com.smart.module.api.message.model;

import lombok.*;

import java.io.Serializable;

/**
 * 微信小程序模板消息数据
 *
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-03 17:15
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WechatMpTemplateData implements Serializable {

    private String name;
    private String value;
    private String color;
}
