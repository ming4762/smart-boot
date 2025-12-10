package com.smart.smc.inter.qingdaoport.api.ship;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smart.module.api.system.SysLogApi;
import com.smart.smc.inter.qingdaoport.SmartSmcQingdaoPortProperties;
import com.smart.smc.inter.qingdaoport.api.QingdaoPortCommonApi;
import com.smart.smc.inter.qingdaoport.constants.QingdaoPortUrlEnum;
import com.smart.smc.inter.qingdaoport.dto.parameter.QingdaoPortShipPlanParameter;
import com.smart.smc.inter.qingdaoport.dto.result.QingdaoPortListResult;
import com.smart.smc.inter.qingdaoport.dto.result.QingdaoPortShipPlanResult;

import java.util.List;

/**
 * 默认云港通船舶接口实现类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-10 15:51
 * @since 5.0.0
 */
public class DefaultQingdaoPortShipApiImpl extends QingdaoPortCommonApi implements QingdaoPortShipApi {

    public DefaultQingdaoPortShipApiImpl(SmartSmcQingdaoPortProperties properties, SysLogApi sysLogApi) {
        super(properties, sysLogApi);
    }

    /**
     * 查询船舶计划
     *
     * @param parameter 查询参数
     * @return 船舶计划列表
     */
    @Override
    public List<QingdaoPortShipPlanResult> listShipPlan(QingdaoPortShipPlanParameter parameter) {
        QingdaoPortListResult<QingdaoPortShipPlanResult> result = this.doRequest(
                QingdaoPortUrlEnum.SHIP_PLAN,
                parameter,
                null,
                false,
                new TypeReference<>() {
                }
        );
        return result.getList();
    }
}
