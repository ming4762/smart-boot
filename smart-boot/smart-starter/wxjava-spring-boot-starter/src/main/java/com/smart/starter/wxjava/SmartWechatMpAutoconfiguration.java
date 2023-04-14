package com.smart.starter.wxjava;

import com.smart.starter.wxjava.model.WechatMpConfig;
import com.smart.starter.wxjava.properties.WechatMpProperties;
import com.smart.starter.wxjava.provider.DefaultWechatMpConfigPropertiesProvider;
import com.smart.starter.wxjava.provider.WechatMpConfigProvider;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
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
    public WechatMpProperties wechatMpProperties() {
        return new WechatMpProperties();
    }

    @Bean
    @ConditionalOnMissingBean(WechatMpConfigProvider.class)
    public DefaultWechatMpConfigPropertiesProvider defaultWechatMpConfigPropertiesProvider(WechatMpProperties properties) {
        return new DefaultWechatMpConfigPropertiesProvider(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public WxMpService wxMpService(WechatMpConfigProvider configProvider) {
        List<WechatMpConfig> configList = configProvider.get();
        if (CollectionUtils.isEmpty(configList)) {
            configList = Collections.emptyList();
            log.warn("获取微信公众号配置失败，请检查配置是否正确");
        }
        WxMpService service = new WxMpServiceImpl();
        if (!CollectionUtils.isEmpty(configList)) {
            service.setMultiConfigStorages(
                    configList.stream()
                            .map(a -> {
                                WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
                                config.setAppId(a.getAppid());
                                config.setSecret(a.getSecret());
                                config.setToken(a.getToken());
                                config.setAesKey(a.getAesKey());
                                return config;
                            }).collect(Collectors.toMap(WxMpDefaultConfigImpl::getAppId, a -> a, (o, n) -> o)));
        }
        return service;
    }
}
