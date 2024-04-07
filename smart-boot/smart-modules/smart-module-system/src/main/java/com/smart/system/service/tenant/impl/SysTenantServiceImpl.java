package com.smart.system.service.tenant.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.crud.plus.metadata.SmartTableInfo;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.crud.utils.CrudUtils;
import com.smart.system.mapper.tenant.SysTenantMapper;
import com.smart.system.mapper.tenant.SysTenantUserMapper;
import com.smart.system.model.SysUserPO;
import com.smart.system.model.tenant.SysTenantPO;
import com.smart.system.model.tenant.SysTenantUserPO;
import com.smart.system.pojo.dbo.tenant.SysTenantUserListDO;
import com.smart.system.pojo.dto.tenant.SysTenantBindUserDTO;
import com.smart.system.pojo.dto.tenant.SysTenantListNoBindUserDTO;
import com.smart.system.pojo.dto.tenant.SysTenantRemoveBindUserDTO;
import com.smart.system.pojo.dto.tenant.SysTenantUserListDTO;
import com.smart.system.service.SysUserService;
import com.smart.system.service.tenant.SysTenantService;
import com.smart.system.service.tenant.SysTenantUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* sys_tenant - 租户表 Service实现类
* @author SmartCodeGenerator
* 2024年3月29日 上午10:42:40
*/
@Service
@RequiredArgsConstructor
public class SysTenantServiceImpl extends BaseServiceImpl<SysTenantMapper, SysTenantPO> implements SysTenantService {

    private final SysTenantUserMapper sysTenantUserMapper;
    private final SysUserService sysUserService;
    private final SysTenantUserService sysTenantUserService;

    /**
     * 查询租户对应用户
     *
     * @param parameter 参数
     * @return 用户列表
     */
    @Override
    public List<SysTenantUserListDO> listTenantUser(SysTenantUserListDTO parameter) {
        SmartTableInfo userTableInfo = CrudUtils.getTableInfo(SysUserPO.class);
        QueryWrapper<SysTenantUserPO> queryWrapper = new QueryWrapper<>();
        if (parameter.getFullName() != null) {
            queryWrapper.like(userTableInfo.getTableFiled(SysUserPO::getFullName).getColumn(), parameter.getFullName());
        }
        if (parameter.getUsername() != null) {
            queryWrapper.like(userTableInfo.getTableFiled(SysUserPO::getUsername).getColumn(), parameter.getUsername());
        }
        queryWrapper.lambda()
                .eq(SysTenantUserPO::getTenantId, parameter.getTenantId());
        return this.sysTenantUserMapper.listTenantUser(queryWrapper);
    }

    /**
     * 查询未绑定租户的用户
     *
     * @param parameter 参数
     * @return 用户列表
     */
    @Override
    public List<SysUserPO> listNoBindUser(SysTenantListNoBindUserDTO parameter) {
        QueryWrapper<SysUserPO> queryWrapper = CrudUtils.createQueryWrapperFromParameters(parameter.getParameter(), SysUserPO.class);
        queryWrapper.apply("user_id not in (select M.user_id from sys_tenant_user M where M.tenant_id = {0})", parameter.getTenantId());
        return this.sysUserService.list(queryWrapper);
    }

    /**
     * 绑定用户
     *
     * @param parameter 参数
     * @return 是否绑定成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean bindTenantUser(SysTenantBindUserDTO parameter) {
        List<SysTenantUserPO> modelList = parameter.getUserIdList().stream()
                .map(userId -> {
                    SysTenantUserPO tenantUser = new SysTenantUserPO();
                    tenantUser.setTenantId(parameter.getTenantId());
                    tenantUser.setUserId(userId);
                    return tenantUser;
                }).toList();
        return this.sysTenantUserService.saveBatch(modelList);
    }

    /**
     * 解绑用户
     *
     * @param parameter 参数
     * @return 是否解绑成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeBindUser(SysTenantRemoveBindUserDTO parameter) {
        return this.sysTenantUserService.remove(
                new LambdaQueryWrapper<>(SysTenantUserPO.class)
                        .eq(SysTenantUserPO::getTenantId, parameter.getTenantId())
                        .in(SysTenantUserPO::getUserId, parameter.getUserIdList())
        );
    }
}