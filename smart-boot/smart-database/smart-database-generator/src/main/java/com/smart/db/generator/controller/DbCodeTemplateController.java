package com.smart.db.generator.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.db.generator.model.DbCodeTemplatePO;
import com.smart.db.generator.model.DbCodeTemplateUserGroupPO;
import com.smart.db.generator.pojo.dto.DbTemplateUserGroupSaveDTO;
import com.smart.db.generator.service.DbCodeTemplateService;
import com.smart.db.generator.service.DbCodeTemplateUserGroupService;
import org.springframework.lang.NonNull;
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

    public DbCodeTemplateController(DbCodeTemplateUserGroupService dbCodeTemplateUserGroupService) {
        this.dbCodeTemplateUserGroupService = dbCodeTemplateUserGroupService;
    }

    /**
     * 保存接口
     * @param model 实体类
     * @return 保存结果
     */
    @Override
    @PostMapping("save")
    public Result<Boolean> save(@RequestBody DbCodeTemplatePO model) {
        return Result.success(this.service.saveWithUser(model, AuthUtils.getCurrentUserId()));
    }

    @Override
    @PostMapping("update")
    public Result<Boolean> update(@RequestBody DbCodeTemplatePO model) {
        return Result.success(this.service.updateWithUserById(model, AuthUtils.getCurrentUserId()));
    }

    @Override
    @PostMapping("saveUpdate")
    public Result<Boolean> saveUpdate(@RequestBody DbCodeTemplatePO model) {
        return Result.success(this.service.saveOrUpdateWithAllUser(model, AuthUtils.getCurrentUserId()));
    }

    @Override
    @PostMapping("batchDeleteById")
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
    public Result<Boolean> saveTemplateUserGroup(@RequestBody @Valid DbTemplateUserGroupSaveDTO parameter) {
         return Result.success(this.service.saveTemplateUserGroup(parameter));
    }
}
