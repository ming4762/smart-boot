package com.smart.system.service.tenant;

import com.smart.crud.service.BaseService;
import com.smart.system.model.tenant.SysTenantUserPO;
import com.smart.system.pojo.dbo.tenant.SysTenantListByUserDO;

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
}