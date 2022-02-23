package com.smart.monitor.actuator.druid.points;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.DruidStatManagerFacade;
import com.smart.monitor.actuator.druid.constants.EndPointIdConstant;
import com.smart.monitor.actuator.druid.utils.DruidUtils;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.lang.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * druid sql监控端点
 * @author ShiZhongMing
 * 2021/4/2 12:51
 * @since 1.0
 */
@Endpoint(id = EndPointIdConstant.DRUID_SQL_END_POINT)
public class DruidSqlEndPoint {

    private static final DruidStatManagerFacade STAT_MANAGER_FACADE = DruidStatManagerFacade.getInstance();

    /**
     * 通过datasourceId查询SQL信息
     * 如果 datasourceId为空，则查询所有的
     * @param datasourceName 数据源名字
     * @return 数据源信息
     */
    @ReadOperation
    public List<Map<String, Object>> getSqlStatDataList(@Nullable String datasourceName) {
        return DruidUtils.getSqlStatDataList(datasourceName);
    }

    /**
     * 查询SQL详情
     * @param id SQL ID
     * @return SQL详情
     */
    @ReadOperation
    public Map<String, Object> getSqlStat(@Selector Integer id) {
        Map<String, Object> map = STAT_MANAGER_FACADE.getSqlStatData(id);
        if (map == null) {
            return new HashMap<>(0);
        }
        String dbType = (String) map.get("DbType");
        String sql = (String) map.get("SQL");

        map.put("formattedSql", SQLUtils.format(sql, dbType));
        List<SQLStatement> statementList = SQLUtils.parseStatements(sql, dbType);

        if (!statementList.isEmpty()) {
            SQLStatement sqlStmt = statementList.get(0);
            SchemaStatVisitor visitor = SQLUtils.createSchemaStatVisitor(DbType.valueOf(dbType));
            sqlStmt.accept(visitor);
            map.put("parsedTable", visitor.getTables().toString());
            map.put("parsedFields", visitor.getColumns().toString());
            map.put("parsedConditions", visitor.getConditions().toString());
            map.put("parsedRelationships", visitor.getRelationships().toString());
            map.put("parsedOrderbycolumns", visitor.getOrderByColumns().toString());
        }

        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
        Date maxTimespanOccurTime = (Date) map.get("MaxTimespanOccurTime");
        if (maxTimespanOccurTime != null) {
            map.put("MaxTimespanOccurTime", format.format(maxTimespanOccurTime));
        }
        return map;
    }
}
