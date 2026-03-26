package com.smart.module.system.service;

import com.smart.framework.crud.service.BaseService;
import com.smart.module.system.model.SysDeptPO;
import com.smart.module.system.model.SysUserPO;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Set;

/**
* sys_dept - 部门表 Service
* @author GCCodeGenerator
* 2022年10月13日 上午10:24:21
*/
public interface SysDeptService extends BaseService<SysDeptPO> {

    /**
     * 查询所有下级
     * @param parentIds 上级ID
     * @return 下级ID
     */
    @NonNull
    Set<Long> queryAllChildIds(@NonNull Set<Long> parentIds);

    /**
     * 根据部门ID查询用户列表
     * @param deptId 部门ID
     * @return 用户列表
     */
    List<SysUserPO> listUserByDeptId(Long deptId);


    /**
     * 获取用户部门
     * @param userId 用户ID
     * @return 部门列表
     */
    List<SysDeptPO> listUserDept(Long userId);
}
