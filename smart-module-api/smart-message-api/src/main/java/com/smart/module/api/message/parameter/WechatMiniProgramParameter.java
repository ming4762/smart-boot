package com.smart.module.api.message.parameter;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

/**
 * 微信小程序跳转参数
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-03 16:17
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WechatMiniProgramParameter implements Serializable {

    @NotBlank(message = "小程序appid不能为空")
    private String appid;

    private String pagePath;
}
