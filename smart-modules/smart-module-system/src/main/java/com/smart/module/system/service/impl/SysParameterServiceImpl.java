package com.smart.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.commons.core.exception.BusinessException;
import com.smart.framework.commons.core.exception.SystemException;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.system.constants.SystemConstantEnum;
import com.smart.module.system.mapper.SysParameterMapper;
import com.smart.module.system.model.SysParameterPO;
import com.smart.module.system.model.SysParameterTenantPO;
import com.smart.module.system.pojo.vo.parameter.SysParameterListVO;
import com.smart.module.system.service.SysParameterService;
import com.smart.module.system.service.SysParameterTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* sys_parameter - 系统参数表 Service实现类
* @author SmartCodeGenerator
* 2023-2-27
*/
@Service
@RequiredArgsConstructor
public class SysParameterServiceImpl extends BaseServiceImpl<SysParameterMapper, SysParameterPO> implements SysParameterService {

    private final SysParameterTenantService sysParameterTenantService;
    /**
     * 查询函数
     *
     * @param queryWrapper 查询参数
     * @param parameter    原始参数
     * @param paging       是否分页
     * @return 查询结果
     */
    @Override
    public List<? extends SysParameterPO> list(@NonNull QueryWrapper<SysParameterPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        List<? extends SysParameterPO> dataList = super.list(queryWrapper, parameter, paging);
        if (CollectionUtils.isEmpty(dataList)) {
            return dataList;
        }
        if (Boolean.TRUE.equals(parameter.getParameter().get(SystemConstantEnum.LIST_PARAMETER_WITH_COMMON))) {
            List<SysParameterListVO> voList = dataList.stream()
                    .map(item -> {
                        SysParameterListVO vo = new SysParameterListVO();
                        BeanUtils.copyProperties(item, vo);
                        return vo;
                    }).toList();
            this.queryCommonParameter(voList);
            return voList;
        }
        return dataList;
    }

    /**
     * 查询默认参数
     * @param parameterList 参数列表
     */
    private void queryCommonParameter(List<SysParameterListVO> parameterList) {
        if (CollectionUtils.isEmpty(parameterList)) {
            return;
        }
        List<Long> idList = parameterList.stream().map(SysParameterPO::getId).toList();
        Map<Long, String> parameterValueMap = this.sysParameterTenantService.lambdaQuery()
                .select(SysParameterTenantPO::getParameter, SysParameterTenantPO::getParameterId)
                .eq(SysParameterTenantPO::getTenantId, SysParameterTenantPO.COMMON_PARAMETER_TENANT_ID)
                .in(SysParameterTenantPO::getParameterId, idList)
                .list()
                .stream()
                .collect(Collectors.toMap(SysParameterTenantPO::getParameterId, SysParameterTenantPO::getParameter));
        parameterList.forEach(item -> item.setCommonParameter(parameterValueMap.get(item.getId())));
    }

    /**
     * 获取参数值
     * @param code 系统参数编码
     * @return 系统参数值
     */
    @Override
    @Nullable
    public String getParameter(@NonNull String code) {
        Map<String, String> parameter = this.getParameter(List.of(code));
        return parameter.get(code);
    }

    @NonNull
    @Override
    public Map<String, String> getParameter(@NonNull List<String> codeList) {
        if (CollectionUtils.isEmpty(codeList)) {
            return Collections.emptyMap();
        }
        List<SysParameterPO> list = this.list(
                new QueryWrapper<SysParameterPO>().lambda()
                        .select(SysParameterPO::getId, SysParameterPO::getCode)
                        .in(SysParameterPO::getCode, codeList)
                        .eq(SysParameterPO::getUseYn, true)
        );
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        // 查询参数信息
        Long currentTenantId = AuthUtils.getCurrentTenantId();
        List<Long> tenantIdList = currentTenantId == null ? List.of(SysParameterTenantPO.COMMON_PARAMETER_TENANT_ID) : List.of(SysParameterTenantPO.COMMON_PARAMETER_TENANT_ID, AuthUtils.getCurrentTenantId());
        Map<Long, Map<Long, String>> parameterValueMap = this.sysParameterTenantService.lambdaQuery()
                .select(SysParameterTenantPO::getParameter, SysParameterTenantPO::getParameterId, SysParameterTenantPO::getTenantId)
                .in(SysParameterTenantPO::getParameterId, list.stream().map(SysParameterPO::getId).toList())
                .in(SysParameterTenantPO::getTenantId, tenantIdList)
                .list().stream()
                .collect(
                        Collectors.groupingBy(
                                SysParameterTenantPO::getParameterId,
                                Collectors.toMap(SysParameterTenantPO::getTenantId, SysParameterTenantPO::getParameter)
                        )
                );
        return list.stream()
                .map(item -> {
                    String code = item.getCode();
                    Map<Long, String> parameterTenantMap = parameterValueMap.get(item.getId());
                    if (parameterTenantMap == null) {
                        throw new SystemException("默认参数未维护");
                    }
                    String parameter = parameterTenantMap.get(currentTenantId);
                    if (parameter == null) {
                        parameter = parameterTenantMap.get(SysParameterTenantPO.COMMON_PARAMETER_TENANT_ID);
                    }
                    return Map.entry(code, parameter);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 添加更新
     * @param saveList   添加列表
     * @param updateList 更新列表
     * @return 是否更新成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUpdate(List<SysParameterPO> saveList, List<SysParameterPO> updateList) {
        if (!CollectionUtils.isEmpty(saveList)) {
            // 验证编码是否重复
            List<SysParameterPO> hasList = this.list(
                    new QueryWrapper<SysParameterPO>().lambda()
                            .select(SysParameterPO::getId)
                            .in(SysParameterPO::getCode, saveList.stream().map(SysParameterPO::getCode).collect(Collectors.toSet()))
            );
            if (!CollectionUtils.isEmpty(hasList)) {
                throw new BusinessException("参数编码已存在！");
            }
            this.saveBatch(saveList);
        }
        if (!CollectionUtils.isEmpty(updateList)) {
            this.updateBatchById(updateList);
        }
        return true;
    }
}