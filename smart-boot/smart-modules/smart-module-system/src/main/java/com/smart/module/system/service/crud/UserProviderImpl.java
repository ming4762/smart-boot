package com.smart.module.system.service.crud;

import com.smart.framework.auth.core.userdetails.RestUserDetails;
import com.smart.framework.auth.core.utils.AuthUtils;
import com.smart.framework.crud.model.UserDeptData;
import com.smart.framework.crud.service.UserProvider;
import com.smart.module.system.model.SysDeptPO;
import com.smart.module.system.service.SysDeptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * 获取人员信息
 * @author zhongming4762
 * 2022/12/16 21:14
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserProviderImpl implements UserProvider {

    private final ObjectProvider<SysDeptService> sysDeptServiceObjectProvider;

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
        List<SysDeptPO> userDeptList = this.sysDeptServiceObjectProvider.getObject().listUserDept(this.getCurrentUserId());
        if (CollectionUtils.isEmpty(userDeptList)) {
            return null;
        }
        if (userDeptList.size() > 1) {
            //TODO: 处理多个部门情况，设置默认部门？还是不支持多部门？
            log.warn("当前用户有多个部门，返回第一个部门信息");
        }
        SysDeptPO dept = userDeptList.getFirst();
        return new UserDeptData(this.getCurrentUserId(), dept.getDeptId(), dept.getDeptName());
    }
}
