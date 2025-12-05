package com.smart.module.auth.server.manager.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
* oauth2_client - oauth2客户端
* @author SmartCodeGenerator
* 2025年11月14日 16:54:04
*/
@Getter
@Setter
@ToString
public class Oauth2ClientSaveUpdateDTO implements Serializable {

    /**
    * 主键
    */
    private Long id;
    /**
    * 客户端id
    */
    private String clientId;
    /**
    * 客户端名称
    */
    private String clientName;
    /**
    * 客户端密钥
    */
    private String clientSecret;
    /**
    * 密钥过期时间
    */
    private ZonedDateTime clientSecretExpire;
    /**
    * 客户端认证方式
    */
    private String clientAuthenticationMethods;
    /**
    * 授权类型
    */
    private String authorizationGrantTypes;
    /**
    * 重定向uri
    */
    private String redirectUri;
    /**
    * 注销后重定向uri
    */
    private String postLogoutRedirectUri;
    /**
    * 作用域
    */
    private String scopes;
    /**
    * 客户端设置
    */
    private String clientSettings;
    /**
    * 令牌设置
    */
    private String tokenSettings;
    /**
    * 备注
    */
    private String remark;

}