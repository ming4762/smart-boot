package com.smart.module.api.system.dto;

import com.smart.module.api.system.constants.SysThirdPlatformSubTypeEnum;
import com.smart.module.api.system.constants.SysThirdPlatformTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 系统用户第三方账号DTO
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-03 16:28
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
public class SysUserThirdAccountDTO implements Serializable {

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
