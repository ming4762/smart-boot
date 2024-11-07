package com.smart.module.system.service;

import com.smart.framework.crud.service.BaseService;
import com.smart.module.system.model.SysRolePO;
import com.smart.module.system.pojo.dto.role.RoleMenuSaveDTO;
import com.smart.module.system.pojo.dto.role.RoleSetUserDTO;

/**
 * @author jackson
 * 2020/1/24 2:20 下午
 */
public interface SysRoleService extends BaseService<SysRolePO> {

    /**
     * 保存角色的菜单信息
     * @param parameter 参数
     * @return 是否保存成功
     */
    boolean saveRoleMenu(RoleMenuSaveDTO parameter);

    /**
     * 设置角色对应的用户
     * @param parameter RoleSetUserDTO
     * @return 是否保存成功
     */
    boolean setRoleUser(RoleSetUserDTO parameter);
}
