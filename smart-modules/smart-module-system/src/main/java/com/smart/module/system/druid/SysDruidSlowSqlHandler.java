package com.smart.module.system.druid;

import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.crud.utils.CrudUtils;
import com.smart.framework.druid.filter.stat.EnhancedStatFilter;
import com.smart.framework.druid.support.slow.AbstractSlowSqlHandler;
import com.smart.framework.druid.support.slow.SlowSqlData;
import com.smart.module.system.model.monitor.SmartMonitorSlowSqlPO;
import com.smart.module.system.service.monitor.SmartMonitorSlowSqlService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author shizhongming
 * 2025/3/2 19:25
 * @since 5.0.0
 */
@Component
@ConditionalOnClass(EnhancedStatFilter.class)
@Slf4j
@RequiredArgsConstructor
public class SysDruidSlowSqlHandler extends AbstractSlowSqlHandler {

    private final ObjectProvider<SmartMonitorSlowSqlService> smartMonitorSlowSqlServiceProvider;
    private final ObjectProvider<SysDruidSlowSqlHandler> sysDruidSlowSqlHandlerProvider;

    /**
     * 慢SQL处理
     *
     * @param slowSqlData 慢SQL数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void doHandler(@NonNull SlowSqlData slowSqlData) {
        this.sysDruidSlowSqlHandlerProvider.getObject().innerHandler(slowSqlData);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void innerHandler(@NonNull SlowSqlData slowSqlData) {
        // 排除插入慢SQL本身
        String sql = slowSqlData.getSql();
        String tableName = CrudUtils.getTableName(SmartMonitorSlowSqlPO.class);
        if (sql.contains(tableName)) {
            return;
        }
        ZonedDateTime zonedDateTime = Instant.ofEpochMilli(slowSqlData.getTimestamp())
                .atZone(ZoneId.systemDefault());
        SmartMonitorSlowSqlPO model = SmartMonitorSlowSqlPO.builder()
                .sqlId(slowSqlData.getSqlId())
                .dbType(slowSqlData.getDbType())
                .sqlText(slowSqlData.getSql())
                .parameter(slowSqlData.getParameter())
                .datasourceName(slowSqlData.getDatasourceName())
                .columnList(JsonUtils.toJsonString(slowSqlData.getColumnList()))
                .timestamp(zonedDateTime)
                .useTime(slowSqlData.getUseTime().toMillis())
                .build();
        this.smartMonitorSlowSqlServiceProvider.getObject().save(model);
    }
}
