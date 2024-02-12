package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.system.mapper.SysRoleFunctionMapper;
import com.smart.system.model.SysRoleFunctionPO;
import com.smart.system.service.SysRoleFunctionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shizhongming
 * 2020/9/22 8:47 下午
 */
@Service
public class SysRoleFunctionServiceImpl extends BaseServiceImpl<SysRoleFunctionMapper, SysRoleFunctionPO> implements SysRoleFunctionService {
    /**
     * 查询角色功能
     *
     * @param queryWrapper 查询参数
     * @return List<SysRoleFunctionPO>
     */
    @Override
    public List<SysRoleFunctionPO> listRoleFunction(LambdaQueryWrapper<SysRoleFunctionPO> queryWrapper) {
        return this.baseMapper.listRoleFunction(queryWrapper);
    }
}
