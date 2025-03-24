package com.smart.cloud.starter.auth.userdetails;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 自动屏蔽自动装配 org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
 * 微服务模式下UserDetailsServiceAutoConfiguration无意义
 * @author shizhongming
 * 2025/1/12 18:09
 * @since 5.0.0
 */
public class UnSupportUserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("不支持用户名密码登录");
    }
}
