package com.smart.module.api.system.constants;

import lombok.Getter;

/**
 * 第三方平台子类型枚举
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026/3/2 21:40
 * @since 5.0.0
 */
@Getter
public enum SysThirdPlatformSubTypeEnum {
    /**
     * 微信公众号
     */
    WECHAT_MP(SysThirdPlatformTypeEnum.WECHAT),
    /**
     * 微信小程序
     */
    WECHAT_MINIAPP(SysThirdPlatformTypeEnum.WECHAT),
    ;

    private final SysThirdPlatformTypeEnum platformType;

    SysThirdPlatformSubTypeEnum(SysThirdPlatformTypeEnum platformType) {
        this.platformType = platformType;
    }
}
