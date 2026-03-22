package com.smart.module.sso.server.common.manager.model;

import com.baomidou.mybatisplus.annotation.*;
import com.smart.framework.crud.annotation.TableUseYnField;
import com.smart.framework.crud.model.BaseModelDeleteUserTime;
import com.smart.framework.crud.mybatis.handler.StringSplitTypeHandler;
import com.smart.module.sso.server.common.manager.constants.SsoOauth2ClientTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

/**
* oauth2_client - oauth2客户端
* @author SmartCodeGenerator
* 2025年11月14日 16:54:04
*/
@Getter
@Setter
@TableName(value = "sso_oauth2_client", autoResultMap = true)
public class SsoOauth2ClientPO extends BaseModelDeleteUserTime {

    /**
    * id - 主键
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * client_id - 客户端id
    */
    private String clientCode;

    /**
    * client_name - 客户端名称
    */
    private String clientName;

    /**
     * 客户端类型:PUBLIC 公开，PRIVATE私有的，公开应用可以被所有用户访问
     */
    private SsoOauth2ClientTypeEnum clientType;

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
    @TableField(typeHandler = StringSplitTypeHandler.class)
    private List<String> clientAuthenticationMethods;

    /**
    * authorization_grant_types - 授权类型,以逗号分隔, authorization_code, client_credentials, refresh_token
    */
    @TableField(typeHandler = StringSplitTypeHandler.class)
    private List<String> authorizationGrantTypes;

    /**
    * redirect_uri - 重定向uri,以逗号分隔
    */
    @TableField(typeHandler = StringSplitTypeHandler.class)
    private List<String> redirectUri;

    /**
    * post_logout_redirect_uri - 注销后重定向uri,以逗号分隔
    */
    @TableField(typeHandler = StringSplitTypeHandler.class)
    private List<String> postLogoutRedirectUri;

    /**
    * scopes - 作用域,以逗号分隔
    */
    @TableField(typeHandler = StringSplitTypeHandler.class)
    private List<String> scopes;

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

    public boolean isPublic() {
        return SsoOauth2ClientTypeEnum.PUBLIC.equals(this.clientType);
    }

}