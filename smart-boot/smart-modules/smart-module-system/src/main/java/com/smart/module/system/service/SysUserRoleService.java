package com.smart.module.system.service;

import com.smart.framework.crud.service.BaseService;
import com.smart.module.system.model.SysUserRolePO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author shizhongming
 * 2020/9/24 9:37 下午
 */
public interface SysUserRoleService extends BaseService<SysUserRolePO> {

    /**
     * 通过角色ID查询
     * @param roleIdList 角色ID集合
     * @return SysUserRolePO
     */
    List<SysUserRolePO> listByRoleIdList(@NonNull List<Long> roleIdList);
}
