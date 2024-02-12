package com.smart.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smart.crud.service.BaseService;
import com.smart.system.model.SysRoleFunctionPO;

import java.util.List;

/**
 * @author shizhongming
 * 2020/9/22 8:46 下午
 */
public interface SysRoleFunctionService extends BaseService<SysRoleFunctionPO> {

    /**
     * 查询角色功能
     * @param queryWrapper 查询参数
     * @return List<SysRoleFunctionPO>
     */
    List<SysRoleFunctionPO> listRoleFunction(LambdaQueryWrapper<SysRoleFunctionPO> queryWrapper);
}
