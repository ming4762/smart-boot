package com.smart.module.api.system.parameter;

import lombok.*;

import java.io.Serializable;

/**
 * @author zhongming4762
 * 2023/5/15
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WechatUserQueryParameter implements Serializable {

    private String appid;

    private String openid;

    private String unionid;
}
