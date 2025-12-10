package com.smart.smc.inter.qingdaoport.api;

import com.smart.smc.inter.qingdaoport.api.ship.QingdaoPortShipApi;

/**
 * 云港通接口
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/12/10 15:46
 * @since 5.0.0
 */
public interface QingdaoPortApi {

    /**
     * 切换客户
     * @param customerCode 客户编码
     * @return 云港通接口
     */
    QingdaoPortApi switchover(String customerCode);

    /**
     * 切换默认客户
     * @return 云港通接口
     */
    QingdaoPortApi switchoverDefault();

    /**
     * 船舶接口
     * @return 船舶接口
     */
    QingdaoPortShipApi shipApi();

}
