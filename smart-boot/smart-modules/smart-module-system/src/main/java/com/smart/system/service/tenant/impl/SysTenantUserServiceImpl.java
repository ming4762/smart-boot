package com.smart.system.service.tenant.impl;

import com.smart.crud.service.BaseServiceImpl;
import com.smart.system.mapper.tenant.SysTenantUserMapper;
import com.smart.system.model.tenant.SysTenantUserPO;
import com.smart.system.pojo.dbo.tenant.SysTenantListByUserDO;
import com.smart.system.pojo.dto.tenant.SysListTenantFunctionDTO;
import com.smart.system.pojo.dto.tenant.SysListTenantRoleFunctionDTO;
import com.smart.system.service.tenant.SysTenantUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* sys_tenant_user - 租户用户关联关系表 Service实现类
* @author SmartCodeGenerator
* 2024年4月6日 下午8:25:53
*/
@Service
public class SysTenantUserServiceImpl extends BaseServiceImpl<SysTenantUserMapper, SysTenantUserPO> implements SysTenantUserService {

    /**
     * 根据用户ID查询满足条件的一个租户
     *
     * @param userId 用户ID
     * @return 租户信息
     */
    @Override
    public SysTenantListByUserDO selectOneTenantByUser(Long userId) {
        return this.baseMapper.selectOneTenantByUser(userId);
    }

    /**
     * 根据租户角色查询功能ID
     *
     * @param parameter 参数
     * @return functionIdList
     */
    @Override
    public List<Long> listTenantRoleFunctionIds(SysListTenantRoleFunctionDTO parameter) {
        return this.baseMapper.listTenantRoleFunctionIds(parameter);
    }

    /**
     * 查询租户所有有效订阅菜单ID
     *
     * @param parameter 参数
     * @return 菜单ID
     */
    @Override
    public List<Long> listTenantFunctionIds(SysListTenantFunctionDTO parameter) {
        return this.getBaseMapper().listTenantFunctionIds(parameter);
    }
}