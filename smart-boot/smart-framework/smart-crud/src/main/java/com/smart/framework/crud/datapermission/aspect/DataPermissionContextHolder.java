package com.smart.framework.crud.datapermission.aspect;

import com.smart.framework.crud.datapermission.annotation.SmartDataPermission;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据权限上下文
 * @author shizhongming
 * 2025/3/20 13:08
 * @since 5.0.0
 */
public class DataPermissionContextHolder {

    private DataPermissionContextHolder() {
        throw new IllegalStateException("Utility class");
    }

    private static final ThreadLocal<List<SmartDataPermission>> CONTEXT_HOLDER = ThreadLocal.withInitial(() -> new ArrayList<>(10));

    public static void set(List<SmartDataPermission> list) {
        CONTEXT_HOLDER.set(list);
    }

    public static List<SmartDataPermission> get() {
        return CONTEXT_HOLDER.get();
    }

    public static void clear() {
        CONTEXT_HOLDER.remove();
    }
}
