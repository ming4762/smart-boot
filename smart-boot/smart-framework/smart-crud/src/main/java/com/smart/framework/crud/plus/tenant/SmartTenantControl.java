package com.smart.framework.crud.plus.tenant;

import com.smart.framework.commons.core.exception.SystemException;
import com.smart.framework.crud.plus.metadata.SmartTableInfo;
import com.smart.framework.crud.plus.metadata.TableTenantFieldInfo;
import com.smart.framework.crud.utils.CrudUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.mapping.SqlCommandType;

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
    public static void changeIgnore(String tableName, List<SqlCommandType> ignoreCommandList, List<SqlCommandType> platformTenantIgnoreCommandList) {
        THREAD_IGNORE_LOCAL.get().put(tableName, new SmartTenantIgnoreData(ignoreCommandList, tableName, platformTenantIgnoreCommandList));
    }

    /**
     * 重置忽略租户的信息
     * @param tableName 表名
     */
    public static void restIgnore(String tableName) {
        THREAD_IGNORE_LOCAL.get().remove(tableName);
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
     * 重置租户字段
     * @param tableName 表名
     */
    public static void restTenantField(String tableName) {
        THREAD_FIELD_LOCAL.get().remove(tableName);
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
     * 清除threadLocal
     */
    public static void clear() {
        THREAD_IGNORE_LOCAL.remove();
        THREAD_FIELD_LOCAL.remove();
    }

    /**
     * 忽略租户的信息
     */
    @Getter
    @Setter
    @AllArgsConstructor
    public static class SmartTenantIgnoreData {
        private List<SqlCommandType> ignoreCommandList;
        private String tableName;
        private List<SqlCommandType> platformTenantIgnoreCommandList;
    }
}
