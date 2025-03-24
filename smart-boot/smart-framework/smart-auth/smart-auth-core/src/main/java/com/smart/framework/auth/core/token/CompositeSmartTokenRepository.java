package com.smart.framework.auth.core.token;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 基于组合模式代理多个SmartTokenRepository
 * @author shizhongming
 * 2025/3/24 20:23
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class CompositeSmartTokenRepository implements SmartTokenRepository {

    private final List<SmartTokenRepository> tokenRepositoryList;

    /**
     * 生成token
     *
     * @return token
     */
    @Override
    public String generateToken() {
        return this.forGet(SmartTokenRepository::generateToken);
    }

    /**
     * 查询所有数据
     *
     * @return jwt数据
     */
    @Override
    @NonNull
    public List<TokenCacheData> listToken() {
        return this.allGet(SmartTokenRepository::listToken);
    }

    /**
     * 通过用户名查询token
     *
     * @param username 用户名
     * @param tenantId 租户ID
     * @return token
     */
    @Override
    @NonNull
    public List<TokenCacheData> listToken(String username, Long tenantId) {
        return this.allGet(repository -> repository.listToken(username, tenantId));
    }

    /**
     * 获取用户缓存数据
     *
     * @param attributeName 属性名称
     * @return 属性值
     */
    @Override
    public <T> T getAttribute(String attributeName) {
        return this.forGet(repository -> repository.getAttribute(attributeName));
    }

    /**
     * 设置用户缓存数据
     *
     * @param attributeName  属性名称
     * @param attributeValue 属性值
     */
    @Override
    public boolean setAttribute(String attributeName, Object attributeValue) {
        return this.forGetBoolean(repository -> repository.setAttribute(attributeName, attributeValue));
    }

    /**
     * 使token失效
     *
     * @param token token
     * @return 是否失效成功
     */
    @Override
    public boolean invalidateByToken(String token) {
        return this.forGetBoolean(repository -> repository.invalidateByToken(token));
    }

    /**
     * 使用户登录失效
     *
     * @param tenantId 租户ID
     * @param username 用户名
     * @return 是否失效成功
     */
    @Override
    public boolean invalidateByUsername(Long tenantId, String username) {
        return this.forGetBoolean(repository -> repository.invalidateByUsername(tenantId, username));
    }

    /**
     * 通过token获取用户信息
     *
     * @param token token
     * @return 用户信息
     */
    @Override
    public RestUserDetails getUserByToken(String token) {
        return this.forGet(repository -> repository.getUserByToken(token));
    }

    protected <T> T forGet(Function<SmartTokenRepository, T> handler) {
        T result = null;
        for (SmartTokenRepository tokenRepository : this.tokenRepositoryList) {
            result = handler.apply(tokenRepository);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    protected <T> List<T> allGet(Function<SmartTokenRepository, List<T>> handler) {
        List<T> resultList = new ArrayList<>(10);
        for (SmartTokenRepository tokenRepository : this.tokenRepositoryList) {
            List<T> applyList = handler.apply(tokenRepository);
            if (!CollectionUtils.isEmpty(applyList)) {
                resultList.addAll(applyList);
            }
        }
        return resultList;
    }

    protected boolean forGetBoolean(Predicate<SmartTokenRepository> handler) {
        boolean result = false;
        for (SmartTokenRepository tokenRepository : this.tokenRepositoryList) {
            result = handler.test(tokenRepository);
            if (Boolean.TRUE.equals(result)) {
                break;
            }
        }
        return result;
    }
}
