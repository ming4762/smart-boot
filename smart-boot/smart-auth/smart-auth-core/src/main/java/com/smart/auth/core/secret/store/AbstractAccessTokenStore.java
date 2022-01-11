package com.smart.auth.core.secret.store;

import com.google.common.collect.Lists;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.secret.data.AccessTokenStoreData;
import com.smart.auth.core.userdetails.RestUserDetails;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.NonNull;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * AccessToken保存接口
 * @author ShiZhongMing
 * @since 1.0
 */
public abstract class AbstractAccessTokenStore implements AccessTokenStore {

    private static final String APP_SECRET_KEY = "appsecret";

    private final String prefix;

    private final AuthProperties.AppsecretProperties properties;

    protected AbstractAccessTokenStore(String prefix, AuthProperties.AppsecretProperties properties) {
        this.prefix = prefix;
        this.properties = properties;
    }

    @Override
    public void save(@NonNull RestUserDetails app, @NonNull String accessToken) {
        String cacheKey = this.getPrefixKey(app.getUsername());
        // 判断是否存在
        boolean hasKey = this.has(cacheKey);
        List<AccessTokenStoreData.AccessTokenStoreDataItem> dataList = Lists.newArrayList();
        dataList.add(new AccessTokenStoreData.AccessTokenStoreDataItem(accessToken, Instant.now()));
        if (hasKey) {
            List<AccessTokenStoreData.AccessTokenStoreDataItem> cacheList = this.get(cacheKey).getAccessTokenList();
            if (CollectionUtils.isNotEmpty(cacheList)) {
                AccessTokenStoreData.AccessTokenStoreDataItem cache = cacheList.get(0);
                dataList.add(new AccessTokenStoreData.AccessTokenStoreDataItem(cache.getAccessToken(), Instant.now()));
            }
        }
        this.doSave(cacheKey, new AccessTokenStoreData(app, dataList), this.properties.getAccessTokenTimeout());
    }

    /**
     * zhi行保存AccessToken
     * @param key key
     * @param data AccessToken
     * @param expire 过期时间
     */
    protected abstract void doSave(@NonNull String key, @NonNull AccessTokenStoreData data, @NonNull Duration expire);

    @Override
    public AccessTokenStoreData validate(@NonNull String appId) {
        String cacheKey = this.getPrefixKey(appId);
        AccessTokenStoreData storeData = this.get(cacheKey);
        if (storeData == null) {
            return null;
        }
        List<AccessTokenStoreData.AccessTokenStoreDataItem> validateList = new ArrayList<>();
        List<AccessTokenStoreData.AccessTokenStoreDataItem> dataList = storeData.getAccessTokenList();
        if (CollectionUtils.isNotEmpty(dataList)) {
            AccessTokenStoreData.AccessTokenStoreDataItem first = dataList.get(0);
            // 判断是否超时（正常来说不会超时，因为有缓存超时设置）
            if (!first.getCreateTime().plus(this.properties.getAccessTokenTimeout()).isBefore(Instant.now())) {
                validateList.add(first);
            }
        }
        if (dataList.size() > 1) {
            // 判断上一个是否过期
            AccessTokenStoreData.AccessTokenStoreDataItem second = dataList.get(1);
            // 判断是否超时
            if (!second.getCreateTime().plus(this.properties.getOldAccessTokenTimeout()).isBefore(Instant.now())) {
                validateList.add(second);
            }
        }
        if (CollectionUtils.isEmpty(validateList)) {
            this.delete(cacheKey);
            return null;
        }
        storeData.setAccessTokenList(validateList);
        return storeData;
    }

    /**
     * 删除AccessToken
     * @param key key
     */
    protected abstract void delete(@NonNull String key);

    /**
     * 是否有AccessToken
     * @param key key
     * @return 是否有AccessToken
     */
    protected abstract boolean has(@NonNull String key);

    /**
     * 获取是否有AccessToken
     * @param key key
     * @return 是否有AccessToken
     */
    protected abstract AccessTokenStoreData get(@NonNull String key);

    protected String getPrefixKey(@NonNull String key) {
        return String.format("%s:%s:%s", prefix, APP_SECRET_KEY, key);
    }
}
