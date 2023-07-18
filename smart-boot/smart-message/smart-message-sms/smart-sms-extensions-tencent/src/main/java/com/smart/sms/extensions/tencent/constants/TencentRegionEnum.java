package com.smart.sms.extensions.tencent.constants;

import lombok.Getter;

/**
 * 地域信息
 * @author zhongming4762
 * 2023/5/25
 */
@Getter
public enum TencentRegionEnum {
    /**
     * 地域信息
     */
    BEIJING("tap-beijing", "华北地区(北京)"),
    GUANGZHOU("ap-guangzhou", "华南地区(广州)"),
    NANJING("ap-nanjing", "华东地区(南京)")
    ;

    private final String region;

    private final String remark;

    TencentRegionEnum(String region, String remark) {
        this.region = region;
        this.remark = remark;
    }
}
