package com.smart.auth.security.controller;

import com.smart.auth.core.annotation.NonUrlCheck;
import com.smart.auth.core.i18n.AuthI18nMessage;
import com.smart.auth.core.model.TempTokenData;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AuthCache;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.auth.security.pojo.dto.TempTokenApplyDTO;
import com.smart.commons.core.i18n.I18nUtils;
import com.smart.commons.core.message.Result;
import com.smart.commons.core.utils.DigestUtils;
import com.smart.commons.core.utils.IpUtils;
import com.smart.commons.core.utils.JsonUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

    public AuthController(AuthCache<String, Object> authCache, AuthProperties authProperties) {
        this.authCache = authCache;
        this.authProperties = authProperties;
    }

    /**
     * 验证用户是否登录
     * @return 是否登录
     */
    @PostMapping("auth/isLogin")
    @Operation(summary = "判断是否登录")
    public Result<Boolean> isLogin() {
        RestUserDetails restUserDetails = AuthUtils.getCurrentUser();
        return Result.success(ObjectUtils.isNotEmpty(restUserDetails));
    }

    /**
     * 注册临时token
     * @param parameter 注册数据
     * @return 临时token
     */
    @Operation(summary = "申请临时token")
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
        boolean hasPermission = AuthUtils.isSuperAdmin();
        if (!hasPermission) {
            hasPermission = user.getPermissions().stream().anyMatch(item -> item.getAuthority().equals(tempTokenData.getResource()));
        }
        if (!hasPermission) {
            throw new AccessDeniedException(I18nUtils.get(AuthI18nMessage.ERROR_TEMP_TOKEN_APPLY_RESOURCE_FAIL));
        }
        // 设置IP地址
        tempTokenData.setUserId(user.getUserId());
        tempTokenData.setIp(IpUtils.getIpAddr(request));

        String key = DigestUtils.sha256(JsonUtils.toJsonString(tempTokenData), 1);
        // 默认有效期60秒
        this.authCache.put(key, tempTokenData, properties.getTimeout());
        return Result.success(key);
    }

//    /**
//     * 获取所有在线用户
//     * @return 在线用户列表
//     */
//    @PostMapping("auth/listOnlineUser")
//    @Operation(summary = "查询所有在线用户")
//    public Result<List<SysOnlineUserVO>> listOnlineUser(@RequestBody OnlineUserQueryDTO parameter) {
//        Set<String> tokens = parameter.getUsername() == null ? this.cacheJwtStore.listAll() : this.cacheJwtStore.listAll(parameter.getUsername());
//        return Result.success(this.sysUserAccountService.listOnlineUser(tokens));
//    }
//
//    @PostMapping("auth/offline")
//    @Operation(summary = "用户离线操作")
//    @Log(value = "用户离线操作", type = LogOperationTypeEnum.DELETE)
//    @PreAuthorize("hasPermission('sys:auth', 'offline')")
//    public Result<Boolean> offline(@RequestBody OfflineDTO parameter) {
//        if (StringUtils.isNotBlank(parameter.getToken())) {
//            RestUserDetails userDetails = this.jwtResolver.resolver(parameter.getToken());
//            return Result.success(this.cacheJwtStore.invalidateByToken(userDetails.getUsername(), parameter.getToken()));
//        }
//        if (StringUtils.isNotBlank(parameter.getUsername())) {
//            return Result.success(this.cacheJwtStore.invalidateByUsername(parameter.getUsername()));
//        }
//        return Result.success(false);
//    }
}
