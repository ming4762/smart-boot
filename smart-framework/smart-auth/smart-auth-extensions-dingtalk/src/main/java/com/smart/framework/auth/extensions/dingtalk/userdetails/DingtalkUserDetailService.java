package com.smart.framework.auth.extensions.dingtalk.userdetails;

import com.smart.framework.auth.common.userdetails.RestUserDetails;

/**
 * 钉钉用户详情服务
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/5 13:50
 * @since 5.0.0
 */
public interface DingtalkUserDetailService {

     /**
      * 通过unionid加载用户详情
      * @param unionId 钉钉用户unionid
      * @return 用户详情
      */
    default RestUserDetails loadUserByUnionId(String unionId) {
        return null;
    }

     /**
      * 通过openid加载用户详情
      * @param clientId 钉钉应用clientId
      * @param openId 钉钉用户openid
      * @return 用户详情
      */
    default RestUserDetails loadUserByOpenId(String clientId, String openId) {
        return null;
    }

    /**
     * 通过手机号加载用户详情
     * @param mobile 钉钉用户手机号
     * @return 用户详情
     */
    default RestUserDetails loadUserByMobile(String mobile) {
        return null;
    }
}
