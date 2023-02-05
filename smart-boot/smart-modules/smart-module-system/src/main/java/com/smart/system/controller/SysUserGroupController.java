package com.smart.system.controller;

import com.google.common.collect.ImmutableList;
import com.smart.auth.core.annotation.NonUrlCheck;
import com.smart.commons.core.http.HttpStatus;
import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.system.model.SysUserGroupPO;
import com.smart.system.model.SysUserPO;
import com.smart.system.pojo.dto.UserGroupUserSaveDTO;
import com.smart.system.pojo.dto.UserUserGroupSaveDTO;
import com.smart.system.service.SysUserGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 用户组controller
 * @author shizhongming
 * 2020/1/24 3:10 下午
 */
@RestController
@RequestMapping("sys/userGroup")
@Tag(name = "用户组管理", description = "系统模块")
@NonUrlCheck
public class SysUserGroupController extends BaseController<SysUserGroupService, SysUserGroupPO> {

    @Override
    @Operation(summary = "添加修改用户组")
    @PostMapping("saveUpdate")
    @Log(value = "添加修改用户组", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:userGroup', 'save') or hasPermission('sys:userGroup', 'update')")
    public Result<Boolean> saveUpdate(@RequestBody @Valid SysUserGroupPO model) {
        return Result.success(this.service.saveOrUpdate(model));
    }

    /**
     * 批量保存/更新
     *
     * @param modelList 数据列表
     */
    @Override
    @Operation(summary = "批量添加修改用户组")
    @PostMapping("batchSaveUpdate")
    @Log(value = "添加修改用户组", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:userGroup', 'save') or hasPermission('sys:userGroup', 'update')")
    public Result<Boolean> batchSaveUpdate(@RequestBody List<SysUserGroupPO> modelList) {
        return super.batchSaveUpdate(modelList);
    }

    /**
     * 执行删除
     * @param idList 用户组ID集合
     * @return 是否删除成功
     */
    @PreAuthorize("hasPermission('sys:userGroup', 'delete')")
    @Operation(summary = "通过ID批量删除用户组")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除用户组", type = LogOperationTypeEnum.DELETE)
    @Override
    public Result<Boolean> batchDeleteById(@NonNull @RequestBody List<Serializable> idList) {
        if (idList.isEmpty()) {
            return Result.ofStatus(HttpStatus.PARAM_NOT_NULL, "用户组ID集合不能为空");
        }
        return Result.success(this.service.removeByIds(idList));
    }

    @Override
    @PostMapping("list")
    @Operation(summary = "查询用户组列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    /**
     * 通过ID查询用户集合
     * @param id 用户组ID
     * @return 用户集合
     */
    @PostMapping("listUserById")
    @Operation(summary = "通过用户组ID查询用户列表", method = "POST")
    public Result<List<SysUserPO>> listUserById(@RequestBody Long id) {
        final Map<Long, List<SysUserPO>> result = this.service.listUserByIds(ImmutableList.of(id));
        if (result.containsKey(id)) {
            return Result.success(result.get(id));
        }
        return Result.success(ImmutableList.of());
    }

    /**
     * 通过ID查询用户ID集合
     * @param id 用户组ID
     * @return 用户ID集合
     */
    @PostMapping(value = "listUserIdById")
    @Operation(summary = "通过用户组ID查询用户ID列表")
    public Result<List<Long>> listUserIdById(@RequestBody Long id) {
        final Map<Long, List<Long>> result = this.service.listUserIdByIds(ImmutableList.of(id));
        if (result.containsKey(id)) {
            return Result.success(result.get(id));
        }
        return Result.success(ImmutableList.of());
    }

    /**
     * 保存用户
     * @param parameter 参数
     * @return 是否保存成功
     */
    @PostMapping("saveUserGroupByGroupId")
    @PreAuthorize("hasPermission('sys:userGroup', 'setUser')")
    @Operation(summary = "设置用户组包含的用户")
    @Log(value = "设置用户组包含的用户", type = LogOperationTypeEnum.UPDATE)
    public Result<Boolean> saveUserGroupByGroupId(@RequestBody @Valid UserGroupUserSaveDTO parameter) {
        return Result.success(this.service.saveUserGroupByGroupId(parameter));
    }

    /**
     * 保存用户
     * @param parameter 参数
     * @return 是否保存成功
     */
    @PostMapping("saveUserGroupByUserId")
    @Operation(summary = "设置用户所属用户组")
    @PreAuthorize("hasPermission('sys:userGroup', 'setUser')")
    @Log(value = "设置用户所属用户组", type = LogOperationTypeEnum.UPDATE)
    public Result<Boolean> saveUserGroupByUserId(@RequestBody @Valid UserUserGroupSaveDTO parameter) {
        return Result.success(this.service.saveUserGroupByUserId(parameter));
    }

    @Override
    @PostMapping("getById")
    @Operation(summary = "通过ID查询用户组信息")
    public Result<SysUserGroupPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }
}
