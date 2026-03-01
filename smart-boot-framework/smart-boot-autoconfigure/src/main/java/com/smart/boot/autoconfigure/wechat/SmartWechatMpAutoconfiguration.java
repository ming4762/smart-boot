package com.smart.boot.autoconfigure.wechat;

import com.smart.framework.commons.core.cache.CacheService;
import com.smart.framework.extension.wechat.cache.SmartWechatMpCacheServiceConfigImpl;
import com.smart.framework.extension.wechat.model.WechatMpConfig;
import com.smart.framework.extension.wechat.provider.DefaultWechatMpConfigPropertiesProvider;
import com.smart.framework.extension.wechat.provider.WechatMpConfigProvider;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhongming4762
 * 2023/4/7
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(WxMpService.class)
@Slf4j
public class SmartWechatMpAutoconfiguration {

    @Bean
    @ConditionalOnMissingBean(WechatMpConfigProvider.class)
    public DefaultWechatMpConfigPropertiesProvider defaultWechatMpConfigPropertiesProvider(SmartWechatProperties properties) {
        return new DefaultWechatMpConfigPropertiesProvider(properties.getMp().getConfigs());
    }

    @Bean
    @ConditionalOnMissingBean
    public WxMpService wxMpService(SmartWechatProperties properties,
                                   WechatMpConfigProvider configProvider,
                                   ObjectProvider<CacheService> cacheServiceObjectProvider
    ) {
        List<WechatMpConfig> configList = configProvider.get();
        if (CollectionUtils.isEmpty(configList)) {
            configList = Collections.emptyList();
            log.warn("获取微信公众号配置失败，请检查配置是否正确");
        }
        WxMpService service = new WxMpServiceImpl();
        if (!CollectionUtils.isEmpty(configList)) {
            service.setMultiConfigStorages(this.createWxMpConfigStorageMap(properties.getKeyPrefix(), configList, cacheServiceObjectProvider));
        }
        return service;
    }

    private Map<String, WxMpConfigStorage> createWxMpConfigStorageMap(String keyPrefix, List<WechatMpConfig> configList, ObjectProvider<CacheService> cacheServiceObjectProvider) {
        return configList.stream()
                .map(a -> {
                    WxMpDefaultConfigImpl config;
                    CacheService cacheService = cacheServiceObjectProvider.getIfAvailable();
                    if (cacheService != null) {
                        config = new SmartWechatMpCacheServiceConfigImpl(cacheService, keyPrefix);
                    } else {
                        config = new WxMpDefaultConfigImpl();
                    }
                    config.setAppId(a.getAppid());
                    config.setSecret(a.getSecret());
                    config.setToken(a.getToken());
                    config.setAesKey(a.getAesKey());
                    return config;
                })
                .collect(Collectors.toMap(WxMpDefaultConfigImpl::getAppId, a -> a, (o, n) -> o));
    }
}
