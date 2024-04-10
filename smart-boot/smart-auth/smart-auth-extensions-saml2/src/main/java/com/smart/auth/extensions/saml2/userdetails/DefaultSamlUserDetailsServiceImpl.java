package com.smart.auth.extensions.saml2.userdetails;

import com.smart.auth.core.userdetails.UserDetailsBuilder;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.dto.AuthUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

/**
 * @author ShiZhongMing
 * 2021/1/7 17:14
 * @since 1.0
 */
@RequiredArgsConstructor
public class DefaultSamlUserDetailsServiceImpl implements SAMLUserDetailsService {

    private final SystemAuthUserApi systemAuthUserApi;
    private final UserDetailsBuilder userDetailsBuilder;


    @Override
    public Object loadUserBySAML(SAMLCredential credential) {
        // 获取用户名
        String username = credential.getNameID().getValue();
        // 查询用户
        final AuthUserDTO user = this.systemAuthUserApi.getByUsername(username);
        return userDetailsBuilder.buildUserDetails(user);
    }
}
