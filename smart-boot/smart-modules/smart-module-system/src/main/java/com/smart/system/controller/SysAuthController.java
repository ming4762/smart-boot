package com.smart.system.controller;

import com.smart.auth.core.i18n.AuthI18nMessage;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.auth.extensions.jwt.store.CacheJwtStore;
import com.smart.commons.core.i18n.I18nException;
import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.system.model.SysUserPO;
import com.smart.system.pojo.dto.ChangePasswordDTO;
import com.smart.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author ShiZhongMing
 * 2022/1/17
 * @since 1.0
 */
@RestController
@RequestMapping
@Api(tags = "系统认证管理")
public class SysAuthController {

    private final SysUserService sysUserService;

    private final CacheJwtStore cacheJwtStore;

    public SysAuthController(SysUserService sysUserService, CacheJwtStore cacheJwtStore) {
        this.sysUserService = sysUserService;
        this.cacheJwtStore = cacheJwtStore;
    }

    /**
     * 更改用户密码
     * @param parameter 参数
     * @return 是否更改成功
     */
    @PostMapping("auth/changePassword")
    @Log(value = "更新密码", type = LogOperationTypeEnum.UPDATE)
    @ApiOperation(value = "更改密码")
    public Result<Boolean> changePassword(@NonNull @RequestBody @Valid ChangePasswordDTO parameter) {
        // 验证密码是否一致
        if (!StringUtils.equals(parameter.getNewPassword(), parameter.getNewPasswordConfirm())) {
            throw new I18nException(AuthI18nMessage.PASSWORD_CHANGE_PASSWORD_INCONSISTENT);
        }
        // 新旧密码不能一致
        if (StringUtils.equals(parameter.getOldPassword(), parameter.getNewPassword())) {
            throw new I18nException(AuthI18nMessage.PASSWORD_CHANGE_OLD_NEW_PASSWORD_SAME);
        }
        // 获取当前用户的密码
        String oldPassword = Optional.ofNullable(this.sysUserService.getById(AuthUtils.getNonNullCurrentUserId()))
                .map(SysUserPO::getPassword)
                .orElse(null);
        // 判断旧密码是否正确
        if (!StringUtils.equals(parameter.getOldPassword(), oldPassword)) {
            throw new I18nException(AuthI18nMessage.PASSWORD_CHANGE_OLD_PASSWORD_ERROR);
        }
        this.sysUserService.changePassword(AuthUtils.getNonNullCurrentUserId(), parameter.getNewPassword());
        // 删除用户登录状态
        this.cacheJwtStore.invalidateByUsername(AuthUtils.getNonNullCurrentUser().getUsername());
        return Result.success(true);
    }
}
