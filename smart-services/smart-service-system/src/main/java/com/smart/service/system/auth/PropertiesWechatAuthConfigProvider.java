package com.smart.service.system.auth;

import com.smart.framework.auth.core.wechat.WechatAuthConfigProvider;
import org.springframework.stereotype.Component;

/**
 * @author zhongming4762
 * 2023/4/4
 */
@Component
public class PropertiesWechatAuthConfigProvider implements WechatAuthConfigProvider {

//    private final   wechatAppConfigProvider;
//
//    public PropertiesWechatAuthConfigProvider(WechatAppConfigProvider wechatAppConfigProvider) {
//        this.wechatAppConfigProvider = wechatAppConfigProvider;
//    }


    /**
     * 获取小程序配置
     *
     * @return WechatConfig
     */
//    @Override
//    public String getDefaultAppid(AuthTypeEnum authType) {
//        List<WechatAppConfig> configList = this.wechatAppConfigProvider.get();
//        if (CollectionUtils.isEmpty(configList)) {
//            return null;
//        }
//        // 随意获取一个
//        WechatAppConfig appConfig = configList.get(0);
//        return appConfig.getAppid();
//    }
}
