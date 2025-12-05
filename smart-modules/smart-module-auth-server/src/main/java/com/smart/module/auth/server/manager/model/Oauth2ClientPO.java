package com.smart.module.auth.server.manager.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.annotation.TableUseYnField;
import com.smart.framework.crud.model.BaseModelDeleteUserTime;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
* oauth2_client - oauth2客户端
* @author SmartCodeGenerator
* 2025年11月14日 16:54:04
*/
@Getter
@Setter
@TableName("oauth2_client")
public class Oauth2ClientPO extends BaseModelDeleteUserTime {

    /**
    * id - 主键
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * client_id - 客户端id
    */
    private String clientId;

    /**
    * client_name - 客户端名称
    */
    private String clientName;

    /**
    * client_secret - 客户端密钥
    */
    private String clientSecret;

    /**
    * client_secret_expire - 密钥过期时间, null表示永不过期
    */
    private ZonedDateTime clientSecretExpire;

    /**
    * client_authentication_methods - 客户端认证方式,以逗号分隔, client_secret_basic, client_secret_post, client_secret_jwt
    */
    private String clientAuthenticationMethods;

    /**
    * authorization_grant_types - 授权类型,以逗号分隔, authorization_code, client_credentials, refresh_token
    */
    private String authorizationGrantTypes;

    /**
    * redirect_uri - 重定向uri,以逗号分隔
    */
    private String redirectUri;

    /**
    * post_logout_redirect_uri - 注销后重定向uri,以逗号分隔
    */
    private String postLogoutRedirectUri;

    /**
    * scopes - 作用域,以逗号分隔
    */
    private String scopes;

    /**
    * client_settings - 客户端设置
    */
    private String clientSettings;

    /**
    * token_settings - 令牌设置
    */
    private String tokenSettings;

    /**
    * remark - 备注
    */
    private String remark;

    /**
    * use_yn - useYn
    */
    @TableUseYnField
    private Boolean useYn;

    /**
    * delete_yn - deleteYn
    */
    @TableLogic
    private Boolean deleteYn;

}