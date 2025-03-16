package com.smart.framework.auth.core.token;

import org.springframework.lang.NonNull;

import java.util.List;

/**
 * token存储器
 * @author shizhongming
 * 2025/3/13 17:13
 * @since 5.0.0
 */
public interface SmartTokenRepository {

    /**
     * 生成token
     * @return token
     */
    String generateToken();

    /**
     * 查询所有数据
     * @return jwt数据
     */
    @NonNull
    List<TokenCacheData> listToken();

    /**
     * 通过用户名查询token
     * @param username 用户名
     * @param tenantId 租户ID
     * @return token
     */
    @NonNull
    List<TokenCacheData> listToken(String username, Long tenantId);
}
