package com.smart.auth.extensions.access.secret;

import com.smart.auth.extensions.access.secret.filter.AuthAccessSecretAuthenticationFilter;
import com.smart.framework.auth.core.config.SmartSecurityConfigurerAdapter;
import com.smart.framework.auth.core.secret.AccessSecretProvider;
import com.smart.framework.auth.core.service.AuthCache;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.access.ExceptionTranslationFilter;

/**
 * @author shizhongming
 * 2023/10/25 15:35
 * @since 3.0.0
 */
public class AuthAccessSecretSecurityConfigurer<H extends HttpSecurityBuilder<H>> extends SmartSecurityConfigurerAdapter<H, AuthAccessSecretSecurityConfigurer<H>> {

    public static <H extends HttpSecurityBuilder<H>> AuthAccessSecretSecurityConfigurer<H> build() {
        return new AuthAccessSecretSecurityConfigurer<>();
    }

    public H config(Customizer<AuthAccessSecretSecurityConfigurer<H>> customizer) {
        customizer.customize(this);
        return this.getBuilder();
    }

    @Override
    public void configure(H builder) {
        builder.addFilterAfter(
                        new AuthAccessSecretAuthenticationFilter(
                                this.getAuthProperties(),
                                this.getBean(AccessSecretProvider.class, null),
                                this.getBean(AuthCache.class, null)
                        ),
                        ExceptionTranslationFilter.class);
    }

    @Override
    public void init(H builder) {
        AuthenticationManagerBuilder authenticationManagerBuilder = builder.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.parentAuthenticationManager(null);
    }
}
