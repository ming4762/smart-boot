package com.smart.auth.core.userdetails;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author ShiZhongMing
 * 2021/6/3 14:19
 * @since 1.0
 */
public interface SmsUserDetailService {

    /**
     * 通过手机号加载用户信息
     * @param phone 手机号
     * @return 用户信息
     * @throws AuthenticationException 异常信息
     */
    UserDetails loadUserByPhone(String phone) throws AuthenticationException;
}
