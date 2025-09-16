package com.smart.smc.inter.sdport.pay;

import com.smart.framework.freemarker.engine.TemplateEngine;
import com.smart.module.api.system.SysLogApi;
import com.smart.module.api.system.SysParameterApi;
import com.smart.smc.inter.sdport.pay.api.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 山港云付接口自动配置类
 * @author shizhongming
 * 2025/9/16 09:50
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SmartSmcSdportPayProperties.class)
public class SmartSmcInterfaceSdportPayAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SdportPayApi sdportPayApi() {
        return new SdportPayApi();
    }

    @Bean
    @ConditionalOnMissingBean
    public SdportPayCustomerManagerApi sdportPayCustomerManagerApi(SmartSmcSdportPayProperties properties, SysLogApi sysLogApi, SysParameterApi sysParameterApi) {
        return new SdportPayCustomerManagerApiImpl(properties, sysLogApi, sysParameterApi);
    }

    @Bean
    @ConditionalOnMissingBean
    public SdportPayOrderApi sdportPayOrderApi(SysLogApi sysLogApi, SysParameterApi sysParameterApi, TemplateEngine templateEngine) {
        return new SdportPayOrderApiImpl(sysLogApi, sysParameterApi, templateEngine);
    }

    @Bean
    @ConditionalOnMissingBean
    public SdportPayRefundApi sdportPayRefundApi(SysLogApi sysLogApi, SysParameterApi sysParameterApi) {
        return new SdportPayRefundApiImpl(sysLogApi, sysParameterApi);
    }
}
