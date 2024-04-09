package com.smart.commons.core.tenant;

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

    private static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置租户ID
     * @param tenantId 租户ID
     */
    public static void set(Long tenantId) {
        THREAD_LOCAL.remove();
        THREAD_LOCAL.set(tenantId);
    }

    public static Long get() {
        return THREAD_LOCAL.get();
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }
}
