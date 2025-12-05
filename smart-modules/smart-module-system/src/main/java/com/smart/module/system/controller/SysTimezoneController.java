package com.smart.module.system.controller;

import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.commons.core.message.Result;
import com.smart.module.api.system.SysDictApi;
import com.smart.module.api.system.SysUserApi;
import com.smart.module.api.system.dto.SysDictItemDTO;
import com.smart.module.api.system.dto.SysUserDTO;
import com.smart.module.system.model.SysUserPO;
import com.smart.module.system.pojo.dto.timezone.SysSetTimezoneDTO;
import com.smart.module.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 时区接口
 * @author shizhongming
 * 2025/10/30 10:05
 * @since 5.0.0
 */
@RestController
@RequestMapping("sys/timezone")
@RequiredArgsConstructor
@Tag(name = "时区接口")
public class SysTimezoneController {

    private static final String TIMEZONE_OPTIONS_DICT_KEY = "SYSTEM_TIMEZONE_OPTIONS";

    private final SysDictApi sysDictApi;
    private final SysUserApi sysUserApi;
    private final SysUserService sysUserService;


    @PostMapping("listTimezoneOptions")
    @Operation(summary = "查询时区选项")
    public Result<List<TimezoneOptionsVO>> listTimezoneOptions() {
        final List<SysDictItemDTO> dictItems = this.sysDictApi.listByDictCode(TIMEZONE_OPTIONS_DICT_KEY);
        if (CollectionUtils.isEmpty(dictItems)) {
            return Result.success(List.of());
        }
        return Result.success(dictItems.stream()
                .map(item -> new TimezoneOptionsVO(item.getDictItemName(), item.getDictItemCode()))
                .toList());
    }

    @PostMapping("getCurrentUserTimezone")
    @Operation(summary = "查询当前用户时区")
    public Result<String> getCurrentUserTimezone() {
        Long userId = AuthUtils.getNonNullCurrentUserId();
        SysUserDTO user = this.sysUserApi.getUserById(userId);
        if (user == null) {
            return Result.success();
        }
        return Result.success(user.getTimezone());
    }

    @PostMapping("setCurrentUserTimezone")
    @Operation(summary = "设置当前用户时区")
    public Result<Boolean> setCurrentUserTimezone(@RequestBody @Valid SysSetTimezoneDTO parameter) {
        Long userId = AuthUtils.getNonNullCurrentUserId();
        return Result.success(
                this.sysUserService.lambdaUpdate()
                        .set(SysUserPO::getTimezone, parameter.getTimezone())
                        .eq(SysUserPO::getUserId, userId)
                        .update()
        );
    }

    public record TimezoneOptionsVO(String label, String value) {
    }
}
