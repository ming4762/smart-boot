package com.smart.service.system.auth;

import com.smart.auth.core.constants.AuthTypeEnum;
import com.smart.auth.core.wechat.WechatAuthConfigProvider;
import com.smart.starter.wxjava.model.WechatAppConfig;
import com.smart.starter.wxjava.provider.WechatAppConfigProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/4/4
 */
@Component
public class PropertiesWechatAuthConfigProvider implements WechatAuthConfigProvider {

    private final WechatAppConfigProvider wechatAppConfigProvider;

    public PropertiesWechatAuthConfigProvider(WechatAppConfigProvider wechatAppConfigProvider) {
        this.wechatAppConfigProvider = wechatAppConfigProvider;
    }


    /**
     * 获取小程序配置
     *
     * @return WechatConfig
     */
    @Override
    public String getDefaultAppid(AuthTypeEnum authType) {
        List<WechatAppConfig> configList = this.wechatAppConfigProvider.get();
        if (CollectionUtils.isEmpty(configList)) {
            return null;
        }
        // 随意获取一个
        WechatAppConfig appConfig = configList.get(0);
        return appConfig.getAppid();
    }
}
