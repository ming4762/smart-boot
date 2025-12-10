package com.smart.smc.inter.qingdaoport.api.ship;

import com.smart.smc.inter.qingdaoport.dto.parameter.QingdaoPortShipPlanParameter;
import com.smart.smc.inter.qingdaoport.dto.result.QingdaoPortShipPlanResult;

import java.util.List;

/**
 * 云港通船舶接口
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/12/10 15:47
 * @since 5.0.0
 */
public interface QingdaoPortShipApi {

    /**
     * 查询船舶计划
     * @param parameter 查询参数
     * @return 船舶计划列表
     */
    List<QingdaoPortShipPlanResult> listShipPlan(QingdaoPortShipPlanParameter parameter);
}
