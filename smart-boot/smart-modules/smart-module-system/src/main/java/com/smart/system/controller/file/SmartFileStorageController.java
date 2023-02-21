package com.smart.system.controller.file;

import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.IdParameter;
import com.smart.crud.query.PageSortQuery;
import com.smart.system.model.file.SmartFileStoragePO;
import com.smart.system.pojo.dto.file.SmartFileStorageSaveUpdateDTO;
import com.smart.system.service.file.SmartFileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
* smart_file_storage - 文件存储器配置 Controller
* @author SmartCodeGenerator
* 2023-2-14 15:19:53
*/
@RestController
@Tag(name = "文件存储库")
@RequestMapping("smart/fileStorage")
public class SmartFileStorageController extends BaseController<SmartFileStorageService, SmartFileStoragePO> {

    @Override
    @PostMapping("save")
    @Operation(summary = "添加文件存储器配置")
    @Log(value = "添加文件存储器配置", type = LogOperationTypeEnum.ADD)
    @PreAuthorize("hasPermission('smart:fileStorage', 'save')")
    public Result<Boolean> save(@RequestBody SmartFileStoragePO model) {
        return Result.success(this.service.save(model));
    }

    @Override
    @PostMapping("update")
    @Operation(summary = "更新文件存储器配置")
    @Log(value = "更新文件存储器配置", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('smart:fileStorage', 'update')")
    public Result<Boolean> update(@RequestBody SmartFileStoragePO model) {
        return super.update(model);
    }

    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    @PreAuthorize("hasPermission('smart:fileStorage', 'query')")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Operation(summary = "批量添加修改文件存储器配置")
    @PostMapping("saveUpdateBatch")
    @Log(value = "批量添加修改文件存储器配置", type = LogOperationTypeEnum.UPDATE)
    @PreAuthorize("hasPermission('smart:fileStorage', 'update')")
    public Result<Boolean> saveUpdateBatch(@RequestBody @Valid List<SmartFileStorageSaveUpdateDTO> parameterList) {
        List<SmartFileStoragePO> modelList = parameterList.stream().map(item -> {
            SmartFileStoragePO model = new SmartFileStoragePO();
            BeanUtils.copyProperties(item, model);
            return model;
        }).collect(Collectors.toList());
        return super.batchSaveUpdate(modelList);
    }

    @Override
    @Operation(summary = "通过ID批量删除文件存储器配置")
    @PostMapping("batchDeleteById")
    @PreAuthorize("hasPermission('smart:fileStorage', 'delete')")
    @Log(value = "通过ID批量删除文件存储器配置", type = LogOperationTypeEnum.DELETE)
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.success(false);
        }
        return super.batchDeleteById(idList);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    @PreAuthorize("hasPermission('smart:fileStorage', 'query')")
    public Result<SmartFileStoragePO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    @PreAuthorize("hasPermission('smart:fileStorage', 'setDefault')")
    @PostMapping("setDefault")
    @Operation(summary = "设置默认")
    @Log(value = "设置默认", type = LogOperationTypeEnum.UPDATE)
    public Result<Boolean> setDefault(@RequestBody IdParameter parameter) {
        return Result.success(this.service.setDefault(parameter.getId()));
    }
}