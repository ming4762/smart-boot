package com.smart.boot.autoconfigure.dingtalk;

import com.smart.framework.commons.core.cache.CacheService;
import com.smart.framework.extension.dingtalk.DingtalkApi;
import com.smart.framework.extension.dingtalk.api.AccessSecureApi;
import com.smart.framework.extension.dingtalk.api.UserApi;
import com.smart.framework.extension.dingtalk.api.WorkNoticeApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2024/4/26 17:30
 * @since 3.0.0
 */
@Configuration(proxyBeanMethods = false)
public class SmartDingtalkAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AccessSecureApi dingtalkAccessSecureApi(CacheService cacheService) {
        return new AccessSecureApi(cacheService);
    }

    @Bean
    @ConditionalOnMissingBean
    public UserApi dingtalkUserApi(AccessSecureApi accessSecureApi) {
        return new UserApi(accessSecureApi);
    }

    @Bean
    @ConditionalOnMissingBean
    public WorkNoticeApi dingtalkWorkNoticeApi(AccessSecureApi accessSecureApi) {
        return new WorkNoticeApi(accessSecureApi);
    }

    @Bean
    public DingtalkApi dingtalkApi() {
        return new DingtalkApi();
    }
}
