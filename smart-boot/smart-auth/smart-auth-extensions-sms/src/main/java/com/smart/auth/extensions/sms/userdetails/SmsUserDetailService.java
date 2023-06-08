package com.smart.auth.extensions.sms.userdetails;

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
     * @param mobile 手机号
     * @return 用户信息
     * @throws AuthenticationException 异常信息
     */
    UserDetails loadUserByMobile(String mobile) throws AuthenticationException;
}
