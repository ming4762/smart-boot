package com.smart.auth.extensions.jwt.userdetails;

import com.smart.auth.core.token.TokenRepository;
import com.smart.auth.core.userdetails.AbstractUserDetailsService;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.dto.AuthUserDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/6/7
 */
public class RestUserDetailsServiceImpl extends AbstractUserDetailsService implements UserDetailsService {

    private final SystemAuthUserApi systemAuthUserApi;

    public RestUserDetailsServiceImpl(List<TokenRepository> tokenRepositoryList, SystemAuthUserApi systemAuthUserApi) {
        super(tokenRepositoryList, systemAuthUserApi);
        this.systemAuthUserApi = systemAuthUserApi;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        AuthUserDTO authUser = this.systemAuthUserApi.getByUsername(username);
        return this.getUserDetails(authUser);
    }
}
