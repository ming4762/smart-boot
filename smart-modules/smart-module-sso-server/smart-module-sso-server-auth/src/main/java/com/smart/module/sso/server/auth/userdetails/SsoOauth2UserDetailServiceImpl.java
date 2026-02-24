package com.smart.module.sso.server.auth.userdetails;

import com.smart.framework.auth.core.exception.RestUsernameNotFoundException;
import com.smart.framework.auth.core.i18n.AuthI18nMessage;
import com.smart.framework.commons.core.i18n.I18nUtils;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.dto.AuthUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 单点登录服务器-认证模块-用户详情服务实现类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-22 23:45
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class SsoOauth2UserDetailServiceImpl implements UserDetailsService {

    private final SystemAuthUserApi systemAuthUserApi;
    private final UserDetailsManagerConfigurer.UserDetailsBuilder userDetailsBuilder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUserDTO authUser = this.systemAuthUserApi.getByUsername(username);
        if (authUser == null) {
            throw new RestUsernameNotFoundException(I18nUtils.get(AuthI18nMessage.USER_NOT_FOUND_ERROR));
        }
        // 校验用户是否绑定客户端
        return null;
    }
}
