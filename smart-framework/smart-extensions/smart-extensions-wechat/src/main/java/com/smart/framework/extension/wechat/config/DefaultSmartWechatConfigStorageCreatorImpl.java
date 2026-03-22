package com.smart.framework.extension.wechat.config;

import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.smart.framework.commons.core.cache.CacheService;
import com.smart.framework.extension.wechat.cache.SmartWechatMaCacheServiceConfigImpl;
import com.smart.framework.extension.wechat.cache.SmartWechatMpCacheServiceConfigImpl;
import com.smart.framework.extension.wechat.model.WechatMiniappConfig;
import com.smart.framework.extension.wechat.model.WechatMpConfig;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.ObjectProvider;

/**
 *
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-03 15:44
 * @since 5.0.0
 */
public class DefaultSmartWechatConfigStorageCreatorImpl implements SmartWechatConfigStorageCreator{

    private final String keyPrefix;
    private final ObjectProvider<CacheService> cacheServiceProvider;

    public DefaultSmartWechatConfigStorageCreatorImpl(String keyPrefix, ObjectProvider<CacheService> cacheServiceProvider) {
        this.keyPrefix = keyPrefix;
        this.cacheServiceProvider = cacheServiceProvider;
    }

    /**
     * 创建微信服务号配置存储
     *
     * @param config 微信服务号配置
     * @return 微信服务号配置存储
     */
    @Override
    public WxMpConfigStorage createMpConfigStorage(WechatMpConfig config) {
        WxMpDefaultConfigImpl configStorage;
        CacheService cacheService = cacheServiceProvider.getIfAvailable();
        if (cacheService != null) {
            configStorage = new SmartWechatMpCacheServiceConfigImpl(cacheService, keyPrefix);
        } else {
            configStorage = new WxMpDefaultConfigImpl();
        }
        configStorage.setAppId(config.getAppid());
        configStorage.setSecret(config.getSecret());
        configStorage.setToken(config.getToken());
        configStorage.setAesKey(config.getAesKey());
        return configStorage;
    }

    /**
     * 创建微信小程序配置存储
     *
     * @param config 微信小程序配置
     * @return 微信小程序配置存储
     */
    @Override
    public WxMaConfig createMiniappConfigStorage(WechatMiniappConfig config) {
        WxMaDefaultConfigImpl configStorage;
        CacheService cacheService = cacheServiceProvider.getIfAvailable();
        if (cacheService != null) {
            configStorage = new SmartWechatMaCacheServiceConfigImpl(cacheService, this.keyPrefix);
        } else {
            configStorage = new WxMaDefaultConfigImpl();
        }
        configStorage.setAppid(config.getAppid());
        configStorage.setSecret(config.getSecret());
        configStorage.setToken(config.getToken());
        configStorage.setAesKey(config.getAesKey());
        configStorage.setMsgDataFormat(config.getMsgDataFormat());
        return configStorage;
    }
}
