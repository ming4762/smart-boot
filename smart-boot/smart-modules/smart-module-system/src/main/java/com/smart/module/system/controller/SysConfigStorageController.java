package com.smart.module.system.controller;

import com.smart.framework.auth.core.utils.AuthUtils;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.crud.controller.BaseController;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.module.system.constants.SysConfigStorageIdentEnum;
import com.smart.module.system.model.SysConfigStoragePO;
import com.smart.module.system.pojo.dto.config.SysConfigStorageSaveUpdateDTO;
import com.smart.module.system.service.SysConfigStorageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

/**
* sys_config_storage - 配置存储表 Controller
* @author SmartCodeGenerator
* 2024年12月31日 11:27:39
*/
@RestController
@RequestMapping("/sys/configStorage")
public class SysConfigStorageController extends BaseController<SysConfigStorageService, SysConfigStoragePO> {

    @Override
    @PostMapping("save")
    @Operation(summary = "添加配置存储表")
    public Result<Boolean> save(@RequestBody SysConfigStoragePO model) {
        return Result.success(this.service.save(model));
    }

    @Override
    @PostMapping("update")
    @Operation(summary = "更新配置存储表")
    public Result<Boolean> update(@RequestBody SysConfigStoragePO model) {
        return super.update(model);
    }

    @Override
    @PostMapping("list")
    @Operation(summary = "查询角色列表（支持分页、实体类属性查询）")
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }

    @Override
    @Operation(summary = "通过ID查询")
    @PostMapping("getById")
    public Result<SysConfigStoragePO> getById(@RequestBody Serializable id) {
        return super.getById(id);
    }

    @Operation(summary = "查询当前登录人员vxe配置")
    @PostMapping("listCurrentUserConfig")
    public Result<List<SysConfigStoragePO>> listCurrentUserVxeConfig() {
        return Result.success(
                this.service.lambdaQuery()
                        .eq(SysConfigStoragePO::getBelongUserId, AuthUtils.getNonNullCurrentUserId())
                        .eq(SysConfigStoragePO::getConfigIdentifier, SysConfigStorageIdentEnum.UI_VXE_CUSTOM)
                        .list()
        );
    }

    @Operation(summary = "保存当前登录人员配置")
    @PostMapping("saveVxeConfig")
    public Result<Boolean> saveVxeConfig(@RequestBody SysConfigStorageSaveUpdateDTO parameter) {
        Long hasConfig = this.service.lambdaQuery()
                .eq(SysConfigStoragePO::getConfigIdentifier, SysConfigStorageIdentEnum.UI_VXE_CUSTOM)
                .eq(SysConfigStoragePO::getBelongUserId, AuthUtils.getNonNullCurrentUserId())
                .eq(SysConfigStoragePO::getConfigKey, parameter.getConfigKey())
                .count();
        if (hasConfig > 0) {
            return Result.success(this.service.lambdaUpdate()
                            .eq(SysConfigStoragePO::getConfigIdentifier, SysConfigStorageIdentEnum.UI_VXE_CUSTOM)
                            .eq(SysConfigStoragePO::getBelongUserId, AuthUtils.getNonNullCurrentUserId())
                            .eq(SysConfigStoragePO::getConfigKey, parameter.getConfigKey())
                            .set(SysConfigStoragePO::getConfigValue, parameter.getConfigValue())
                            .set(SysConfigStoragePO::getUpdateBy, AuthUtils.getCurrentUsername())
                            .set(SysConfigStoragePO::getUpdateTime, ZonedDateTime.now())
                            .set(SysConfigStoragePO::getUpdateUserId, AuthUtils.getNonNullCurrentUserId())
                    .update());
        }
        SysConfigStoragePO model = SysConfigStoragePO.builder()
                .configIdentifier(SysConfigStorageIdentEnum.UI_VXE_CUSTOM)
                .configKey(parameter.getConfigKey())
                .configValue(parameter.getConfigValue())
                .belongUserId(AuthUtils.getNonNullCurrentUserId())
                .build();
        return Result.success(this.service.save(model));
    }
}