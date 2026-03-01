package com.smart.boot.autoconfigure.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.smart.framework.commons.core.cache.CacheService;
import com.smart.framework.extension.wechat.cache.SmartWechatMaCacheServiceConfigImpl;
import com.smart.framework.extension.wechat.model.WechatMiniappConfig;
import com.smart.framework.extension.wechat.provider.DefaultWechatAppConfigPropertiesProvider;
import com.smart.framework.extension.wechat.provider.WechatminiAppConfigProvider;
import lombok.extern.slf4j.Slf4j;
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
 * 微信小程序自动配置类
 * @author zhongming4762
 * 2023/4/4
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(WxMaService.class)
@Slf4j
public class SmartWechatMiniappAutoconfiguration {

    @Bean
    @ConditionalOnMissingBean(WechatminiAppConfigProvider.class)
    public DefaultWechatAppConfigPropertiesProvider defaultWechatAppConfigPropertiesProvider(SmartWechatProperties properties) {
        return new DefaultWechatAppConfigPropertiesProvider(properties.getMinapp().getConfigs());
    }

    @Bean
    @ConditionalOnMissingBean
    public WxMaService wxMaService(SmartWechatProperties properties,
                                   WechatminiAppConfigProvider configProvider,
                                   ObjectProvider<CacheService> cacheServiceObjectProvider
    ) {
        List<WechatMiniappConfig> configList = configProvider.get();
        if (CollectionUtils.isEmpty(configList)) {
            configList = Collections.emptyList();
            log.warn("获取微信小程序配置失败，请检查配置是否正确");
        }
        WxMaService wxMaService = new WxMaServiceImpl();
        if (!CollectionUtils.isEmpty(configList)) {
            wxMaService.setMultiConfigs(this.createWxMpConfigStorageMap(properties.getKeyPrefix(), configList, cacheServiceObjectProvider));
        }
        return wxMaService;
    }

    private Map<String, WxMaConfig> createWxMpConfigStorageMap(String keyPrefix, List<WechatMiniappConfig> configList, ObjectProvider<CacheService> cacheServiceObjectProvider) {
        return configList.stream()
                .map(a -> {
                    WxMaDefaultConfigImpl config;
                    CacheService cacheService = cacheServiceObjectProvider.getIfAvailable();
                    if (cacheService != null) {
                        config = new SmartWechatMaCacheServiceConfigImpl(cacheService, keyPrefix);
                    } else {
                        config = new WxMaDefaultConfigImpl();
                    }
                    config.setAppid(a.getAppid());
                    config.setSecret(a.getSecret());
                    config.setToken(a.getToken());
                    config.setAesKey(a.getAesKey());
                    config.setMsgDataFormat(a.getMsgDataFormat());
                    return config;
                })
                .collect(Collectors.toMap(WxMaDefaultConfigImpl::getAppid, a -> a, (o, n) -> o));
    }
}
