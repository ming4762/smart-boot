package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.query.PageSortQuery;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.crud.service.UserSetterService;
import com.smart.system.mapper.SysRoleMapper;
import com.smart.system.model.SysRoleFunctionPO;
import com.smart.system.model.SysRolePO;
import com.smart.system.model.SysUserRolePO;
import com.smart.system.pojo.dto.role.RoleMenuSaveDTO;
import com.smart.system.pojo.dto.role.RoleSetUserDTO;
import com.smart.system.pojo.vo.SysRoleListVO;
import com.smart.system.service.SysRoleFunctionService;
import com.smart.system.service.SysRoleService;
import com.smart.system.service.SysUserRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jackson
 * 2020/1/24 2:20 下午
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRolePO> implements SysRoleService {

    private final SysRoleFunctionService sysRoleFunctionService;

    private final SysUserRoleService sysUserRoleService;

    private UserSetterService userSetterService;

    public SysRoleServiceImpl(SysRoleFunctionService sysRoleFunctionService, SysUserRoleService sysUserRoleService) {
        this.sysRoleFunctionService = sysRoleFunctionService;
        this.sysUserRoleService = sysUserRoleService;
    }

    @Override
    public List<? extends SysRolePO> list(@NonNull QueryWrapper<SysRolePO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        List<? extends SysRolePO> sysRoleList = super.list(queryWrapper, parameter, paging);
        if (CollectionUtils.isEmpty(sysRoleList)) {
            return new ArrayList<>(0);
        }
        List<SysRoleListVO> roleVoList = sysRoleList.stream()
                .map(item -> {
                    SysRoleListVO vo = new SysRoleListVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                }).toList();
        if (Boolean.TRUE.equals(parameter.getParameter().get(CrudCommonEnum.QUERY_CREATE_UPDATE_USER.name()))) {
            this.userSetterService.setCreateUpdateUser(roleVoList);
        }
        return roleVoList;
    }

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
        // 保存
        if (!CollectionUtils.isEmpty(parameter.getFunctionIdList())) {
            return this.sysRoleFunctionService.saveBatch(
                parameter.getFunctionIdList().stream().map(item -> {
                    final SysRoleFunctionPO sysRoleFunction = new SysRoleFunctionPO();
                    sysRoleFunction.setFunctionId(item);
                    sysRoleFunction.setRoleId(parameter.getRoleId());
                    return sysRoleFunction;
                }).toList()
            );
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
                parameter.getUserIdList().stream().map(item -> new SysUserRolePO(item, parameter.getRoleId(), true)).toList()
        );
        return true;
    }

    @Autowired
    public void setUserSetterService(UserSetterService userSetterService) {
        this.userSetterService = userSetterService;
    }
}
