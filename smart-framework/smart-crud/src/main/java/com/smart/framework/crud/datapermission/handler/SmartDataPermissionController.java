package com.smart.framework.crud.datapermission.handler;

import com.smart.framework.crud.utils.CrudUtils;
import com.smart.module.api.crud.module.SmartDataPermissionModel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;

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

    public static void ignoreTable(Class<?> tableClass) {
        String tableName = CrudUtils.getTableName(tableClass);
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
    public static void addManualDataPermission(SmartDataPermissionModel ...dataPermission) {
        if (dataPermission.length == 0) {
            return;
        }
        MANUAL_DATA_PERMISSION.get().addAll(Arrays.stream(dataPermission).toList());
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
        /**
         * 忽略类型
         */
        private IgnoreType type;

        /**
         * 表名或者mapperId
         */
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
