package com.smart.auth.extensions.sms.userdetails;

import com.smart.auth.core.token.TokenRepository;
import com.smart.auth.core.userdetails.AbstractUserDetailsService;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.dto.AuthUserDTO;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/6/7
 */
public class DefaultSmsUserDetailServiceImpl extends AbstractUserDetailsService implements SmsUserDetailService {

    private final SystemAuthUserApi systemAuthUserApi;

    public DefaultSmsUserDetailServiceImpl(List<TokenRepository> tokenRepositoryList, SystemAuthUserApi systemAuthUserApi) {
        super(tokenRepositoryList, systemAuthUserApi);
        this.systemAuthUserApi = systemAuthUserApi;
    }

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
        return this.getUserDetails(user);
    }
}
