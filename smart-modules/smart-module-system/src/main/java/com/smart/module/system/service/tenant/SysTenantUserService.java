package com.smart.module.system.service.tenant;

import com.smart.framework.crud.service.BaseService;
import com.smart.module.system.model.tenant.SysTenantUserPO;
import com.smart.module.system.pojo.dbo.tenant.SysTenantListByUserDO;
import com.smart.module.system.pojo.dto.tenant.SysListTenantFunctionDTO;
import com.smart.module.system.pojo.dto.tenant.SysListTenantRoleFunctionDTO;

import java.util.List;

/**
* sys_tenant_user - 租户用户关联关系表 Service
* @author SmartCodeGenerator
* 2024年4月6日 下午8:25:53
*/
public interface SysTenantUserService extends BaseService<SysTenantUserPO> {

    /**
     * 根据用户ID查询满足条件的一个租户
     * @param userId 用户ID
     * @return 租户信息
     */
    SysTenantListByUserDO selectOneTenantByUser(Long userId);

    /**
     * 根据租户角色查询功能ID
     * @param parameter 参数
     * @return functionIdList
     */
    List<Long> listTenantRoleFunctionIds(SysListTenantRoleFunctionDTO parameter);

    /**
     * 查询租户所有有效订阅菜单ID
     * @param parameter 参数
     * @return 菜单ID
     */
    List<Long> listTenantFunctionIds(SysListTenantFunctionDTO parameter);
}