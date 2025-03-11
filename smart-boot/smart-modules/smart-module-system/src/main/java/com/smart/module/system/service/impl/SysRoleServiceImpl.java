package com.smart.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.module.system.mapper.SysRoleMapper;
import com.smart.module.system.model.SysRoleDataPermissionPO;
import com.smart.module.system.model.SysRoleFunctionPO;
import com.smart.module.system.model.SysRolePO;
import com.smart.module.system.model.SysUserRolePO;
import com.smart.module.system.pojo.dto.role.RoleMenuSaveDTO;
import com.smart.module.system.pojo.dto.role.RoleSetDataPermissionDTO;
import com.smart.module.system.pojo.dto.role.RoleSetUserDTO;
import com.smart.module.system.service.SysRoleDataPermissionService;
import com.smart.module.system.service.SysRoleFunctionService;
import com.smart.module.system.service.SysRoleService;
import com.smart.module.system.service.SysUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jackson
 * 2020/1/24 2:20 下午
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRolePO> implements SysRoleService {

    private final SysRoleFunctionService sysRoleFunctionService;
    private final SysUserRoleService sysUserRoleService;
    private final SysRoleDataPermissionService sysRoleDataPermissionService;


    /**
     * 保存角色的菜单信息
     * @param parameter 参数
     * @return 是否保存成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRoleMenu(RoleMenuSaveDTO parameter) {
        // 删除
        this.sysRoleFunctionService.remove(
                new QueryWrapper<SysRoleFunctionPO>().lambda()
                .eq(SysRoleFunctionPO :: getRoleId, parameter.getRoleId())
        );
        List<SysRoleFunctionPO> modelList = new ArrayList<>(16);
        // 保存
        if (!CollectionUtils.isEmpty(parameter.getFunctionIdList())) {
            modelList.addAll(
                    parameter.getFunctionIdList().stream().map(item -> {
                        final SysRoleFunctionPO sysRoleFunction = new SysRoleFunctionPO();
                        sysRoleFunction.setFunctionId(item);
                        sysRoleFunction.setRoleId(parameter.getRoleId());
                        sysRoleFunction.setHalfYn(false);
                        return sysRoleFunction;
                    }).toList());
        }

        if (!CollectionUtils.isEmpty(parameter.getHalfFunctionIdList())) {
            modelList.addAll(
                    parameter.getHalfFunctionIdList().stream().map(item -> {
                        final SysRoleFunctionPO sysRoleFunction = new SysRoleFunctionPO();
                        sysRoleFunction.setFunctionId(item);
                        sysRoleFunction.setRoleId(parameter.getRoleId());
                        sysRoleFunction.setHalfYn(true);
                        return sysRoleFunction;
                    }).toList()
            );
        }
        if (!modelList.isEmpty()) {
            this.sysRoleFunctionService.saveBatch(modelList);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setRoleUser(RoleSetUserDTO parameter) {
        // 删除角色
        this.sysUserRoleService.remove(
                new QueryWrapper<SysUserRolePO>().lambda()
                .eq(SysUserRolePO :: getRoleId, parameter.getRoleId())
        );
        if (CollectionUtils.isEmpty(parameter.getUserIdList())) {
            return false;
        }
        this.sysUserRoleService.saveBatch(
                parameter.getUserIdList().stream().map(item -> new SysUserRolePO(item, parameter.getRoleId(), true, null)).toList()
        );
        return true;
    }

    /**
     * 设置角色的数据权限
     *
     * @param parameter RoleSetDataPermissionDTO
     * @return 是否保存成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setRoleDataPermission(RoleSetDataPermissionDTO parameter) {
        // 删除已配置的数据权限
        this.sysRoleDataPermissionService.remove(
                Wrappers.lambdaQuery(SysRoleDataPermissionPO.class)
                        .eq(SysRoleDataPermissionPO::getRoleId, parameter.getRoleId())
        );
        if (CollectionUtils.isEmpty(parameter.getDataPermissionIdList())) {
            return true;
        }
        // 保存角色数据权限
        return this.sysRoleDataPermissionService.saveBatch(
                parameter.getDataPermissionIdList().stream().map(item -> {
                    final SysRoleDataPermissionPO sysRoleDataPermission = new SysRoleDataPermissionPO();
                    sysRoleDataPermission.setRoleId(parameter.getRoleId());
                    sysRoleDataPermission.setDataPermissionId(item);
                    return sysRoleDataPermission;
                }).toList()
        );
    }

    /**
     * 获取角色的数据权限
     *
     * @param roleId 角色id
     * @return 数据权限id列表
     */
    @Override
    public List<Long> listRoleDataPermissionId(Long roleId) {
        if (roleId == null) {
            return Collections.emptyList();
        }
        return this.sysRoleDataPermissionService.lambdaQuery()
                .select(SysRoleDataPermissionPO::getDataPermissionId)
                .eq(SysRoleDataPermissionPO::getRoleId, roleId)
                .list().stream()
                .map(SysRoleDataPermissionPO::getDataPermissionId)
                .distinct()
                .toList();
    }
}
