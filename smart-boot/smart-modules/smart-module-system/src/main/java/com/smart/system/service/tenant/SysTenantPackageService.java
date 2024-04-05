package com.smart.system.service.tenant;

import com.smart.crud.service.BaseService;
import com.smart.system.model.tenant.SysTenantPackagePO;
import com.smart.system.pojo.dto.tenant.SysTenantPackageSaveFunctionDTO;

/**
* sys_tenant_package - 租户产品套餐 Service
* @author SmartCodeGenerator
* 2024年4月2日 下午3:02:14
*/
public interface SysTenantPackageService extends BaseService<SysTenantPackagePO> {

    /**
     * 保存租户套餐功能
     * @param parameter 参数
     * @return 是否保存成功
     */
    boolean savePackageFunction(SysTenantPackageSaveFunctionDTO parameter);
}