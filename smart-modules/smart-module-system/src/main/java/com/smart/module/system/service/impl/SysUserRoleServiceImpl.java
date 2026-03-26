package com.smart.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.system.mapper.SysUserRoleMapper;
import com.smart.module.system.model.SysUserRolePO;
import com.smart.module.system.service.SysUserRoleService;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shizhongming
 * 2020/9/24 9:38 下午
 */
@Service
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRoleMapper, SysUserRolePO> implements SysUserRoleService {
    @Override
    public List<SysUserRolePO> listByRoleIdList(@NonNull List<Long> roleIdList) {
        return this.list(
                new QueryWrapper<SysUserRolePO>().lambda()
                .in(SysUserRolePO :: getRoleId, roleIdList)
                .eq(SysUserRolePO :: getEnable, Boolean.TRUE)
        );
    }
}
