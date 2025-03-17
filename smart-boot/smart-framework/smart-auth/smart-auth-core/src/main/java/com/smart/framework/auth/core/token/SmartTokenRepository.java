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

    /**
     * 获取用户缓存数据
     * @param attributeName 属性名称
     * @return 属性值
     * @param <T> 属性值类型
     */
    <T> T getAttribute(String attributeName);

    /**
     * 设置用户缓存数据
     * @param attributeName 属性名称
     * @param attributeValue 属性值
     */
    void setAttribute(String attributeName, Object attributeValue);

}
