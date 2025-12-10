package com.smart.smc.inter.qingdaoport.api;

import com.smart.smc.inter.qingdaoport.api.ship.QingdaoPortShipApi;
import com.smart.smc.inter.qingdaoport.api.transfer.QingdaoPortTransferApi;
import com.smart.smc.inter.qingdaoport.support.QingdaoPortCustomHolder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

/**
 * 默认云港通接口实现类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-10 15:46
 * @since 5.0.0
 */
public class DefaultQingdaoPortApiImpl implements QingdaoPortApi, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 切换客户
     *
     * @param customerCode 客户编码
     * @return 云港通接口
     */
    @Override
    public QingdaoPortApi switchover(String customerCode) {
        QingdaoPortCustomHolder.set(customerCode);
        return this;
    }

    /**
     * 切换默认客户
     *
     * @return 云港通接口
     */
    @Override
    public QingdaoPortApi switchoverDefault() {
        QingdaoPortCustomHolder.clear();
        return this;
    }

    /**
     * 船舶接口
     *
     * @return 船舶接口
     */
    @Override
    public QingdaoPortShipApi shipApi() {
        return this.applicationContext.getBean(QingdaoPortShipApi.class);
    }

    /**
     * 智能转运平台接口
     *
     * @return 智能转运平台接口
     */
    @Override
    public QingdaoPortTransferApi transferApi() {
        return this.applicationContext.getBean(QingdaoPortTransferApi.class);
    }
}
