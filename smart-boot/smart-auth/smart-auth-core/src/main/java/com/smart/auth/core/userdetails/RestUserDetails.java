package com.smart.auth.core.userdetails;

import com.smart.auth.core.constants.AuthTypeEnum;
import com.smart.auth.core.constants.LoginTypeEnum;
import com.smart.auth.core.model.Permission;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
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
    Set<String> getRoles();

    /**
     * 获取权限
     * @return 权限编码列表
     */
    @NonNull
    Set<Permission> getPermissions();

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
    LocalDateTime getLoginTime();

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
}
