package com.smart.module.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.model.BaseModelCreateUserTime;
import com.smart.module.api.system.constants.SysThirdPlatformSubTypeEnum;
import com.smart.module.api.system.constants.SysThirdPlatformTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
* sys_user_third_account - 用户第三方账号表
* @author SmartCodeGenerator
* 2026年3月2日 21:38:50
*/
@Getter
@Setter
@TableName("sys_user_third_account")
public class SysUserThirdAccountPO extends BaseModelCreateUserTime {

    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * user_id - 用户ID
    */
    private Long userId;

    /**
    * platform_type - 平台类型
    */
    private SysThirdPlatformTypeEnum platformType;

    /**
    * app_type - 应用类型
    */
    private SysThirdPlatformSubTypeEnum platformSubType;

    /**
    * appid - 应用ID
    */
    private String appid;

    /**
    * openid - 用户的openid
    */
    private String openid;

    /**
    * unionid - 用户的unionid
    */
    private String unionid;

    /**
    * third_user_id - 第三方用户ID
    */
    private String thirdUserId;
}