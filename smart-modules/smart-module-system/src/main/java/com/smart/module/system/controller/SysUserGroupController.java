package com.smart.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.framework.auth.common.annotation.NonUrlCheck;
import com.smart.framework.commons.core.http.HttpStatus;
import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.LogOperationTypeEnum;
import com.smart.framework.commons.core.message.PageData;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.crud.controller.BaseController;
import com.smart.framework.crud.parameter.SetUseYnParameter;
import com.smart.framework.crud.query.IdPageSortQuery;
import com.smart.framework.crud.query.IdParameter;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.utils.CrudPageHelper;
import com.smart.framework.crud.utils.CrudUtils;
import com.smart.module.system.model.SysUserGroupPO;
import com.smart.module.system.model.SysUserPO;
import com.smart.module.system.pojo.dto.UserGroupUserSaveDTO;
import com.smart.module.system.pojo.dto.UserUserGroupSaveDTO;
import com.smart.module.system.service.SysUserGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Tag(name = "用户组管理")
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
     * @param parameter 用户组ID
     * @return 用户集合
     */
    @PostMapping("listUserById")
    @Operation(summary = "通过用户组ID查询用户列表", method = "POST")
    public Result<List<SysUserPO>> listUserById(@RequestBody @Valid IdParameter parameter) {
        Long id = parameter.getId();
        final Map<Long, List<SysUserPO>> result = this.service.listUserByIds(List.of(id));
        if (result.containsKey(id)) {
            return Result.success(result.get(id));
        }
        return Result.success(List.of());
    }

    /**
     * 通过ID查询未绑定用户集合
     * @param parameter 用户组ID
     * @return 用户集合
     */
    @PostMapping("listNoBindUser")
    @Operation(summary = "通过用户组ID查询未绑定用户列表")
    public Result<PageData<SysUserPO>> listNoBindUser(@RequestBody @Valid IdPageSortQuery parameter) {
        QueryWrapper<SysUserPO> queryWrapper = CrudUtils.createQueryWrapperFromParameters(parameter, SysUserPO.class);
        Page<SysUserPO> page = this.doPage(parameter);
        Long id = parameter.getId();
        List<SysUserPO> userList = CrudPageHelper.withPage(page, () -> this.service.listNoBindUserByIds(List.of(id), queryWrapper));
        return Result.success(PageData.of(userList, page.getTotal()));
    }

    /**
     * 通过ID查询用户ID集合
     * @param id 用户组ID
     * @return 用户ID集合
     */
    @PostMapping(value = "listUserIdById")
    @Operation(summary = "通过用户组ID查询用户ID列表")
    public Result<List<Long>> listUserIdById(@RequestBody Long id) {
        final Map<Long, List<Long>> result = this.service.listUserIdByIds(List.of(id));
        if (result.containsKey(id)) {
            return Result.success(result.get(id));
        }
        return Result.success(List.of());
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

    @PostMapping("unBindUser")
    @PreAuthorize("hasPermission('sys:userGroup', 'setUser')")
    @Operation(summary = "用户组解绑用户")
    @Log(value = "用户组解绑用户", type = LogOperationTypeEnum.UPDATE)
    public Result<Boolean> unBindUser(@RequestBody @Valid UserGroupUserSaveDTO parameter) {
        return Result.success(this.service.unBindUser(parameter.getGroupId(), parameter.getUserIdList()));
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

    /**
     * 设置启用停用
     *
     * @param parameter 参数
     * @return 是否设置成功
     */
    @Override
    @PostMapping("setUseYn")
    @Operation(summary = "启用停用")
    @Log(value = "启用停用用户组", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:userGroup', 'useYn')")
    public Result<Boolean> setUseYn(@RequestBody @Valid SetUseYnParameter parameter) {
        return super.setUseYn(parameter);
    }
}
