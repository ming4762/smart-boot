package com.smart.cloud.api.crud;

import com.smart.cloud.api.crud.api.SmartCrudDataPermissionCloudApi;
import com.smart.cloud.api.crud.api.SmartCrudUserCloudApi;
import com.smart.module.api.system.SysUserApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongming4762
 * 2023/3/22
 */
@Configuration(proxyBeanMethods = false)
public class SmartCloudCrudApiAutoConfiguration {

    @Bean
    public SmartCrudUserCloudApi smartCrudUserCloudApi(SysUserApi sysUserApi) {
        return new SmartCrudUserCloudApi(sysUserApi);
    }

    @Bean
    public SmartCrudDataPermissionCloudApi smartCrudDataPermissionCloudApi(SysUserApi sysUserApi) {
        return new SmartCrudDataPermissionCloudApi(sysUserApi);
    }
}
