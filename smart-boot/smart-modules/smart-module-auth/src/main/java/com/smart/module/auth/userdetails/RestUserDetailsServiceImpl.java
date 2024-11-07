package com.smart.module.auth.userdetails;

import com.smart.framework.auth.core.userdetails.UserDetailsBuilder;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.dto.AuthUserDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author zhongming4762
 * 2023/6/7
 */
@RequiredArgsConstructor
public class RestUserDetailsServiceImpl implements UserDetailsService {

    private final SystemAuthUserApi systemAuthUserApi;
    private final UserDetailsBuilder userDetailsBuilder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        AuthUserDTO authUser = this.systemAuthUserApi.getByUsername(username);
        return this.userDetailsBuilder.buildUserDetails(authUser);
    }
}
