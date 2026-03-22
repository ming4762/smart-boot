package com.smart.module.auth.userdetails;

import com.smart.framework.auth.core.exception.RestUsernameNotFoundException;
import com.smart.framework.auth.core.i18n.AuthI18nMessage;
import com.smart.framework.auth.core.userdetails.UserDetailsBuilder;
import com.smart.framework.commons.core.i18n.I18nUtils;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.dto.AuthUserDTO;
import lombok.RequiredArgsConstructor;
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
        AuthUserDTO authUser = this.systemAuthUserApi.getByUsername(username);
        if (authUser == null) {
            throw new RestUsernameNotFoundException(I18nUtils.get(AuthI18nMessage.USER_NOT_FOUND_ERROR));
        }
        return this.userDetailsBuilder.buildUserDetails(authUser);
    }
}
