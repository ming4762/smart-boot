package com.smart.system.service.tenant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smart.crud.service.BaseService;
import com.smart.system.model.tenant.SysTenantPackageFunctionPO;

import java.util.List;

/**
* sys_tenant_package_function - 租户套餐-功能菜单关联关系表 Service
* @author SmartCodeGenerator
* 2024年4月3日 下午1:40:45
*/
public interface SysTenantPackageFunctionService extends BaseService<SysTenantPackageFunctionPO> {

    /**
     * 查询租户套餐包对应功能
     * @param queryWrapper 参数
     * @return SysTenantPackageFunctionPO
     */
    List<SysTenantPackageFunctionPO> listPackageFunction(LambdaQueryWrapper<SysTenantPackageFunctionPO> queryWrapper);
}