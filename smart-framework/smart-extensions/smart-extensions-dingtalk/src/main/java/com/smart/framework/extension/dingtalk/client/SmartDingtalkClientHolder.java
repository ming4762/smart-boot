package com.smart.framework.extension.dingtalk.client;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * 钉钉应用配置服务类持有者接口
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/1 22:07
 * @since 5.0.0
 */
public interface SmartDingtalkClientHolder {

    /**
     * 切换到指定的钉钉应用
     * @param clientId 钉钉应用名称
     * @return 是否切换成功
     */
    boolean switchover(String clientId);


    /**
     * 获取指定的钉钉应用
     * @param clientId 钉钉应用ID
     * @return 钉钉应用
     */
    @Nullable
    SmartDingtalkClient getClient(String clientId);

     /**
      * 获取当前正在使用的钉钉应用
      * @return 钉钉应用
      */
    @NonNull
    SmartDingtalkClient getClient();
}
