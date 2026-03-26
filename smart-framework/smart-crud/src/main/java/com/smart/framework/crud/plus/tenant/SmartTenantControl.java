package com.smart.framework.crud.plus.tenant;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.smart.framework.commons.core.exception.SystemException;
import com.smart.framework.crud.plus.metadata.SmartTableInfo;
import com.smart.framework.crud.plus.metadata.TableTenantFieldInfo;
import com.smart.framework.crud.utils.CrudUtils;
import org.apache.ibatis.mapping.SqlCommandType;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 租户控制器
 * @author shizhongming
 * 2025/2/12 11:25
 * @since 5.0.0
 */
public final class SmartTenantControl {

    private SmartTenantControl() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 忽略所有租户的key
     */
    private static final String IGNORE_ALL_KEY = "SMART_TENANT_IGNORE_ALL_KEY";

    /**
     * 忽略租户的信息
     */
    private static final ThreadLocal<Map<String, SmartTenantIgnoreData>> THREAD_IGNORE_LOCAL = ThreadLocal.withInitial(ConcurrentHashMap::new);
    /**
     * 租户字段
     */
    private static final ThreadLocal<Map<String, String>> THREAD_FIELD_LOCAL = ThreadLocal.withInitial(ConcurrentHashMap::new);

    /**
     * 设置忽略租户的信息
     * @param tableName 表名
     * @param ignoreCommandList 忽略的命令
     * @param platformTenantIgnoreCommandList 平台管理租户忽略的命令
     */
    public static SmartTenantIgnoreData ignore(String tableName, @Nullable List<SqlCommandType> ignoreCommandList, @Nullable List<SqlCommandType> platformTenantIgnoreCommandList) {
        SmartTenantIgnoreData smartTenantIgnoreData = new SmartTenantIgnoreData(tableName, ignoreCommandList, platformTenantIgnoreCommandList);
        THREAD_IGNORE_LOCAL.get().put(tableName, smartTenantIgnoreData);
        return smartTenantIgnoreData;
    }

    /**
     * 修改忽略租户的信息
     * @param tableClass 表类
     * @param ignoreCommandList 忽略的命令
     * @param platformTenantIgnoreCommandList 平台管理租户忽略的命令
     */
    public static SmartTenantIgnoreData ignore(Class<?> tableClass, @Nullable List<SqlCommandType> ignoreCommandList, @Nullable List<SqlCommandType> platformTenantIgnoreCommandList) {
        String tableName = CrudUtils.getTableName(tableClass);
        return ignore(tableName, ignoreCommandList, platformTenantIgnoreCommandList);
    }

    /**
     * 忽略所有租户的信息
     * @param ignoreCommandList 忽略的命令
     * @param platformTenantIgnoreCommandList 平台管理租户忽略的命令
     */
    public static SmartTenantIgnoreData ignoreAll(@Nullable List<SqlCommandType> ignoreCommandList, @Nullable List<SqlCommandType> platformTenantIgnoreCommandList) {
        return ignore(IGNORE_ALL_KEY, ignoreCommandList, platformTenantIgnoreCommandList);
    }

    /**
     * 重置忽略租户的信息
     * @param tableName 表名
     */
    public static void restIgnore(String tableName) {
        THREAD_IGNORE_LOCAL.get().remove(tableName);
    }

    /**
     * 重置忽略租户的信息
     * @param tableClass 表类
     */
    public static void restIgnore(Class<?> tableClass) {
        String tableName = CrudUtils.getTableName(tableClass);
        restIgnore(tableName);
    }

    /**
     * 重置忽略所有租户的信息
     */
    public static void restIgnoreAll() {
        restIgnore(IGNORE_ALL_KEY);
    }

    /**
     * 切换租户字段
     * @param tableName 表名
     * @param tenantField 租户字段
     */
    public static void switchTenantField(String tableName, String tenantField) {
        SmartTableInfo smartTableInfo = CrudUtils.getTableInfo(tableName);
        if (smartTableInfo == null) {
            throw new SystemException("表名错误，未找到表信息");
        }
        TableTenantFieldInfo tenantFieldInfo = smartTableInfo.getTenantFieldInfo(tenantField);
        if (tenantFieldInfo == null) {
            throw new SystemException("租户字段错误，未找到租户字段信息");
        }
        THREAD_FIELD_LOCAL.get().put(tableName, tenantField);
    }

    /**
     * 切换租户字段
     * @param tableClass 表类
     * @param column 租户字段
     */
    public static void switchTenantField(Class<?> tableClass, SFunction<?, ?> column) {
        String tableName = CrudUtils.getTableName(tableClass);
        String tenantField = CrudUtils.getJavaProperty(column);
        switchTenantField(tableName, tenantField);
    }

    /**
     * 重置租户字段
     * @param tableName 表名
     */
    public static void restTenantField(String tableName) {
        THREAD_FIELD_LOCAL.get().remove(tableName);
    }

     /**
     * 重置租户字段
     * @param tableClass 表类
     */
    public static void restTenantField(Class<?> tableClass) {
        String tableName = CrudUtils.getTableName(tableClass);
        restTenantField(tableName);
    }

    /**
     * 清除threadLocal
     */
    public static void clear() {
        THREAD_IGNORE_LOCAL.remove();
        THREAD_FIELD_LOCAL.remove();
    }

    /**
     * 获取租户字段
     * @param tableName 表名
     * @return 租户字段
     */
    static String getTableTenantField(String tableName) {
        return THREAD_FIELD_LOCAL.get().get(tableName);
    }

    /**
     * 获取忽略租户的信息
     * @param tableName 表名
     * @return 忽略租户的信息
     */
    static SmartTenantIgnoreData getIgnore(String tableName) {
        return THREAD_IGNORE_LOCAL.get().get(tableName);
    }

     /**
     * 获取忽略所有租户的信息
     * @return 忽略所有租户的信息
     */
    static SmartTenantIgnoreData getIgnoreAll() {
        return getIgnore(IGNORE_ALL_KEY);
    }
}
