package com.smart.framework.commons.core.tenant;

import com.smart.framework.commons.core.dto.auth.UserTenantDTO;
import org.springframework.lang.Nullable;

import java.util.Optional;

/**
 * 租户ID存储器
 * @author shizhongming
 * 2024/4/9 10:01
 * @since 3.0.0
 */
public class SmartTenantHolder {

    private SmartTenantHolder() {
        throw new IllegalStateException("Utility class");
    }

    private static final ThreadLocal<UserTenantDTO> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置租户ID
     * @param tenant 租户
     */
    public static void set(UserTenantDTO tenant) {
        THREAD_LOCAL.remove();
        THREAD_LOCAL.set(tenant);
    }

    @Nullable
    public static UserTenantDTO get() {
        return THREAD_LOCAL.get();
    }

    @Nullable
    public static Long getTenantId() {
        return Optional.ofNullable(get()).map(UserTenantDTO::getTenantId).orElse(null);
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }

    /**
     * 设置平台管理租户
     */
    public static void setPlatformTenant() {
        set(
                UserTenantDTO.builder()
                        .tenantId(1L)
                        .platformYn(true)
                        .useYn(true)
                        .build()
        );
    }
}
