package com.smart.module.system.service.tenant.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.system.mapper.tenant.SysTenantPackageFunctionMapper;
import com.smart.module.system.model.tenant.SysTenantPackageFunctionPO;
import com.smart.module.system.service.tenant.SysTenantPackageFunctionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* sys_tenant_package_function - 租户套餐-功能菜单关联关系表 Service实现类
* @author SmartCodeGenerator
* 2024年4月3日 下午1:40:45
*/
@Service
public class SysTenantPackageFunctionServiceImpl extends BaseServiceImpl<SysTenantPackageFunctionMapper, SysTenantPackageFunctionPO> implements SysTenantPackageFunctionService {


    /**
     * 查询租户套餐包对应功能
     *
     * @param queryWrapper 参数
     * @return SysTenantPackageFunctionPO
     */
    @Override
    public List<SysTenantPackageFunctionPO> listPackageFunction(LambdaQueryWrapper<SysTenantPackageFunctionPO> queryWrapper) {
        return this.baseMapper.listPackageFunction(queryWrapper);
    }
}