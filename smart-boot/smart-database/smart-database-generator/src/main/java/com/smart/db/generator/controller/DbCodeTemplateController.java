package com.smart.db.generator.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.db.generator.model.DbCodeTemplateGroupPO;
import com.smart.db.generator.model.DbCodeTemplatePO;
import com.smart.db.generator.model.DbCodeTemplateUserGroupPO;
import com.smart.db.generator.pojo.dto.DbTemplateUserGroupSaveDTO;
import com.smart.db.generator.service.DbCodeTemplateGroupService;
import com.smart.db.generator.service.DbCodeTemplateService;
import com.smart.db.generator.service.DbCodeTemplateUserGroupService;
import io.swagger.annotations.ApiOperation;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/5/7 17:16
 * @since 1.0
 */
@RestController
@RequestMapping("db/code/template")
public class DbCodeTemplateController extends BaseController<DbCodeTemplateService, DbCodeTemplatePO> {

    private final DbCodeTemplateUserGroupService dbCodeTemplateUserGroupService;

    private final DbCodeTemplateGroupService templateGroupService;

    public DbCodeTemplateController(DbCodeTemplateUserGroupService dbCodeTemplateUserGroupService, DbCodeTemplateGroupService templateGroupService) {
        this.dbCodeTemplateUserGroupService = dbCodeTemplateUserGroupService;
        this.templateGroupService = templateGroupService;
    }

    /**
     * 保存接口
     * @param model 实体类
     * @return 保存结果
     */
    @Override
    @PostMapping("save")
    @PreAuthorize("hasPermission('db:template', 'save')")
    public Result<Boolean> save(@RequestBody DbCodeTemplatePO model) {
        return Result.success(this.service.saveWithUser(model, AuthUtils.getCurrentUserId()));
    }

    @Override
    @PostMapping("update")
    @PreAuthorize("hasPermission('db:template', 'update')")
    public Result<Boolean> update(@RequestBody DbCodeTemplatePO model) {
        return Result.success(this.service.updateWithUserById(model, AuthUtils.getCurrentUserId()));
    }

    @Override
    @PostMapping("saveUpdate")
    @PreAuthorize("hasPermission('db:template', 'save') or hasPermission('db:template', 'update')")
    public Result<Boolean> saveUpdate(@RequestBody DbCodeTemplatePO model) {
        return Result.success(this.service.saveOrUpdateWithAllUser(model, AuthUtils.getCurrentUserId()));
    }

    @Override
    @PostMapping("batchDeleteById")
    @PreAuthorize("hasPermission('db:template', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        return super.batchDeleteById(idList);
    }

    @Override
    @PostMapping("list")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Override
    @PostMapping("getById")
    public Result<DbCodeTemplatePO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    /**
     * 查询模板对应的用户组
     * @param templateId 模板ID
     * @return 用户组集合
     */
    @PostMapping("listUserGroupByTemplate")
    public Result<List<DbCodeTemplateUserGroupPO>> listUserGroupByTemplate(@RequestBody Long templateId) {
        return Result.success(
                this.dbCodeTemplateUserGroupService.list(
                        new QueryWrapper<DbCodeTemplateUserGroupPO>().lambda()
                        .eq(DbCodeTemplateUserGroupPO :: getTemplateId, templateId)
                )
        );
    }

    /**
     * 保存模板对应的用户组
     * @param parameter 参数
     * @return 结果
     */
    @PostMapping("saveTemplateUserGroup")
    @PreAuthorize("hasPermission('db:template', 'saveTemplateUserGroup')")
    public Result<Boolean> saveTemplateUserGroup(@RequestBody @Valid DbTemplateUserGroupSaveDTO parameter) {
         return Result.success(this.service.saveTemplateUserGroup(parameter));
    }

    /**
     * 查询分组列表
     * @return 分组列表
     */
    @PostMapping("listGroup")
    @ApiOperation("查询分组列表")
    public Result<List<DbCodeTemplateGroupPO>> listGroup() {
        return Result.success(
                this.templateGroupService.list(
                        new QueryWrapper<DbCodeTemplateGroupPO>().lambda()
                                .select(DbCodeTemplateGroupPO :: getGroupId, DbCodeTemplateGroupPO :: getGroupName, DbCodeTemplateGroupPO :: getSeq)
                                .orderByAsc(DbCodeTemplateGroupPO :: getSeq)
                )
        );
    }

    /**
     * 通过ID查询 模板分组
     * @param groupId groupId
     * @return DbCodeTemplateGroupPO
     */
    @PostMapping("getGroupById")
    @ApiOperation("通过ID查询模板分组")
    public Result<DbCodeTemplateGroupPO> getGroupById(@RequestBody Long groupId) {
        return Result.success(this.templateGroupService.getById(groupId));
    }

    @PostMapping("saveUpdateGroup")
    @ApiOperation("保存模板分组")
    @PreAuthorize("hasPermission('db:templateGroup', 'save') or hasPermission('db:templateGroup', 'update')")
    public Result<Boolean> saveUpdateGroup(@RequestBody DbCodeTemplateGroupPO templateGroup) {
        return Result.success(this.templateGroupService.saveOrUpdateWithAllUser(templateGroup, AuthUtils.getCurrentUserId()));
    }

    @PostMapping("deleteGroupById")
    @ApiOperation("通过ID删除模板分组")
    @PreAuthorize("hasPermission('db:templateGroup', 'delete')")
    public Result<Boolean> deleteGroupById(@RequestBody List<Long> idList) {
        return Result.success(this.templateGroupService.removeByIds(idList));
    }
}
