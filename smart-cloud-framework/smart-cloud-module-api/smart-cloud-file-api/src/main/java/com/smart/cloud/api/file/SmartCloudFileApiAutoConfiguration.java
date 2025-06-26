package com.smart.cloud.api.file;

import com.smart.cloud.api.file.feign.FeignSmartFileApi;
import com.smart.cloud.api.file.feign.RemoteSmartFileApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongming4762
 * 2023/3/22
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan
@EnableFeignClients(basePackages = {"com.smart.cloud.api.file.feign"})
@ConditionalOnMissingClass("com.smart.module.file.SmartFileManagerAutoConfiguration")
public class SmartCloudFileApiAutoConfiguration {

    @Bean
    public RemoteSmartFileApi remoteSmartFileApi(FeignSmartFileApi feignSmartFileApi) {
        return new RemoteSmartFileApi(feignSmartFileApi);
    }
}
