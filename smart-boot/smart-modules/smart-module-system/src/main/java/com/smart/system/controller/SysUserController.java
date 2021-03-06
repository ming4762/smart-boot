package com.smart.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.smart.auth.core.annotation.NonUrlCheck;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.data.Tree;
import com.smart.commons.core.http.HttpStatus;
import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.commons.core.utils.TreeUtils;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.system.model.SysFunctionPO;
import com.smart.system.model.SysUserPO;
import com.smart.system.model.SysUserRolePO;
import com.smart.system.pojo.dto.common.UseYnSetDTO;
import com.smart.system.pojo.dto.user.UserSetRoleDTO;
import com.smart.system.pojo.dto.user.UserUpdateDTO;
import com.smart.system.pojo.vo.SysFunctionListVO;
import com.smart.system.service.SysUserRoleService;
import com.smart.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户controller层
 * @author shizhongming
 * 2020/1/24 2:22 下午
 */
@RestController
@RequestMapping("sys/user")
@Api(value = "用户管理", tags = "系统模块")
@NonUrlCheck
public class SysUserController extends BaseController<SysUserService, SysUserPO> {

    private final SysUserRoleService sysUserRoleService;

    public SysUserController(SysUserRoleService sysUserRoleService) {
        this.sysUserRoleService = sysUserRoleService;
    }

    /**
     * 添加保存方法
     * @param parameter 用户实体
     * @return 是否保存成功
     */
    @PostMapping("saveUpdate")
    @ApiOperation("添加/更新用户")
    @Log(value = "添加/更新用户", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:user', 'save') or hasPermission('sys:user', 'update')")
    public Result<Boolean> saveUpdate(@RequestBody @Valid UserUpdateDTO parameter) {
        SysUserPO model = new SysUserPO();
        BeanUtils.copyProperties(parameter, model);
        return Result.success(this.service.saveOrUpdateWithAllUser(model, 1L));
    }

    @PostMapping("getById")
    @ApiOperation("通过ID查询")
    @Override
    public Result<SysUserPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    @Override
    @PostMapping("save")
    @ApiOperation("添加用户")
    @Log(value = "添加用户", type = LogOperationTypeEnum.ADD)
    @PreAuthorize("hasPermission('sys:user', 'save')")
    public Result<Boolean> save(@RequestBody @Valid SysUserPO model) {
        return Result.success(this.service.saveWithUser(model, AuthUtils.getCurrentUserId()));
    }

    @Override
    @PostMapping("update")
    @ApiOperation("更新用户")
    @Log(value = "更新用户", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:user', 'update')")
    public Result<Boolean> update(@RequestBody SysUserPO model) {
        return super.update(model);
    }


    @PostMapping("list")
    @ApiOperation(value = "查询用户列表（支持分页、实体类属性查询）")
    @Override
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    /**
     * 通过ID批量删除
     * @param idList ID集合
     * @return 是否删除成功
     */
    @PreAuthorize("hasPermission('sys:user', 'delete')")
    @ApiOperation(value = "通过ID批量删除用户")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除用户", type = LogOperationTypeEnum.DELETE)
    @Override
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (idList.isEmpty()) {
            return Result.ofStatus(HttpStatus.PARAM_NOT_NULL, "用户ID集合不能为空");
        }
        return Result.success(this.service.removeByIds(idList));
    }

    /**
     * 查询用户菜单信息
     * @return 用户菜单
     */
    @ApiOperation(value = "查询用户菜单信息")
    @PostMapping("listUserMenu")
    public Result<List<SysFunctionListVO>> listUserMenu(@RequestBody List<Locale> localeList) {
        return Result.success(this.service.listCurrentUserMenu(localeList));
    }

    /**
     * 查询用户菜单树
     * @return 用户菜单树
     */
    @ApiOperation(value = "查询用户菜单树")
    @PostMapping("listUserMenuTree")
    public Result<List<Tree<SysFunctionPO>>> listUserMenuTree(@RequestBody List<Locale> localeList) {
        final List<SysFunctionListVO> sysFunctionList = this.service.listCurrentUserMenu(localeList);
        if (CollectionUtils.isEmpty(sysFunctionList)) {
            return Result.success(Lists.newArrayList());
        }
        return Result.success(TreeUtils.buildList(
                sysFunctionList.stream().map(item -> {
                    final Tree<SysFunctionPO> tree = new Tree<>();
                    tree.setId(item.getFunctionId());
                    tree.setData(item);
                    tree.setText(item.getFunctionName());
                    tree.setParentId(item.getParentId());
                    return tree;
                }).collect(Collectors.toList()),
                0L
        ));
    }

    /**
     * 通过用户ID查询角色ID
     * @param userId 用户ID
     * @return 角色ID
     */
    @ApiOperation(value = "查询角色ID列表")
    @PostMapping("listRoleId")
    public Result<Set<Long>> listRoleId(@RequestBody Long userId) {
        if (Objects.isNull(userId)) {
            return Result.success(Sets.newHashSet());
        }
        return Result.success(
                this.sysUserRoleService.list(
                        new QueryWrapper<SysUserRolePO>().lambda()
                        .select(SysUserRolePO :: getRoleId)
                        .eq(SysUserRolePO :: getUserId, userId)
                ).stream().map(SysUserRolePO :: getRoleId)
                .collect(Collectors.toSet())
        );
    }

    @PostMapping("setRole")
    @Log(value = "设置角色", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:user', 'setRole')")
    @ApiOperation(value = "设置角色")
    public Result<Boolean> setRole(@RequestBody @Valid UserSetRoleDTO parameter) {
        return Result.success(this.service.setRole(parameter));
    }

    /**
     * 通过角色ID查询用户信息
     * @param roleIdList 角色ID列表
     * @return 用户信息
     */
    @PostMapping("listUserByRoleId")
    @ApiOperation(value = "通过角色ID查询用户信息")
    public Result<List<SysUserPO>> listUserByRoleId(@RequestBody List<Long> roleIdList) {
        return Result.success(this.service.listUserByRoleId(roleIdList));
    }



    /**
     * 设置启用状态
     * @return 结果
     */
    @PostMapping("setUseYn")
    @Log(value = "设置用户启停状态", type = LogOperationTypeEnum.UPDATE)
    @ApiOperation(value = "设置用户启停状态")
    @PreAuthorize("hasPermission('sys:user', 'setUseYn')")
    public Result<Boolean> setUseYn(@RequestBody @Valid UseYnSetDTO parameter) {
        Lists.partition(parameter.getIdList(), 500).forEach(list -> this.service.update(
                new UpdateWrapper<SysUserPO>().lambda()
                        .set(SysUserPO :: getUseYn, parameter.getUseYn())
                        .in(SysUserPO :: getUserId, list)
        ));
        return Result.success(true);
    }
}
