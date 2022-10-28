package com.smart.crud.datapermission;

/**
 * @author ShiZhongMing
 * 2022/10/14
 * @since 3.0.0
 */
public class DataPermissionHolder {

    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    public static void setSql(String sql) {
        THREAD_LOCAL.remove();
        THREAD_LOCAL.set(sql);
    }

    public static String getSql() {
        return THREAD_LOCAL.get();
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }
}
