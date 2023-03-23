package com.smart.auth.core.temptoken;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ShiZhongMing
 * 2021/3/10 12:34
 * @since 1.0
 */
@Configuration
public class AuthTempTokenConfig implements WebMvcConfigurer {

    private final TempTokenValidateInterceptor tempTokenValidateInterceptor;

    public AuthTempTokenConfig(TempTokenValidateInterceptor tempTokenValidateInterceptor) {
        this.tempTokenValidateInterceptor = tempTokenValidateInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.tempTokenValidateInterceptor).addPathPatterns("/**");
    }

}
