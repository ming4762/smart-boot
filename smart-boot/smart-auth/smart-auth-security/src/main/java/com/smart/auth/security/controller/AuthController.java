package com.smart.auth.security.controller;

import com.smart.auth.core.annotation.NonUrlCheck;
import com.smart.auth.core.i18n.AuthI18nMessage;
import com.smart.auth.core.model.TempTokenData;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AuthCache;
import com.smart.auth.core.service.AuthUserService;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.auth.security.exception.PasswordChangeException;
import com.smart.auth.security.pojo.dto.ChangePasswordDTO;
import com.smart.auth.security.pojo.dto.TempTokenApplyDTO;
import com.smart.commons.core.i18n.I18nUtils;
import com.smart.commons.core.log.Log;
import com.smart.commons.core.log.LogOperationTypeEnum;
import com.smart.commons.core.message.Result;
import com.smart.commons.core.utils.Base64Utils;
import com.smart.commons.core.utils.IpUtils;
import com.smart.commons.core.utils.JsonUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author shizhongming
 * 2020/4/24 9:17 上午
 */
@RequestMapping
@RestController
@NonUrlCheck
public class AuthController {

    private final AuthProperties authProperties;

    private final AuthCache<String, Object> authCache;

    private final AuthUserService authUserService;

    public AuthController(AuthCache<String, Object> authCache, AuthProperties authProperties, AuthUserService authUserService) {
        this.authCache = authCache;
        this.authProperties = authProperties;
        this.authUserService = authUserService;
    }

    /**
     * 验证用户是否登录
     * @return 是否登录
     */
    @PostMapping("auth/isLogin")
    @ApiOperation(value = "判断是否登录")
    public Result<Boolean> isLogin() {
        RestUserDetails restUserDetails = AuthUtils.getCurrentUser();
        return Result.success(ObjectUtils.isNotEmpty(restUserDetails));
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
            throw new PasswordChangeException(AuthI18nMessage.PASSWORD_CHANGE_PASSWORD_INCONSISTENT);
        }
        // 判断旧密码是否正确
        if (!StringUtils.equals(parameter.getOldPassword(), AuthUtils.getNonNullCurrentUser().getPassword())) {
            throw new PasswordChangeException(AuthI18nMessage.PASSWORD_CHANGE_OLD_PASSWORD_ERROR);
        }
        return Result.success(this.authUserService.changePassword(parameter.getNewPassword()));
    }

    /**
     * 注册临时token
     * @param parameter 注册数据
     * @return 临时token
     */
    @ApiOperation(value = "申请临时token")
    @PostMapping("auth/tempToken/apply")
    public Result<String> applyTempToken(@RequestBody @Valid TempTokenApplyDTO parameter, HttpServletRequest request) {
        // 获取配置信息
        final AuthProperties.TempToken properties = this.authProperties.getTempToken();

        final TempTokenData tempTokenData = new TempTokenData();
        BeanUtils.copyProperties(parameter, tempTokenData);
        final RestUserDetails user = AuthUtils.getNonNullCurrentUser();
        if (StringUtils.isBlank(tempTokenData.getResource())) {
            throw new AccessDeniedException(I18nUtils.get(AuthI18nMessage.ERROR_TEMP_TOKEN_APPLY_RESOURCE_NULL));
        }
        // 验证权限
        boolean hasPermission = user.getPermissions().stream().anyMatch(item -> item.getAuthority().equals(tempTokenData.getResource()));
        if (!hasPermission) {
            throw new AccessDeniedException(I18nUtils.get(AuthI18nMessage.ERROR_TEMP_TOKEN_APPLY_RESOURCE_FAIL));
        }
        // 设置IP地址
        tempTokenData.setUserId(user.getUserId());
        tempTokenData.setIp(IpUtils.getIpAddr(request));

        String key = Base64Utils.encoder(JsonUtils.toJsonString(tempTokenData));
        // 默认有效期60秒
        this.authCache.put(key, tempTokenData, properties.getTimeout());
        return Result.success(key);
    }
}
