package com.smart.auth.core.authentication;

import com.google.common.collect.ImmutableMap;
import com.smart.auth.core.event.AuthenticationFailureIpBindEvent;
import com.smart.auth.core.exception.IpBindAuthenticationException;
import com.smart.auth.core.exception.LongTimeNoLoginLockedException;
import com.smart.auth.core.exception.PasswordNoLifeLockedException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;

/**
 * @author ShiZhongMing
 * 2021/12/29 15:24
 * @since 1.0.7
 */
public class AuthenticationFailureEventInitializer implements ApplicationRunner {


    private final DefaultAuthenticationEventPublisher eventPublisher;

    public AuthenticationFailureEventInitializer(DefaultAuthenticationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void run(ApplicationArguments args) {
        eventPublisher.setAdditionalExceptionMappings(
                ImmutableMap.of(
                        // ip绑定认证异常
                        IpBindAuthenticationException.class, AuthenticationFailureIpBindEvent.class,
                        LongTimeNoLoginLockedException.class, AuthenticationFailureLockedEvent.class,
                        PasswordNoLifeLockedException.class, AuthenticationFailureLockedEvent.class
                )
        );
    }
}
