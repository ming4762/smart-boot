package com.smart.smc.inter.qingdaoport.dto.result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 转运参数返回结果
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-10 16:34
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
public class QingdaoPortTransferParamQueryResult implements Serializable {

    /**
     * 转运航线对应的唯一Id，转运申请申报时需填写
     */
    private String routeRecord;

    /**
     * 转运航线描述
     */
    private String routeDesc;

    /**
     * 起运地代码
     */
    private String fromPlaceCode;

    /**
     * 起运地名称
     */
    private String fromPlaceName;

    /**
     * 目的地代码
     */
    private String toPlaceCode;

    /**
     * 目的地名称
     */
    private String toPlaceName;
}
