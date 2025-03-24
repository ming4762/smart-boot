package com.smart.module.system.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.smart.framework.commons.core.data.Tree;
import com.smart.framework.commons.core.exception.BusinessException;
import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.LogOperationTypeEnum;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.crud.controller.BaseController;
import com.smart.framework.crud.datapermission.handler.SmartDataPermissionMapperHolder;
import com.smart.framework.crud.parameter.SetUseYnParameter;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.module.system.model.SysDataPermissionPO;
import com.smart.module.system.pojo.dto.datapermission.SysDataPermissionSaveUpdateDTO;
import com.smart.module.system.pojo.vo.datapermission.SysDataPermissionListVO;
import com.smart.module.system.service.SysDataPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

/**
* sys_data_permission - 数据权限表 Controller
* @author SmartCodeGenerator
* 2025年3月7日 19:29:00
*/
@RestController
@RequestMapping("/sys/dataPermission/")
public class SysDataPermissionController extends BaseController<SysDataPermissionService, SysDataPermissionPO> {

    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Operation(summary = "保存操作")
    @PostMapping("save")
    @Log(value = "添加数据权限表", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:function', 'save')")
    public Result<Boolean> save(@RequestBody @Valid SysDataPermissionSaveUpdateDTO parameter) {
        // 校验编码是否重复
        long count = this.service.count(
                Wrappers.lambdaQuery(SysDataPermissionPO.class)
                        .eq(SysDataPermissionPO::getPermissionCode, parameter.getPermissionCode())
        );
        if (count > 0) {
            // TODO：国际化
            throw new BusinessException("数据权限编码已存在");
        }
        SysDataPermissionPO model = new SysDataPermissionPO();
        BeanUtils.copyProperties(parameter, model);
        SmartDataPermissionMapperHolder.clear();
        return super.save(model);
    }

    @Operation(summary = "保存操作")
    @PostMapping("update")
    @Log(value = "更新数据权限表", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('sys:function', 'update')")
    public Result<Boolean> update(@RequestBody @Valid SysDataPermissionSaveUpdateDTO parameter) {
        SysDataPermissionPO model = new SysDataPermissionPO();
        BeanUtils.copyProperties(parameter, model);
        SmartDataPermissionMapperHolder.clear();
        return super.update(model);
    }

    @Override
    @Operation(summary = "通过ID批量删除数据权限表")
    @PostMapping("batchDeleteById")
    @Log(value = "通过ID批量删除数据权限表", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:function', 'delete')")
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        SmartDataPermissionMapperHolder.clear();
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<SysDataPermissionPO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    /**
     * 设置启用停用
     *
     * @param parameter 参数
     * @return 是否设置成功
     */
    @Override
    @Operation(summary = "启用停用数据权限表")
    @PostMapping("setUseYn")
    @Log(value = "启用停用数据权限表", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:function', 'setUseYn')")
    public Result<Boolean> setUseYn(@RequestBody @Valid SetUseYnParameter parameter) {
        SmartDataPermissionMapperHolder.clear();
        return super.setUseYn(parameter);
    }

    @PostMapping("listAllWithFunction")
    @Operation(summary = "查询所有数据权限列表")
    public Result<List<Tree<SysDataPermissionListVO>>> listAllWithFunction() {
        return Result.success(this.service.listAllWithFunction());
    }
}