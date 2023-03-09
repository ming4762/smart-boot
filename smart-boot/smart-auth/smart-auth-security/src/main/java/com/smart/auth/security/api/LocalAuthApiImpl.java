package com.smart.auth.security.api;

import com.smart.auth.core.token.TokenData;
import com.smart.auth.core.token.TokenRepository;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.commons.core.dto.auth.UserRolePermission;
import com.smart.module.api.auth.AuthApi;
import com.smart.module.api.auth.dto.AuthUserDetailsDTO;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author zhongming4762
 * 2023/3/7
 */
@Component
@Primary
public class LocalAuthApiImpl implements AuthApi {

    private final List<TokenRepository> tokenRepositoryList;

    public LocalAuthApiImpl(List<TokenRepository> tokenRepositoryList) {
        this.tokenRepositoryList = tokenRepositoryList;
    }

    /**
     * 通过token离线
     *
     * @param token token
     * @return 结果
     */
    @Override
    public boolean offlineByToken(@NonNull String token) {
        boolean result = false;
        for (TokenRepository repository : this.tokenRepositoryList) {
            result = repository.invalidateByToken(token);
            if (result) {
                break;
            }
        }
        return result;
    }

    /**
     * 通过用户名离线
     *
     * @param username 用户名
     * @return 结果
     */
    @Override
    public boolean offlineByUsername(@NonNull String username) {
        boolean result = false;
        for (TokenRepository repository : this.tokenRepositoryList) {
            result = repository.invalidateByUsername(username);
            if (result) {
                break;
            }
        }
        return result;
    }

    /**
     * 通过token查询用户信息
     *
     * @param token token
     * @return AuthUserDetailsDTO
     */
    @Override
    public AuthUserDetailsDTO getUserDetails(@NonNull String token) {
        RestUserDetails userDetails = null;
        for (TokenRepository tokenRepository : this.tokenRepositoryList) {
            userDetails = Optional.ofNullable(tokenRepository.getData(token))
                    .map(TokenData::getUser)
                    .orElse(null);
            if (userDetails != null) {
                break;
            }
        }
        if (userDetails == null) {
            return null;
        }

        UserRolePermission rolePermission = new UserRolePermission();
        rolePermission.setRoleCodes(userDetails.getRoles());
        rolePermission.setPermissions(userDetails.getPermissions());
        return AuthUserDetailsDTO.builder()
                .userId(userDetails.getUserId())
                .username(userDetails.getUsername())
                .fullName(userDetails.getFullName())
                .locale(userDetails.getLocale())
                .loginTime(userDetails.getLoginTime())
                .loginIp(userDetails.getLoginIp())
                .bindIp(userDetails.getBindIp())
                .ipWhiteList(userDetails.getIpWhiteList())
                .rolePermission(rolePermission)
                .build();
    }
}
