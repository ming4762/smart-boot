package com.smart.module.system.api.local;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.module.api.crud.SmartCrudUserApi;
import com.smart.module.api.crud.module.UserDeptData;
import com.smart.module.api.system.SysUserApi;
import com.smart.module.api.system.dto.SysDeptDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author shizhongming
 * 2025/3/17 20:58
 * @since 5.0.0
 */
@RequiredArgsConstructor
@Component
@Primary
@Slf4j
public class LocalSmartCrudUserApi implements SmartCrudUserApi {

    private final ObjectProvider<SysUserApi> sysUserApi;

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
        List<SysDeptDTO> userDeptList = this.sysUserApi.getObject().listUserDept(null);
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
