package com.smart.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.auth.core.i18n.AuthI18nMessage;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.auth.extensions.jwt.resolver.JwtResolver;
import com.smart.auth.extensions.jwt.store.CacheJwtStore;
import com.smart.commons.core.exception.SystemException;
import com.smart.commons.core.i18n.I18nException;
import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.system.model.SysUserPO;
import com.smart.system.pojo.dbo.SysUserWthAccountBO;
import com.smart.system.pojo.dto.ChangePasswordDTO;
import com.smart.system.pojo.dto.auth.OfflineDTO;
import com.smart.system.pojo.dto.auth.OnlineUserQueryDTO;
import com.smart.system.pojo.vo.SysOnlineUserVO;
import com.smart.system.service.SysUserAccountService;
import com.smart.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author ShiZhongMing
 * 2022/1/17
 * @since 1.0
 */
@RestController
@RequestMapping
@Api(tags = "系统认证管理")
@Slf4j
public class SysAuthController {

    private final SysUserService sysUserService;

    private final SysUserAccountService sysUserAccountService;

    private final CacheJwtStore cacheJwtStore;

    private final JwtResolver jwtResolver;

    public SysAuthController(SysUserService sysUserService, CacheJwtStore cacheJwtStore, SysUserAccountService sysAuthUserService, JwtResolver jwtResolver) {
        this.sysUserService = sysUserService;
        this.cacheJwtStore = cacheJwtStore;
        this.sysUserAccountService = sysAuthUserService;
        this.jwtResolver = jwtResolver;
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
        this.sysUserAccountService.changePassword(AuthUtils.getNonNullCurrentUserId(), parameter.getNewPassword());
        // 删除用户登录状态
        this.cacheJwtStore.invalidateByUsername(AuthUtils.getNonNullCurrentUser().getUsername());
        return Result.success(true);
    }

    /**
     * 获取所有在线用户
     * @return 在线用户列表
     */
    @PostMapping("auth/listOnlineUser")
    @ApiOperation(value = "查询所有在线用户")
    public Result<List<SysOnlineUserVO>> listOnlineUser(@RequestBody OnlineUserQueryDTO parameter) {
        Set<String> tokens = parameter.getUsername() == null ? this.cacheJwtStore.listAll() : this.cacheJwtStore.listAll(parameter.getUsername());
        return Result.success(this.sysUserAccountService.listOnlineUser(tokens));
    }

    @PostMapping("auth/offline")
    @ApiOperation(value = "用户离线操作")
    @Log(value = "用户离线操作", type = LogOperationTypeEnum.DELETE)
    @PreAuthorize("hasPermission('sys:auth', 'offline')")
    public Result<Boolean> offline(@RequestBody OfflineDTO parameter) {
        if (StringUtils.isNotBlank(parameter.getToken())) {
            RestUserDetails userDetails = this.jwtResolver.resolver(parameter.getToken());
            return Result.success(this.cacheJwtStore.invalidateByToken(userDetails.getUsername(), parameter.getToken()));
        }
        if (StringUtils.isNotBlank(parameter.getUsername())) {
            return Result.success(this.cacheJwtStore.invalidateByUsername(parameter.getUsername()));
        }
        return Result.success(false);
    }

    /**
     * 是否是初始化密码
     * @return 是否是初始化密码
     */
    @PostMapping("auth/isInitialPassword")
    @ApiOperation(value = "是否是初始化密码")
    public Result<Boolean> isInitialPassword() {
        List<SysUserWthAccountBO> userList = this.sysUserService.listUserWithAccount(
                new QueryWrapper<SysUserPO>()
                        .eq("A.user_id", AuthUtils.getNonNullCurrentUserId())
        );
        if (userList.isEmpty()) {
            // todo:国际化
            throw new SystemException("系统发生未知错误，未找到人员信息");
        }
        Boolean initPassword = userList.get(0).getInitialPasswordYn();
        return Result.success(initPassword == null || Boolean.TRUE.equals(initPassword));
    }
}
