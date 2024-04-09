package com.smart.crud.plus.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.smart.commons.core.tenant.SmartTenantHolder;
import com.smart.crud.plus.metadata.SmartTableInfo;
import com.smart.crud.utils.CrudUtils;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

/**
 * 租户支持
 * @author shizhongming
 * 2024/4/9 0:16
 * @since 3.0.0
 */
public class DefaultTenantLineHandlerImpl implements TenantLineHandler {
    /**
     * 获取租户 ID 值表达式，只支持单个 ID 值
     * <p>
     *
     * @return 租户 ID 值表达式
     */
    @Override
    public Expression getTenantId() {
        Long tenantId = SmartTenantHolder.get();
        if (tenantId == null) {
            return null;
        }
        return new LongValue(tenantId);
    }

    /**
     * 获取租户字段名
     * TODO:目前不支持动态
     * <p>
     * 默认字段名叫: tenant_id
     *
     * @return 租户字段名
     */
    @Override
    public String getTenantIdColumn() {
        return TenantLineHandler.super.getTenantIdColumn();
    }

    /**
     * 根据表名判断是否忽略拼接多租户条件
     * <p>
     * 默认都要进行解析并拼接多租户条件
     *
     * @param tableName 表名
     * @return 是否忽略, true:表示忽略，false:需要解析并拼接多租户条件
     */
    @Override
    public boolean ignoreTable(String tableName) {
        SmartTableInfo tableInfo = CrudUtils.getTableInfo(tableName);
        if (tableInfo == null) {
            return true;
        }
        return !tableInfo.supportTenant();
    }
}
