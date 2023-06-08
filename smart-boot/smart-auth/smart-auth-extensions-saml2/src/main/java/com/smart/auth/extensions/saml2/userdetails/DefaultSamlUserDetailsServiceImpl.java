package com.smart.auth.extensions.saml2.userdetails;

import com.smart.auth.core.constants.AuthTypeEnum;
import com.smart.auth.core.model.RestUserDetailsImpl;
import com.smart.auth.core.token.TokenRepository;
import com.smart.auth.core.userdetails.AbstractUserDetailsService;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.dto.AuthUser;
import com.smart.module.api.system.dto.AuthUserDTO;
import org.springframework.lang.NonNull;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/1/7 17:14
 * @since 1.0
 */
public class DefaultSamlUserDetailsServiceImpl extends AbstractUserDetailsService implements SAMLUserDetailsService {

    private final SystemAuthUserApi systemAuthUserApi;

    protected DefaultSamlUserDetailsServiceImpl(List<TokenRepository> tokenRepositoryList, SystemAuthUserApi systemAuthUserApi) {
        super(tokenRepositoryList, systemAuthUserApi);
        this.systemAuthUserApi = systemAuthUserApi;
    }


    @Override
    public Object loadUserBySAML(SAMLCredential credential) {
        // 获取用户名
        String username = credential.getNameID().getValue();
        // 查询用户
        final AuthUserDTO user = this.systemAuthUserApi.getByUsername(username);
        return this.getUserDetails(user);
    }
}
