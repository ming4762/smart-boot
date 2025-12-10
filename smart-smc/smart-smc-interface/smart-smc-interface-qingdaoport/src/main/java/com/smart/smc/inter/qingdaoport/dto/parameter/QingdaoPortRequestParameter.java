package com.smart.smc.inter.qingdaoport.dto.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 云港通请求参数
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-05 15:29
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
public class QingdaoPortRequestParameter {

    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * 签名
     */
    private String sign;

    /**
     * 类型
     */
    private String type;
    /**
     * 数据
     */
    private Object data;
}
