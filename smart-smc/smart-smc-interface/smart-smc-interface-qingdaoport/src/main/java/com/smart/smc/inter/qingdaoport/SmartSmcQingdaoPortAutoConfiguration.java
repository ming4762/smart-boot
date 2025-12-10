package com.smart.smc.inter.qingdaoport;

import com.smart.module.api.system.SysLogApi;
import com.smart.smc.inter.qingdaoport.api.DefaultQingdaoPortApiImpl;
import com.smart.smc.inter.qingdaoport.api.QingdaoPortApi;
import com.smart.smc.inter.qingdaoport.api.ship.DefaultQingdaoPortShipApiImpl;
import com.smart.smc.inter.qingdaoport.api.ship.QingdaoPortShipApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 云港通自动配置类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-05 15:25
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SmartSmcQingdaoPortProperties.class)
public class SmartSmcQingdaoPortAutoConfiguration {

    /**
     * 云港通接口
     * @return 云港通接口
     */
    @Bean
    public QingdaoPortApi qingdaoPortApi() {
        return new DefaultQingdaoPortApiImpl();
    }

    /**
     * 云港通船舶接口
     * @param portProperties 云港通配置属性
     * @param sysLogApi 系统日志接口
     * @return 云港通船舶接口
     */
    @Bean
    @ConditionalOnMissingBean
    public QingdaoPortShipApi qingdaoPortShipApi(SmartSmcQingdaoPortProperties portProperties, SysLogApi sysLogApi) {
        return new DefaultQingdaoPortShipApiImpl(portProperties, sysLogApi);
    }
}
