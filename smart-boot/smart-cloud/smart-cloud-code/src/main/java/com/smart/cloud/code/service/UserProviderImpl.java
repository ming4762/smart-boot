package com.smart.cloud.code.service;

import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.crud.service.UserProvider;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 获取人员信息
 * @author zhongming4762
 * 2022/12/16 21:14
 */
@Component
public class UserProviderImpl implements UserProvider {
    /**
     * 获取当前登录人员ID
     *
     * @return 人员ID
     */
    @Override
    public Long getCurrentUserId() {
        return AuthUtils.getCurrentUserId();
    }

    /**
     * 获取当前登录人员username
     *
     * @return username
     */
    @Override
    public String getCurrentUsername() {
        return AuthUtils.getCurrentUsername();
    }

    /**
     * 获取当前登录人员姓名
     *
     * @return 姓名
     */
    @Override
    public String getCurrentUserFullName() {
        return Optional.ofNullable(AuthUtils.getCurrentUser())
                .map(RestUserDetails::getFullName)
                .orElse(null);
    }
}
