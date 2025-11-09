package com.smart.framework.auth.extensions.dingtalk.userdetails;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.core.userdetails.UserDetailsBuilder;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.dto.AuthUserDTO;
import com.smart.module.api.system.parameter.DingtalkUserQueryParameter;
import lombok.RequiredArgsConstructor;

/**
 * 默认钉钉用户详情服务实现类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/5 15:31
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class DefaultDingtalkUserDetailServiceImpl implements DingtalkUserDetailService {

    private final SystemAuthUserApi systemAuthUserApi;
    private final UserDetailsBuilder userDetailsBuilder;

    /**
     * 通过unionid加载用户详情
     *
     * @param unionId 钉钉用户unionid
     * @return 用户详情
     */
    @Override
    public RestUserDetails loadUserByUnionId(String unionId) {
        AuthUserDTO user = this.systemAuthUserApi.getByDingtalkUnionId(
                DingtalkUserQueryParameter.builder()
                        .unionId(unionId)
                        .build()
        );
        return userDetailsBuilder.buildUserDetails(user);
    }

    /**
     * 通过openid加载用户详情
     *
     * @param openId 钉钉用户openid
     * @return 用户详情
     */
    @Override
    public RestUserDetails loadUserByOpenId(String clientId, String openId) {
        AuthUserDTO user = this.systemAuthUserApi.getByDingtalkOpenId(
                DingtalkUserQueryParameter.builder()
                        .clientId(clientId)
                        .openId(openId)
                        .build()
        );
        return userDetailsBuilder.buildUserDetails(user);
    }

    /**
     * 通过手机号加载用户详情
     *
     * @param mobile 钉钉用户手机号
     * @return 用户详情
     */
    @Override
    public RestUserDetails loadUserByMobile(String mobile) {
        AuthUserDTO user = this.systemAuthUserApi.getByDingtalkMobile(
                DingtalkUserQueryParameter.builder()
                        .mobile(mobile)
                        .build()
        );
        return userDetailsBuilder.buildUserDetails(user);
    }
}
