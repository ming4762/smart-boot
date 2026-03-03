package com.smart.framework.extension.wechat.config;

import cn.binarywang.wx.miniapp.config.WxMaConfig;
import com.smart.framework.extension.wechat.model.WechatMiniappConfig;
import com.smart.framework.extension.wechat.model.WechatMpConfig;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;

/**
 * 微信配置存储创建器
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026/3/3 15:40
 * @since 5.0.0
 */
public interface SmartWechatConfigStorageCreator {

    /**
     * 创建微信服务号配置存储
     * @param config 微信服务号配置
     * @return 微信服务号配置存储
     */
    WxMpConfigStorage createMpConfigStorage(WechatMpConfig config);

    /**
     * 创建微信小程序配置存储
     * @param config 微信小程序配置
     * @return 微信小程序配置存储
     */
    WxMaConfig createMiniappConfigStorage(WechatMiniappConfig config);
}
