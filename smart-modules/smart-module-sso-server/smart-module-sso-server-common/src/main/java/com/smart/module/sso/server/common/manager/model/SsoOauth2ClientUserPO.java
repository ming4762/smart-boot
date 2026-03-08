package com.smart.module.sso.server.common.manager.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.model.BaseModelUserTime;
import com.smart.module.sso.server.common.manager.constants.SsoOauth2UserAccessStrategyEnum;
import lombok.Getter;
import lombok.Setter;

/**
* sso_oauth2_client_user - OAuth2客户端用户关联表
* @author SmartCodeGenerator
* 2026年2月20日 15:06:42
*/
@Getter
@Setter
@TableName("sso_oauth2_client_user")
public class SsoOauth2ClientUserPO extends BaseModelUserTime {

    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * client_id - clientId
    */
    private Long clientId;

    /**
    * user_id - userId
    */
    private Long userId;

    /**
    * access_strategy - 访问策略, ALLOW: 允许, DENY: 拒绝
    */
    private SsoOauth2UserAccessStrategyEnum accessStrategy;

    /**
    * use_yn - 是否启用
    */
    private Boolean useYn;

    public boolean isAllow() {
        return SsoOauth2UserAccessStrategyEnum.ALLOW.equals(this.accessStrategy);
    }
}