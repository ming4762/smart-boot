package com.smart.module.system.mapper.tenant;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.smart.framework.crud.mapper.CrudBaseMapper;
import com.smart.module.system.model.tenant.SysTenantPackageFunctionPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* sys_tenant_package_function - 租户套餐-功能菜单关联关系表 mapper层
* @author SmartCodeGenerator
* 2024年4月3日 下午1:40:45
*/
public interface SysTenantPackageFunctionMapper extends CrudBaseMapper<SysTenantPackageFunctionPO> {

    /**
     * 查询租户套餐包对应功能
     * @param queryWrapper 参数
     * @return SysTenantPackageFunctionPO
     */
    List<SysTenantPackageFunctionPO> listPackageFunction(@Param(Constants.WRAPPER) Wrapper<SysTenantPackageFunctionPO> queryWrapper);
}