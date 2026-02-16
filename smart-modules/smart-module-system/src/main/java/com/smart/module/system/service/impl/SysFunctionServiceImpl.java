package com.smart.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.commons.core.exception.SystemException;
import com.smart.framework.commons.core.utils.BeanUtils;
import com.smart.framework.commons.core.utils.SmartIdGenerator;
import com.smart.framework.crud.model.CreateUpdateUserSetter;
import com.smart.framework.crud.plus.metadata.SmartTableInfo;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.framework.crud.service.UserSetterService;
import com.smart.framework.crud.utils.CrudUtils;
import com.smart.module.system.constants.SystemConstantEnum;
import com.smart.module.system.mapper.CommonMapper;
import com.smart.module.system.mapper.SysFunctionMapper;
import com.smart.module.system.model.SysFunctionPO;
import com.smart.module.system.model.SysRoleFunctionPO;
import com.smart.module.system.model.micorapp.SysFunctionMicroFrontendPO;
import com.smart.module.system.model.tenant.SysTenantPO;
import com.smart.module.system.pojo.dto.tenant.SysListTenantFunctionDTO;
import com.smart.module.system.pojo.parameter.function.SysFunctionSaveUpdateParameter;
import com.smart.module.system.pojo.vo.function.SysFunctionVO;
import com.smart.module.system.service.SysFunctionService;
import com.smart.module.system.service.SysRoleFunctionService;
import com.smart.module.system.service.microapp.SysFunctionMicroFrontendService;
import com.smart.module.system.service.tenant.SysTenantService;
import com.smart.module.system.service.tenant.SysTenantUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jackson
 * 2020/1/27 12:16 下午
 */
@Service
@RequiredArgsConstructor
public class SysFunctionServiceImpl extends BaseServiceImpl<SysFunctionMapper, SysFunctionPO> implements SysFunctionService {

    private UserSetterService userSetterService;

