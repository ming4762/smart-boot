package com.smart.framework.auth.common.userdetails;

import com.smart.framework.auth.common.constants.AuthTypeEnum;
import com.smart.framework.auth.common.constants.LoginTypeEnum;
import com.smart.framework.commons.core.dto.auth.AuthRole;
import com.smart.framework.commons.core.dto.auth.Permission;
import com.smart.framework.commons.core.dto.auth.UserTenantDTO;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author jackson
 * 2020/4/13 9:32 上午
 */
public interface RestUserDetails extends UserDetails {

    /**
     * 获取用户ID
     * @return 用户ID
     */
    Long getUserId();

    /**
     * 获取用户姓名
     * @return 姓名
     */
    String getFullName();

    /**
     * 获取角色
     * @return 角色编码列表
     */
    @NonNull
    Set<AuthRole> getRoles();

    /**
     * 获取权限
     * @return 权限编码列表
     */
    @NonNull
    Set<Permission> getPermissions();

    /**
     * 获取用户拥有的认证域列表
     * @return 认证域列表
     */
    Set<String> getUserAuthDomains();

    /**
     * 获取当前登录的认证域
     * 登录时由认证提供者设置，表示用户当前会话所属的认证域
     * @return 当前登录的认证域，未设置时返回 null
     */
    @Nullable
    String getCurrentAuthDomain();

    /**
     * 设置token
     * @param token token
     */
    void setToken(String token);

    /**
     * 获取token
     * @return token
     */
    String getToken();

     /**
     * 获取刷新token，jwt模式存在
     * @return 刷新token
     */
    @Nullable
    String getRefreshToken();

    /**
     * 获取区域信息
     * @return 区域信息
     */
    @Nullable
    String getLocale();


    /**
     * 获取登录类型
     * @return 登录类型
     */
    LoginTypeEnum getLoginType();

    /**
     * 获取登录时间
     * @return 登录时间
     */
    ZonedDateTime getLoginTime();

    /**
     * 获取认证类型
     * @return 认证类型
     */
    AuthTypeEnum getAuthType();

    /**
     * 获取IP白名单列表
     * @return IP白名单列表
     */
    List<String> getIpWhiteList();

    /**
     * 是否绑定IP
     * @return 是否绑定IP
     */
    Boolean getBindIp();

    /**
     * 获取登录IP
     * @return 登录IP
     */
    String getLoginIp();

    /**
     * 获取登录失败次数
     * @return 登录失败次数
     */
    Long getLoginFailTime();

    /**
     * 判断账户是否锁定
     * @return true：未锁定
     */
    Boolean getAccountNonLocked();

    /**
     * 获取租户信息
     * @return 租户信息
     */
    UserTenantDTO getUserTenant();

     /**
     * JWT模式是否开启权限缓存
     * @return JWT模式是否开启权限缓存
     */
    boolean isPermissionCache();

    /**
     * 获取额外信息
     * @return 额外信息
     */
    Serializable getExtra();
}
