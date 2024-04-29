package com.smart.dingtalk;

import com.smart.dingtalk.api.AccessSecureApi;
import com.smart.dingtalk.api.UserApi;
import com.smart.dingtalk.api.WorkNoticeApi;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

/**
 * @author shizhongming
 * 2024/4/28 11:20
 * @since 3.0.0
 */
public class DingtalkApi implements ApplicationContextAware {

    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取认证API
     * @return 认证API
     */
    public AccessSecureApi accessSecureApi() {
        return this.applicationContext.getBean(AccessSecureApi.class);
    }

    /**
     * 获取用户API
     * @return 用户API
     */
    public UserApi userApi() {
        return this.applicationContext.getBean(UserApi.class);
    }

    /**
     * 获取工作通知API
     * @return 工作通知API
     */
    public WorkNoticeApi workNoticeApi() {
        return this.applicationContext.getBean(WorkNoticeApi.class);
    }
}
