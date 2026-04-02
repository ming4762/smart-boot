package com.smart.boot.dingtalk;

import com.smart.framework.extension.dingtalk.BaseDingtalkApiImpl;
import com.smart.framework.extension.dingtalk.DingtalkApi;
import com.smart.framework.extension.dingtalk.api.AccessSecureApi;
import com.smart.framework.extension.dingtalk.api.UserApi;
import com.smart.framework.extension.dingtalk.api.WorkNoticeApi;
import com.smart.framework.extension.dingtalk.client.SmartDingtalkClientHolder;
import com.smart.framework.extension.dingtalk.client.impl.DefaultSmartDingtalkClientHolderImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2024/4/26 17:30
 * @since 3.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DingtalkApi.class)
public class SmartDingtalkAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AccessSecureApi dingtalkAccessSecureApi(SmartDingtalkClientHolder smartDingtalkClientHolder) {
        return new AccessSecureApi(smartDingtalkClientHolder);
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
    public DingtalkApi dingtalkApi(SmartDingtalkClientHolder smartDingtalkClientHolder) {
        return new BaseDingtalkApiImpl(smartDingtalkClientHolder);
    }

    @Bean
    @ConditionalOnMissingBean
    public SmartDingtalkClientHolder smartDingtalkClientHolder() {
        return new DefaultSmartDingtalkClientHolderImpl();
    }
}
