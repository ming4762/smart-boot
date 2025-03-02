package com.smart.module.system.service.crud;

import com.smart.framework.auth.core.userdetails.RestUserDetails;
import com.smart.framework.auth.core.utils.AuthUtils;
import com.smart.framework.crud.service.UserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 获取人员信息
 * @author zhongming4762
 * 2022/12/16 21:14
 */
@Component
@RequiredArgsConstructor
public class UserProviderImpl implements UserProvider {

//    private final SysUserApi sysUserApi;

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

    /**
     * 获取当前登录人员部门ID
     *
     * @return 部门ID
     */
    @Override
    public Long getCurrentDeptId() {
        return 0L;
    }

    /**
     * 获取当前登录人员部门名称
     *
     * @return 部门名称
     */
    @Override
    public String getCurrentDeptName() {
        return "";
    }
}
