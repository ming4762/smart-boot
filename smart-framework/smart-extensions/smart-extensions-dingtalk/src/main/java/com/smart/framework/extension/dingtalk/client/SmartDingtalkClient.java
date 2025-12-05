package com.smart.framework.extension.dingtalk.client;

import com.smart.framework.extension.dingtalk.constants.DingtalkClientTypeEnum;

import java.time.Duration;
import java.util.concurrent.locks.Lock;

/**
 * 钉钉应用配置服务类
 * 保存每个应用的信息，以及应用别相关信息，例如应用token
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/1 21:16
 * @since 5.0.0
 */
public interface SmartDingtalkClient {

    /**
     * 获取应用ID
     * 钉钉企业内部应用和钉钉第三方企业应用的唯一身份标识
     * @return 应用ID
     */
    String getClientId();

     /**
      * 获取应用密钥
      * 用于对API请求进行身份验证和授权
      * @return 应用密钥
      */
    String getClientSecret();

    /**
     * 获取应用类型
     * @return 应用类型
     */
    DingtalkClientTypeEnum getClientType();

    /**
     * 获取应用token
     * 用于调用钉钉API时进行身份验证
     * @return 应用token
     */
    String getAccessToken();

    /**
     * 更新应用token
     * @param accessToken 新的应用token
     * @param expireIn 应用token的有效期
     */
    void updateAccessToken(String accessToken, Duration expireIn);

     /**
      * 获取应用token的锁
      * 用于在多线程环境下确保对应用token申请的线程安全
      * @return 应用token的锁
      */
    Lock getAccessTokenLock();

    /**
     * 检查应用token是否过期
     * @return true表示过期，false表示未过期
     */
    boolean isAccessTokenExpired();
}
