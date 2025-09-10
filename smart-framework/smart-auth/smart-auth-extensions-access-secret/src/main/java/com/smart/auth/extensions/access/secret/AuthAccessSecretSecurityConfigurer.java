package com.smart.auth.extensions.access.secret;

import com.smart.auth.extensions.access.secret.filter.AuthAccessSecretAuthenticationFilter;
import com.smart.framework.auth.core.config.SmartSecurityConfigurerAdapter;
import com.smart.framework.auth.core.secret.AccessSecretProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.access.ExceptionTranslationFilter;

/**
 * @author shizhongming
 * 2023/10/25 15:35
 * @since 3.0.0
 */
public class AuthAccessSecretSecurityConfigurer extends SmartSecurityConfigurerAdapter<HttpSecurity> {

    public static AuthAccessSecretSecurityConfigurer build() {
        return new AuthAccessSecretSecurityConfigurer();
    }

    public HttpSecurity config(Customizer<AuthAccessSecretSecurityConfigurer> customizer) {
        customizer.customize(this);
        return this.getBuilder();
    }

    @Override
    public void configure(HttpSecurity builder) {
        builder.addFilterAfter(
                        new AuthAccessSecretAuthenticationFilter(
                                this.getAuthProperties(),

                                this.getBean(AccessSecretProvider.class, null)
                        ),
                        ExceptionTranslationFilter.class);
    }

    @Override
    public void init(HttpSecurity builder) {
        AuthenticationManagerBuilder authenticationManagerBuilder = builder.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.parentAuthenticationManager(null);
    }
}
