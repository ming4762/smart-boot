package com.smart.smc.inter.qingdaoport.constants;

import lombok.Getter;

/**
 * 云港通请求地址枚举类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-10 15:06
 * @since 5.0.0
 */
@Getter
public enum QingdaoPortUrlEnum {
    SHIP_PLAN("/gateway/prod/ship/shipPlan", "查询船舶计划"),
    ;

    private final String url;

    private final String remark;

    QingdaoPortUrlEnum(String url, String remark) {
        this.url = url;
        this.remark = remark;
    }
}
