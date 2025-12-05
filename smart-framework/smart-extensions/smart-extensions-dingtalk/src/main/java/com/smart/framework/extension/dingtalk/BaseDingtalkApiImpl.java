package com.smart.framework.extension.dingtalk;

import com.smart.framework.extension.dingtalk.api.AccessSecureApi;
import com.smart.framework.extension.dingtalk.api.UserApi;
import com.smart.framework.extension.dingtalk.api.WorkNoticeApi;
import com.smart.framework.extension.dingtalk.client.SmartDingtalkClientHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

/**
 * @author shizhongming
 * 2024/4/28 11:20
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class BaseDingtalkApiImpl implements ApplicationContextAware, DingtalkApi {

    private final SmartDingtalkClientHolder dingtalkClientHolder;

    /**
     * 获取认证API
     * @return 认证API
     */
    @Override
    public AccessSecureApi accessSecureApi() {
        return this.applicationContext.getBean(AccessSecureApi.class);
    }

    /**
     * 获取用户API
     * @return 用户API
     */
    @Override
    public UserApi userApi() {
        return this.applicationContext.getBean(UserApi.class);
    }

    /**
     * 获取工作通知API
     * @return 工作通知API
     */
    @Override
    public WorkNoticeApi workNoticeApi() {
        return this.applicationContext.getBean(WorkNoticeApi.class);
    }


    /**
     * 切换到指定的钉钉应用
     *
     * @param clientId 钉钉应用名称
     * @return 是否切换成功
     */
    @Override
    public boolean switchover(String clientId) {
        return dingtalkClientHolder.switchover(clientId);
    }

    /**
     * 切换到指定的钉钉应用
     *
     * @param clientId 钉钉应用名称
     * @return 钉钉API实例
     */
    @Override
    public DingtalkApi switchoverTo(String clientId) {
        this.switchover(clientId);
        return this;
    }

    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
