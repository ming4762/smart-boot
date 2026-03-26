package com.smart.module.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.framework.auth.core.i18n.AuthI18nMessage;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.commons.core.exception.SystemException;
import com.smart.framework.commons.core.i18n.I18nException;
import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.LogOperationTypeEnum;
import com.smart.framework.commons.core.message.Result;
import com.smart.module.api.auth.AuthApi;
import com.smart.module.system.model.SysUserPO;
import com.smart.module.system.pojo.dbo.SysUserWthAccountBO;
import com.smart.module.system.pojo.dto.ChangePasswordDTO;
import com.smart.module.system.service.SysUserAccountService;
import com.smart.module.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * @author ShiZhongMing
 * 2022/1/17
 * @since 1.0
 */
@RestController
@RequestMapping
@Tag(name = "系统认证管理")
@Slf4j
public class SysAuthController {

    private final SysUserService sysUserService;

    private final SysUserAccountService sysUserAccountService;

    private final ObjectProvider<AuthApi> authApiProvider;

    public SysAuthController(SysUserService sysUserService, SysUserAccountService sysAuthUserService, ObjectProvider<AuthApi> authApiProvider) {
        this.sysUserService = sysUserService;
        this.sysUserAccountService = sysAuthUserService;
        this.authApiProvider = authApiProvider;
    }

    private AuthApi getNonnullAuthApi() {
        AuthApi authApi = authApiProvider.getIfAvailable();
        if (authApi == null) {
            throw new SystemException("操作失败，文件API未引入，请引入文件模块");
        }
        return authApi;
    }

    /**
     * 更改用户密码
     * @param parameter 参数
     * @return 是否更改成功
     */
    @PostMapping("sys/auth/changePassword")
    @Log(value = "更新密码", type = LogOperationTypeEnum.UPDATE, saveParameter = false, saveResult = true)
    @Operation(summary = "更改密码")
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
        this.sysUserAccountService.changePassword(AuthUtils.getNonNullCurrentUserId(), parameter.getNewPassword());
        // 删除用户登录状态
        this.getNonnullAuthApi().offlineByUsername(AuthUtils.getNonNullCurrentUser().getUsername());
        return Result.success(true);
    }

    /**
     * 是否是初始化密码
     * @return 是否是初始化密码
     */
    @PostMapping("auth/isInitialPassword")
    @Operation(summary = "是否是初始化密码")
    public Result<Boolean> isInitialPassword() {
        List<SysUserWthAccountBO> userList = this.sysUserService.listUserWithAccount(
                new QueryWrapper<SysUserPO>()
                        .eq("A.user_id", AuthUtils.getNonNullCurrentUserId())
        );
        if (userList.isEmpty()) {
            throw new SystemException("系统发生未知错误，未找到人员信息");
        }
        Boolean initPassword = userList.getFirst().getInitialPasswordYn();
        return Result.success(initPassword == null || Boolean.TRUE.equals(initPassword));
    }

}
