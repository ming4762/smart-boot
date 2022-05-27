package com.smart.db.generator.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.db.analysis.pojo.bo.TableViewBO;
import com.smart.db.generator.constants.DbCrudEnum;
import com.smart.db.generator.model.DbCodeConnectionUserGroupPO;
import com.smart.db.generator.model.DbConnectionPO;
import com.smart.db.generator.pojo.dto.DbConnectionSetUserGroupDTO;
import com.smart.db.generator.pojo.dto.DbTableQueryDTO;
import com.smart.db.generator.pojo.vo.connection.DbConnectionTestVO;
import com.smart.db.generator.service.DbCodeConnectionUserGroupService;
import com.smart.db.generator.service.DbConnectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/4/25 16:36
 * @since 1.0
 */
@RestController
@RequestMapping("db/connection")
@Tag(name = "数据库连接管理")
public class DbConnectionController extends BaseController<DbConnectionService, DbConnectionPO> {

    private final DbCodeConnectionUserGroupService dbCodeConnectionUserGroupService;

    public DbConnectionController(DbCodeConnectionUserGroupService dbCodeConnectionUserGroupService) {
        this.dbCodeConnectionUserGroupService = dbCodeConnectionUserGroupService;
    }

    @Override
    @PostMapping("save")
    @Operation(summary = "保存")
    @PreAuthorize("hasPermission('db:connection', 'save')")
    public Result<Boolean> save(@RequestBody DbConnectionPO model) {
        return super.save(model);
    }

    @Override
    @PostMapping("saveUpdate")
    @Operation(summary = "保存修改操作")
    @Log(value = "保存修改数据库连接信息", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('db:connection', 'update') or hasPermission('db:connection', 'update')")
    public Result<Boolean> saveUpdate(@RequestBody DbConnectionPO model) {
        return Result.success(this.service.saveOrUpdateWithAllUser(model, AuthUtils.getCurrentUserId()));
    }

    @Override
    @PostMapping("update")
    @Operation(summary = "更新")
    @PreAuthorize("hasPermission('db:connection', 'update')")
    public Result<Boolean> update(@RequestBody DbConnectionPO model) {
        return super.update(model);
    }

    @Override
    @PostMapping("batchDeleteById")
    @Operation(summary = "通过ID批量删除")
    @PreAuthorize("hasPermission('db:connection', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        return super.batchDeleteById(idList);
    }

    @Operation(summary = "查询列表")
    @Override
    @PostMapping("list")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<DbConnectionPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    @PostMapping("queryDbTable")
    @Operation(summary = "通过数据库表信息")
    public Result<TableViewBO> queryDbTable(@RequestBody @Valid DbTableQueryDTO parameter) {
        return Result.success(this.service.queryDbTable(parameter));
    }

    /**
     * 查询用户组ID
     * @param connectionId 数据库连接
     * @return 用户组ID列表
     */
    @PostMapping("listUserGroupId")
    @Operation(summary = "查询用户组ID")
    public Result<List<Long>> listUserGroupId(@RequestBody Long connectionId) {
        List<Long> userGroupIdList = this.dbCodeConnectionUserGroupService.list(
                new QueryWrapper<DbCodeConnectionUserGroupPO>().lambda()
                .select(DbCodeConnectionUserGroupPO :: getUserGroupId)
                .eq(DbCodeConnectionUserGroupPO :: getConnectionId, connectionId)
        ).stream().map(DbCodeConnectionUserGroupPO :: getUserGroupId)
                .collect(Collectors.toList());
        return Result.success(userGroupIdList);
    }

    @PostMapping("setUserGroup")
    @Operation(summary = "设置用户组")
    @PreAuthorize("hasPermission('db:connection', 'setUserGroup')")
    @Log(value = "数据库连接设置用户组", type = LogOperationTypeEnum.UPDATE)
    public Result<Boolean> setUserGroup(@RequestBody @Valid DbConnectionSetUserGroupDTO parameter) {
        return Result.success(this.service.setUserGroup(parameter));
    }

    /**
     * 通过权限查询
     * @param parameter 参数
     * @return 查询结果
     */
    @PostMapping("listByAuth")
    public Result<Object> listByAuth(@RequestBody PageSortQuery parameter) {
        parameter.getParameter().put(DbCrudEnum.LIST_BY_AUTH.name(), Boolean.TRUE);
        parameter.getParameter().put(DbCrudEnum.QUERY_CREATE_UPDATE_USER.name(), Boolean.TRUE);
        return super.list(parameter);
    }

    /**
     * 测试数据库连接
     * @param connectionId 数据库连接ID
     * @return 测试结果
     */
    @PostMapping("testConnection")
    public Result<DbConnectionTestVO> testConnection(@RequestBody Long connectionId) {
        try {
            boolean result = this.service.testConnection(connectionId);
            return Result.success(new DbConnectionTestVO(result, null));
        } catch (Exception e) {
            return Result.success(new DbConnectionTestVO(false, e.getMessage()));
        }
    }
}
