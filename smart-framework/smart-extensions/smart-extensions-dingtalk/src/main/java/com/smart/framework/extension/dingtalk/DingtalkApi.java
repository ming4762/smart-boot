package com.smart.framework.extension.dingtalk;

import com.smart.framework.extension.dingtalk.api.AccessSecureApi;
import com.smart.framework.extension.dingtalk.api.UserApi;
import com.smart.framework.extension.dingtalk.api.WorkNoticeApi;

/**
 * 钉钉API接口
 * @author shizhongming
 * 2025/10/31 15:32
 * @since 5.0.0
 */
public interface DingtalkApi {

    /**
     * 切换到指定的钉钉应用
     * @param clientId 钉钉应用名称
     * @return 是否切换成功
     */
    boolean switchover(String clientId);

    /**
     * 切换到指定的钉钉应用
     * @param clientId 钉钉应用名称
     * @return 钉钉API实例
     */
    DingtalkApi switchoverTo(String clientId);

    /**
     * 获取用户API
     * @return 用户API实例
     */
    UserApi userApi();

     /**
      * 获取工作通知API
      * @return 工作通知API实例
      */
    WorkNoticeApi workNoticeApi();

    /**
     * 获取访问安全API
     * @return 访问安全API实例
     */
    AccessSecureApi accessSecureApi();
}
