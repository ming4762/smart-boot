package com.smart.cloud.api.file;

import com.smart.cloud.api.file.feign.FeignSmartFileApi;
import com.smart.cloud.api.file.feign.RemoteSmartFileApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongming4762
 * 2023/3/22
 */
@Configuration(proxyBeanMethods = false)
public class SmartCloudFileApiAutoConfiguration {

    @Bean
    public RemoteSmartFileApi remoteSmartFileApi(FeignSmartFileApi feignSmartFileApi) {
        return new RemoteSmartFileApi(feignSmartFileApi);
    }
}
