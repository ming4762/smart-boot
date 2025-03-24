package com.smart.framework.crud.datapermission.handler;

import com.smart.module.api.crud.module.SmartDataPermissionModel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据权限控制器
 * @author shizhongming
 * 2025/3/5 21:02
 * @since 5.0.0
 */
public class SmartDataPermissionController {

    private SmartDataPermissionController() {
        throw new IllegalStateException("Utility class");
    }

    private static final ThreadLocal<Set<IgnoreData>> IGNORE_DATA = ThreadLocal.withInitial(HashSet::new);
    private static final ThreadLocal<List<SmartDataPermissionModel>> MANUAL_DATA_PERMISSION = ThreadLocal.withInitial(() -> new ArrayList<>(0));

    /**
     * 忽略所有数据权限
     */
    public static void ignoreAll() {
        IGNORE_DATA.get().add(new IgnoreData(IgnoreType.ALL, null));
    }

    /**
     * 根据表忽略数据权限
     * @param tableName 表名
     */
    public static void ignoreTable(String tableName) {
        IGNORE_DATA.get().add(new IgnoreData(IgnoreType.TABLE, tableName));
    }

    /**
     * 根据mapperId忽略数据权限
     * @param mapperId mapperId
     */
    public static void ignoreMapper(String mapperId) {
        IGNORE_DATA.get().add(new IgnoreData(IgnoreType.MAPPER, mapperId));
    }

    /**
     * 设置数据权限
     * @param dataPermission 数据权限模型
     */
    public static void setManualDataPermission(SmartDataPermissionModel dataPermission) {
        MANUAL_DATA_PERMISSION.get().add(dataPermission);
    }

    public static void clear() {
        IGNORE_DATA.remove();
        MANUAL_DATA_PERMISSION.remove();
    }

    static Set<IgnoreData> getIgnoreData() {
        return IGNORE_DATA.get();
    }

    static List<SmartDataPermissionModel> getManualDataPermission() {
        return MANUAL_DATA_PERMISSION.get();
    }

    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    static class IgnoreData {
        private IgnoreType type;

        private String value;
    }

    enum IgnoreType {
        /**
         * 忽略数据权限
         */
        TABLE,

        MAPPER,

        ALL
    }
}
