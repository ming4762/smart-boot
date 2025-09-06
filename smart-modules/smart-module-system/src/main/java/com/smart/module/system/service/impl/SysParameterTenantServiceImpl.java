package com.smart.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.system.constants.SystemConstantEnum;
import com.smart.module.system.mapper.SysParameterTenantMapper;
import com.smart.module.system.model.SysParameterTenantPO;
import com.smart.module.system.pojo.vo.parameter.SysParameterTenantListVO;
import com.smart.module.system.service.SysParameterTenantService;
import com.smart.module.system.service.tenant.SysTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
* sys_parameter_tenant - 系统参数租户表 Service实现类
* @author SmartCodeGenerator
* 2025年9月4日 19:47:33
*/
@Service
@RequiredArgsConstructor
public class SysParameterTenantServiceImpl extends BaseServiceImpl<SysParameterTenantMapper, SysParameterTenantPO> implements SysParameterTenantService {

    private final ObjectProvider<SysTenantService> sysTenantService;

    /**
     * 查询函数
     *
     * @param queryWrapper 查询参数
     * @param parameter    原始参数
     * @param paging       是否分页
     * @return 查询结果
     */
    @Override
    public List<? extends SysParameterTenantPO> list(@NonNull QueryWrapper<SysParameterTenantPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        // 非平台管理租户添加数据权限过滤
        if (!AuthUtils.isPlatformTenant()) {
            Long currentTenantId = AuthUtils.getCurrentTenantId();
            List<Long> tenantIds = currentTenantId == null ? List.of(SysParameterTenantPO.COMMON_PARAMETER_TENANT_ID) : List.of(SysParameterTenantPO.COMMON_PARAMETER_TENANT_ID, currentTenantId);
            queryWrapper.lambda().in(SysParameterTenantPO::getTenantId, tenantIds);
        }
        List<? extends SysParameterTenantPO> dataList = super.list(queryWrapper, parameter, paging);
        if (CollectionUtils.isEmpty(dataList)) {
            return dataList;
        }
        if (Boolean.TRUE.equals(parameter.getParameter().get(SystemConstantEnum.LIST_WITH_TENANT))) {
            List<SysParameterTenantListVO> voList = dataList.stream()
                    .map(item -> {
                        SysParameterTenantListVO vo = new SysParameterTenantListVO();
                        BeanUtils.copyProperties(item, vo);
                        return vo;
                    }).toList();
            this.sysTenantService.getObject().injectTenant(voList);
            return voList;
        }
        return dataList;
    }
}