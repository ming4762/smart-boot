package com.smart.framework.auth.core.temptoken;

import com.smart.module.api.auth.AuthApi;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2021/3/10 12:38
 * @since 1.0
 */
@Configuration
public class AuthTempTokenBeanConfig {

    @Bean
    @ConditionalOnMissingBean(TempTokenValidateInterceptor.class)
    public TempTokenValidateInterceptor tempTokenValidateInterceptor(ObjectProvider<AuthApi> authApi) {
        return new TempTokenValidateInterceptor(authApi);
    }
}
