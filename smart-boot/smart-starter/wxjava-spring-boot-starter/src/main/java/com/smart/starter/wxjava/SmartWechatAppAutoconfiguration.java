package com.smart.starter.wxjava;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.smart.starter.wxjava.model.WechatAppConfig;
import com.smart.starter.wxjava.properties.WechatAppProperties;
import com.smart.starter.wxjava.provider.DefaultWechatAppConfigPropertiesProvider;
import com.smart.starter.wxjava.provider.WechatAppConfigProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 微信小程序自动配置类
 * @author zhongming4762
 * 2023/4/4
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(WxMaService.class)
@Slf4j
public class SmartWechatAppAutoconfiguration {


    @Bean
    public WechatAppProperties wxMaProperties() {
        return new WechatAppProperties();
    }

    @Bean
    @ConditionalOnMissingBean(WechatAppConfigProvider.class)
    public DefaultWechatAppConfigPropertiesProvider defaultWechatAppConfigPropertiesProvider(WechatAppProperties properties) {
        return new DefaultWechatAppConfigPropertiesProvider(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public WxMaService wxMaService(WechatAppConfigProvider configProvider) {
        List<WechatAppConfig> configList = configProvider.get();
        if (CollectionUtils.isEmpty(configList)) {
            configList = Collections.emptyList();
            log.warn("获取微信小程序配置失败，请检查配置是否正确");
        }
        WxMaService wxMaService = new WxMaServiceImpl();
        if (!CollectionUtils.isEmpty(configList)) {
            wxMaService.setMultiConfigs(
                    configList.stream()
                            .map(a -> {
                                WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
                                config.setAppid(a.getAppid());
                                config.setSecret(a.getSecret());
                                config.setToken(a.getToken());
                                config.setAesKey(a.getAesKey());
                                config.setMsgDataFormat(a.getMsgDataFormat());
                                return config;
                            }).collect(Collectors.toMap(WxMaDefaultConfigImpl::getAppid, a -> a, (o, n) -> o)));
        }
        return wxMaService;
    }
}
