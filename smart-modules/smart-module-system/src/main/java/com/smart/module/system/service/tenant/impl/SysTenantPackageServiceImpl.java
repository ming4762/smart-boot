package com.smart.module.system.service.tenant.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.system.mapper.tenant.SysTenantPackageMapper;
import com.smart.module.system.model.tenant.SysTenantPackageFunctionPO;
import com.smart.module.system.model.tenant.SysTenantPackagePO;
import com.smart.module.system.pojo.dto.tenant.SysTenantPackageSaveFunctionDTO;
import com.smart.module.system.service.tenant.SysTenantPackageFunctionService;
import com.smart.module.system.service.tenant.SysTenantPackageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
* sys_tenant_package - 租户产品套餐 Service实现类
* @author SmartCodeGenerator
* 2024年4月2日 下午3:02:14
*/
@Service
public class SysTenantPackageServiceImpl extends BaseServiceImpl<SysTenantPackageMapper, SysTenantPackagePO> implements SysTenantPackageService {

    private final SysTenantPackageFunctionService sysTenantPackageFunctionService;

    public SysTenantPackageServiceImpl(SysTenantPackageFunctionService sysTenantPackageFunctionService) {
        this.sysTenantPackageFunctionService = sysTenantPackageFunctionService;
    }

    /**
     * 保存租户套餐功能
     *
     * @param parameter 参数
     * @return 是否保存成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePackageFunction(SysTenantPackageSaveFunctionDTO parameter) {
        // 删除原有套餐
        this.sysTenantPackageFunctionService.remove(
                new LambdaQueryWrapper<>(SysTenantPackageFunctionPO.class)
                        .eq(SysTenantPackageFunctionPO::getTenantPackageId, parameter.getTenantPackageId())
        );

        ArrayList<SysTenantPackageFunctionPO> modelList = new ArrayList<>(16);
        if (!CollectionUtils.isEmpty(parameter.getFunctionIdList())) {
            modelList.addAll(
                    parameter.getFunctionIdList().stream().map(item -> {
                        var model = new SysTenantPackageFunctionPO();
                        model.setFunctionId(item);
                        model.setTenantPackageId(parameter.getTenantPackageId());
                        model.setHalfYn(false);
                        return model;
                    }).toList()
            );
        }
        if (!CollectionUtils.isEmpty(parameter.getHalfFunctionIdList())) {
            modelList.addAll(
                    parameter.getHalfFunctionIdList().stream().map(item -> {
                        var model = new SysTenantPackageFunctionPO();
                        model.setFunctionId(item);
                        model.setTenantPackageId(parameter.getTenantPackageId());
                        model.setHalfYn(true);
                        return model;
                    }).toList()
            );
        }
        if (!modelList.isEmpty()) {
            this.sysTenantPackageFunctionService.saveBatch(modelList);
        }
        return true;
    }


    /**
     * 重写批量删除方法，如果ID只有一个调用removeById方法
     *
     * @param idList ID列表
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<?> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        // 删除与菜单关联关系表
        this.sysTenantPackageFunctionService.remove(
                new LambdaQueryWrapper<>(SysTenantPackageFunctionPO.class)
                        .in(SysTenantPackageFunctionPO::getTenantPackageId, idList)
        );
        return super.removeByIds(idList);
    }
}