    private final CommonMapper commonMapper;
    private final SysRoleFunctionService sysRoleFunctionService;
    private final SysTenantUserService sysTenantUserService;
    private final ObjectProvider<SysTenantService> sysTenantService;
    private final SysFunctionMicroFrontendService sysFunctionMicroFrontendService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<?> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        // 删除功能角色中间关联表
        this.sysRoleFunctionService.remove(
                new QueryWrapper<SysRoleFunctionPO>().lambda()
                        .in(SysRoleFunctionPO::getFunctionId, idList)
        );
        Set<Long> parentIds = this.listByIds((Collection<? extends Serializable>) idList)
                .stream().map(SysFunctionPO::getParentId)
                .collect(Collectors.toSet());
        // 获取上级
        Set<Long> deleteIds = new HashSet<>();
        this.getAllChildren(new HashSet<>((Collection<Long>) idList), deleteIds);
        boolean result = super.removeByIds(deleteIds);
        // 更新是否有上级
        parentIds.forEach(this::updateHasChild);
        return result;
    }

    public void getAllChildren(Set<Long> ids, Set<Long> allIds) {
        allIds.addAll(ids);
        Set<Long> childrenIds = this.list(
                new QueryWrapper<SysFunctionPO>().lambda()
                        .in(SysFunctionPO::getParentId, ids)
        ).stream().map(SysFunctionPO::getFunctionId)
                .filter(item -> !allIds.contains(item))
                .collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(childrenIds)) {
            this.getAllChildren(childrenIds, allIds);
        }
    }

    @Override
    public List<SysFunctionPO> list(@NonNull QueryWrapper<SysFunctionPO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        if (Boolean.TRUE.equals(parameter.getParameter().get(SystemConstantEnum.LIST_FILTER_TENANT))) {
            // 需要根据租户套餐过滤
            SysListTenantFunctionDTO tenantParameter = new SysListTenantFunctionDTO();
            tenantParameter.setTenantId(AuthUtils.getNonNullCurrentTenantId());
            List<Long> functionIds = this.sysTenantUserService.listTenantFunctionIds(tenantParameter);
            if (CollectionUtils.isEmpty(functionIds)) {
                return Collections.emptyList();
            }
            queryWrapper.lambda().in(SysFunctionPO::getFunctionId, new HashSet<>(functionIds));
        }
        return super.list(queryWrapper, parameter, paging);
    }

    /**
     * 根据租户ID查询功能
     *
     * @param tenantId 租户ID
     * @return 功能列表
     */
    @Override
    public List<SysFunctionPO> listTenantFunction(@NonNull Long tenantId) {
        SysTenantPO sysTenant = this.sysTenantService.getObject().getById(tenantId);
        if (sysTenant == null) {
            throw new SystemException("查询租户失败，租户ID：" + tenantId);
        }
        LambdaQueryWrapper<SysFunctionPO> queryWrapper = Wrappers.lambdaQuery(SysFunctionPO.class)
                .select(
                        SysFunctionPO::getFunctionId, SysFunctionPO::getParentId, SysFunctionPO::getFunctionName,
                        SysFunctionPO::getFunctionType
                ).orderByAsc(SysFunctionPO::getSeq)
                .eq(SysFunctionPO::getUseYn, Boolean.TRUE);
        // 需要根据租户套餐过滤
        SysListTenantFunctionDTO tenantParameter = new SysListTenantFunctionDTO();
        tenantParameter.setTenantId(tenantId);
        List<Long> functionIds = this.sysTenantUserService.listTenantFunctionIds(tenantParameter);
        if (CollectionUtils.isEmpty(functionIds)) {
            return Collections.emptyList();
        }
        queryWrapper.in(SysFunctionPO::getFunctionId, new HashSet<>(functionIds));
        return this.list(queryWrapper);
    }

    @Override
    public SysFunctionVO getUserAndParentById(Long functionId) {
        SysFunctionPO function = this.getById(functionId);
        if (function == null) {
            return null;
        }
        SysFunctionVO vo = new SysFunctionVO();
        vo.setFunction(function);
        List<SysFunctionVO> voList = List.of(vo);
        this.queryCreateUpdateUser(voList);
        this.queryParent(voList);
        // 查询微前端微应用
        this.queryMicroFrontend(voList);
        return voList.getFirst();
    }

    /**
     * 添加修改功能
     *
     * @param parameter 参数
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUpdate(SysFunctionSaveUpdateParameter parameter) {
        SysFunctionPO model = BeanUtils.copyProperties(parameter, SysFunctionPO.class);

        boolean isAdd = this.isAdd(model);
        if (isAdd) {
            model.setFunctionId(SmartIdGenerator.nextId());
        }
        boolean isMicroFrontend = Boolean.TRUE.equals(parameter.getIsMicroFrontend());
        if (!isAdd) {
            // 删除微前端关系
            this.sysFunctionMicroFrontendService.remove(
                    Wrappers.lambdaQuery(SysFunctionMicroFrontendPO.class)
                            .eq(SysFunctionMicroFrontendPO::getFunctionId, model.getFunctionId())
            );
        }
        // 保存更新功能
        boolean result = isAdd ? this.save(model) : this.updateById(model);
        // 保存微前端关系
        if (isMicroFrontend) {
            SysFunctionMicroFrontendPO microFrontend = BeanUtils.copyProperties(parameter.getMicroFrontend(), SysFunctionMicroFrontendPO.class);
            microFrontend.setFunctionId(model.getFunctionId());
            this.sysFunctionMicroFrontendService.save(microFrontend);
        }
        // 更新上级是否有子节点
        this.updateHasChild(model.getParentId());
        return result;
    }

    /**
     * 批量修改插入
     *
     * @param entityList 实体对象集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateBatch(Collection<SysFunctionPO> entityList) {
        boolean result = super.saveOrUpdateBatch(entityList);
        entityList.forEach(item -> this.updateHasChild(item.getParentId()));
        return result;
    }

    private void updateHasChild(Long id) {
        SmartTableInfo tableInfo = CrudUtils.getTableInfo(SysFunctionPO.class);
        this.commonMapper.updateHasChild(
                tableInfo.getTableName(),
                tableInfo.getTableFiled(SysFunctionPO::getParentId).getColumn(),
                tableInfo.getKeyColumn(),
                id
        );
    }

    /**
     * 查询创建人更新人信息
     * @param functionVoList voList
     */
    protected void queryCreateUpdateUser(List<? extends CreateUpdateUserSetter> functionVoList) {
        if (CollectionUtils.isEmpty(functionVoList)) {
            return;
        }
        this.userSetterService.setCreateUpdateUser(functionVoList);
    }

    /**
     * 查询上级信息
     * @param functionVoList voList
     */
    protected void queryParent(List<SysFunctionVO> functionVoList) {
        if (CollectionUtils.isEmpty(functionVoList)) {
            return;
        }
        // 查询上级ID和本级ID
        Set<Long> parentIds = functionVoList.stream().map(item -> item.getFunction().getParentId())
                .filter(item -> item != 0)
                .collect(Collectors.toSet());

        Map<Long, SysFunctionPO> parentMap = this.listByIds(parentIds).stream()
                .collect(Collectors.toMap(SysFunctionPO::getFunctionId, item -> item));

        for (SysFunctionVO vo : functionVoList) {
            Long functionId = vo.getFunction().getParentId();
            if (parentMap.containsKey(functionId)) {
                vo.setParent(parentMap.get(functionId));
            }
        }
    }

    /**
     * 查询微前端微应用信息
     * @param functionVoList voList
     */
    protected void queryMicroFrontend(List<SysFunctionVO> functionVoList) {
        if (CollectionUtils.isEmpty(functionVoList)) {
            return;
        }
        Set<Long> functionIds = functionVoList.stream().map(item -> item.getFunction().getFunctionId())
                .collect(Collectors.toSet());

        Map<Long, SysFunctionMicroFrontendPO> microFrontendMap = this.sysFunctionMicroFrontendService
                .lambdaQuery()
                .in(SysFunctionMicroFrontendPO::getFunctionId, functionIds)
                .list().stream()
                .collect(Collectors.toMap(SysFunctionMicroFrontendPO::getFunctionId, item -> item));

        for (SysFunctionVO vo : functionVoList) {
            Long functionId = vo.getFunction().getFunctionId();
            if (microFrontendMap.containsKey(functionId)) {
                vo.setMicroFrontend(microFrontendMap.get(functionId));
            }
        }
    }

    @Autowired
    public void setUserSetterService(UserSetterService userSetterService) {
        this.userSetterService = userSetterService;
    }
}
