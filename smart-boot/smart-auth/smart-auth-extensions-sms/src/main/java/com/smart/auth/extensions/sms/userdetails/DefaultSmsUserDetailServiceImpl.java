package com.smart.auth.extensions.sms.userdetails;

import com.smart.auth.core.userdetails.UserDetailsBuilder;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.dto.AuthUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author zhongming4762
 * 2023/6/7
 */
@RequiredArgsConstructor
public class DefaultSmsUserDetailServiceImpl implements SmsUserDetailService {

    private final SystemAuthUserApi systemAuthUserApi;
    private final UserDetailsBuilder userDetailsBuilder;

    /**
     * 通过手机号加载用户信息
     *
     * @param mobile 手机号
     * @return 用户信息
     * @throws AuthenticationException 异常信息
     */
    @Override
    public UserDetails loadUserByMobile(String mobile) throws AuthenticationException {
        AuthUserDTO user = this.systemAuthUserApi.getByMobile(mobile);
        return userDetailsBuilder.buildUserDetails(user);
    }
}
