package com.smart.service.auth.server.manager.config;

import com.smart.framework.auth.core.handler.AuthAccessDeniedHandler;
import com.smart.framework.auth.core.handler.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Set;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 *
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/14 14:50
 * @since
 */
@Configuration
//@EnableWebSecurity
public class AuthorizationServerConfig {

//    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServer = new OAuth2AuthorizationServerConfigurer();
        http.securityMatcher(authorizationServer.getEndpointsMatcher());
        http.with(authorizationServer, withDefaults());
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(withDefaults());
        http.oauth2ResourceServer(resourceServer-> resourceServer.jwt(withDefaults()));
        http.exceptionHandling(
                configurer -> configurer.authenticationEntryPoint(new RestAuthenticationEntryPoint())
                        .accessDeniedHandler(new AuthAccessDeniedHandler()));
//        http.exceptionHandling(exceptions -> exceptions.defaultAuthenticationEntryPointFor(
//                new LoginUrlAuthenticationEntryPoint("/login"), createRequestMatcher()));
        return http.build();
    }

//    @Bean
//    @Order(SecurityProperties.BASIC_AUTH_ORDER)
//    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated()).formLogin(withDefaults());
//        return http.build();
//    }

    private static RequestMatcher createRequestMatcher() {
        MediaTypeRequestMatcher requestMatcher = new MediaTypeRequestMatcher(MediaType.TEXT_HTML);
        requestMatcher.setIgnoredMediaTypes(Set.of(MediaType.ALL));
        return requestMatcher;
    }
}
