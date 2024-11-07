package com.smart.framework.crud.plus.handlers;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * 获取租户信息
 * @author shizhongming
 * 2024/4/11 14:56
 * @since 3.0.0
 */
public interface SmartTenantLineHandler extends TenantLineHandler {

    /**
     * 根据表名 sql类别判断是否忽略添加多租户条件
     * @param table 表名
     * @param sqlCommandType SQL操作类型
     * @return 是否忽略, true:表示忽略，false:需要解析并拼接多租户条件
     */
    default boolean ignoreTable(String table, SqlCommandType sqlCommandType) {
        return this.ignoreTable(table);
    }

    /**
     * 获取租户字段名
     * 默认：tenant_id
     * @param table 表名
     * @return 租户字段名
     */
    default String getTenantIdColumn(String table) {
        return this.getTenantIdColumn();
    }
}
