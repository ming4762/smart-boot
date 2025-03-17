package com.smart.cloud.api.crud.api;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.module.api.crud.SmartCrudUserApi;
import com.smart.module.api.crud.module.UserDeptData;
import com.smart.module.api.system.SysUserApi;
import com.smart.module.api.system.dto.SysDeptDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * crud模块获取用户信息API-spring cloud
 * @author shizhongming
 * 2025/3/17 20:38
 * @since 5.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class SmartCrudUserCloudApi implements SmartCrudUserApi {

    private final SysUserApi sysUserApi;

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
     * 获取当前登录人员部门信息
     *
     * @return 部门ID
     */
    @Override
    public UserDeptData getCurrentDept() {
        List<SysDeptDTO> userDeptList = this.sysUserApi.listUserDept(null);
        if (CollectionUtils.isEmpty(userDeptList)) {
            return null;
        }
        if (userDeptList.size() > 1) {
            //TODO: 处理多个部门情况，设置默认部门？还是不支持多部门？
            log.warn("当前用户有多个部门，返回第一个部门信息");
        }
        SysDeptDTO userDept = userDeptList.getFirst();
        return new UserDeptData(this.getCurrentUserId(), userDept.getDeptId(), userDept.getDeptName());
    }
}
