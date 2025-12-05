package com.smart.framework.crud.plus.tenant;

import com.smart.framework.commons.core.dto.auth.UserTenantDTO;
import com.smart.framework.commons.core.tenant.SmartTenantHolder;
import com.smart.framework.crud.plus.handlers.SmartTenantLineHandler;
import com.smart.framework.crud.plus.metadata.SmartTableInfo;
import com.smart.framework.crud.plus.metadata.TableTenantFieldInfo;
import com.smart.framework.crud.utils.CrudUtils;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.ibatis.mapping.SqlCommandType;

import java.util.List;
import java.util.Optional;

/**
 * 租户支持
 * @author shizhongming
 * 2024/4/9 0:16
 * @since 3.0.0
 */
public class DefaultTenantLineHandlerImpl implements SmartTenantLineHandler {
    /**
     * 获取租户 ID 值表达式，只支持单个 ID 值
     * <p>
     *
     * @return 租户 ID 值表达式
     */
    @Override
    public Expression getTenantId() {
        Long tenantId = SmartTenantHolder.getTenantId();
        if (tenantId == null) {
            return null;
        }
        return new LongValue(tenantId);
    }

    /**
     * 获取租户字段名
     * <p>
     * 默认字段名叫: tenant_id
     *
     * @return 租户字段名
     */
    @Override
    public String getTenantIdColumn(String table) {
        SmartTableInfo tableInfo = CrudUtils.getTableInfo(table);
        if (tableInfo == null) {
            return null;
        }
        // 从指定设置中获取
        String tenantFieldName = SmartTenantControl.getTableTenantField(table);
        if (tenantFieldName != null) {
            TableTenantFieldInfo setTenantField = tableInfo.getTenantFieldInfo(tenantFieldName);
            if (setTenantField != null) {
                return setTenantField.getTableFieldInfo().getColumn();
            }
        }
        // 获取的默认租户字段
        TableTenantFieldInfo tenantFieldInfo = tableInfo.getTenantFieldInfo();
        if (tenantFieldInfo == null) {
            return null;
        }
        return tenantFieldInfo.getTableFieldInfo().getColumn();
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 根据表名 sql类别判断是否忽略添加多租户条件
     *
     * @param table          表名
     * @param sqlCommandType SQL操作类型
     * @return 是否忽略, true:表示忽略，false:需要解析并拼接多租户条件
     */
    @Override
    public boolean ignoreTable(String table, SqlCommandType sqlCommandType) {
        SmartTableInfo tableInfo = CrudUtils.getTableInfo(table);
        if (tableInfo == null) {
            return true;
        }
        if (!tableInfo.supportTenant()) {
            return true;
        }

        // 从指定设置中获取
        SmartTenantIgnoreData ignoreData = SmartTenantControl.getIgnore(table);
        // 如果指定的表没有配置忽略租户，那么就从忽略所有租户中获取
        if (ignoreData == null) {
            ignoreData = SmartTenantControl.getIgnoreAll();
        }
        if (ignoreData != null) {
            return this.ignoreTable(
                    sqlCommandType,
                    ignoreData.getIgnoreCommandList(),
                    ignoreData.getPlatformTenantIgnoreCommandList()
            );
        }
        return this.ignoreTable(
                sqlCommandType,
                tableInfo.getTenantFieldInfo().getIgnoreCommandList(),
                tableInfo.getTenantFieldInfo().getPlatformTenantIgnoreCommandList()
        );
    }

    private boolean ignoreTable(SqlCommandType sqlCommandType, List<SqlCommandType> ignoreCommandList, List<SqlCommandType> platformTenantIgnoreCommandList) {
        if (ignoreCommandList.contains(sqlCommandType)) {
            return true;
        }
        boolean platformYn = Optional.ofNullable(SmartTenantHolder.get())
                .map(UserTenantDTO::getPlatformYn)
                .orElse(false);
        return platformYn && platformTenantIgnoreCommandList.contains(sqlCommandType);
    }
}
