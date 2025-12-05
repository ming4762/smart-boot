package com.smart.framework.auth.core.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 钉钉认证配置
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/5 10:50
 * @since 5.0.0
 */
@Getter
@Setter
public class AuthDingtalkProperties implements Serializable {

     /**
     * 钉钉应用的ClientID
     */
     @NotBlank(message = "钉钉应用的ClientID不能为空")
     private String clientId;

     /**
     * 钉钉应用的ClientSecret
     */
     @NotBlank(message = "钉钉应用的ClientSecret不能为空")
     private String clientSecret;

    /**
     * 组织ID，用户限定用户登录所选组织
     * 若不配置，则表示不限制组织，用户登录可以选择任意组织
     */
    private String corpId;
}
