package com.smart.framework.extension.dingtalk.client.impl;

import com.smart.framework.extension.dingtalk.client.SmartDingtalkClient;
import com.smart.framework.extension.dingtalk.client.SmartDingtalkClientHolder;
import com.smart.framework.extension.dingtalk.client.SmartDingtalkClientIdHolder;
import com.smart.framework.extension.dingtalk.exception.DingtalkApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认钉钉应用配置服务类持有者接口实现类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/1 22:08
 * @since 5.0.0
 */
@Slf4j
public class DefaultSmartDingtalkClientHolderImpl implements SmartDingtalkClientHolder {

    /**
     * 钉钉配置Map
     */
    private final Map<String, SmartDingtalkClient> dingtalkConfigMap = new ConcurrentHashMap<>(10);

    /**
     * 切换到指定的钉钉应用
     *
     * @param clientId 钉钉应用名称
     * @return 是否切换成功
     */
    @Override
    public boolean switchover(String clientId) {
        if (!dingtalkConfigMap.containsKey(clientId)) {
            log.error("钉钉应用[{}]不存在", clientId);
            return false;
        }
        SmartDingtalkClientIdHolder.set(clientId);
        return true;
    }

    /**
     * 获取指定的钉钉应用
     *
     * @param clientId 钉钉应用ID
     * @return 钉钉应用
     */
    @Override
    @Nullable
    public SmartDingtalkClient getClient(String clientId) {
        return dingtalkConfigMap.get(clientId);
    }

    /**
     * 获取当前正在使用的钉钉应用
     *
     * @return 钉钉应用
     */
    @Override
    @NonNull
    public SmartDingtalkClient getClient() {
        if (dingtalkConfigMap.size() == 1) {
            return dingtalkConfigMap.values().iterator().next();
        }
        String currentClientId = SmartDingtalkClientIdHolder.get();
        SmartDingtalkClient smartDingtalkClient = this.getClient(currentClientId);
        if (smartDingtalkClient == null) {
            throw new DingtalkApiException("当前正在使用的钉钉应用[{}]不存在", currentClientId);
        }
        return smartDingtalkClient;
    }
}
