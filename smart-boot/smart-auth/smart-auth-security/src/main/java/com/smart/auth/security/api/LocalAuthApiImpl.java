package com.smart.auth.security.api;

import com.smart.auth.core.api.AuthApi;
import com.smart.auth.core.token.TokenRepository;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/3/7
 */
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
}
