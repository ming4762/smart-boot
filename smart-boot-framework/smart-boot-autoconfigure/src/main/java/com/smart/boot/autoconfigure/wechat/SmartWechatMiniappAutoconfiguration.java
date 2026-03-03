package com.smart.boot.autoconfigure.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import com.smart.framework.extension.wechat.config.SmartWechatConfigStorageCreator;
import com.smart.framework.extension.wechat.model.WechatMiniappConfig;
import com.smart.framework.extension.wechat.provider.DefaultWechatAppConfigPropertiesProvider;
import com.smart.framework.extension.wechat.provider.WechatminiAppConfigProvider;
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
public class SmartWechatMiniappAutoconfiguration {

    @Bean
    @ConditionalOnMissingBean(WechatminiAppConfigProvider.class)
    public DefaultWechatAppConfigPropertiesProvider defaultWechatAppConfigPropertiesProvider(SmartWechatProperties properties) {
        return new DefaultWechatAppConfigPropertiesProvider(properties.getMinapp().getConfigs());
    }

    @Bean
    @ConditionalOnMissingBean
    public WxMaService wxMaService(
            WechatminiAppConfigProvider configProvider,
            SmartWechatConfigStorageCreator configStorageCreator
    ) {
        List<WechatMiniappConfig> configList = configProvider.get();
        if (CollectionUtils.isEmpty(configList)) {
            configList = Collections.emptyList();
            log.warn("获取微信小程序配置失败，请检查配置是否正确");
        }
        WxMaService wxMaService = new WxMaServiceImpl();
        if (!CollectionUtils.isEmpty(configList)) {
            wxMaService.setMultiConfigs(
                    configList.stream()
                            .map(configStorageCreator::createMiniappConfigStorage)
                            .collect(Collectors.toMap(WxMaConfig::getAppid, a -> a))
            );
        }
        return wxMaService;
    }
}
