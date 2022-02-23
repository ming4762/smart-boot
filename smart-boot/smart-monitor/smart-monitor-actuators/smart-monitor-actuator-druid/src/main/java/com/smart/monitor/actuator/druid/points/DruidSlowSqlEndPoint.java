package com.smart.monitor.actuator.druid.points;

import com.smart.monitor.actuator.druid.constants.EndPointIdConstant;
import com.smart.monitor.actuator.druid.support.SlowSqlData;
import com.smart.monitor.actuator.druid.support.SlowSqlStore;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * 慢SQL端点
 * @author ShiZhongMing
 * 2021/4/7 9:29
 * @since 1.0
 */
@Endpoint(id = EndPointIdConstant.DRUID_SLOW_SQL_END_POINT)
public class DruidSlowSqlEndPoint {

    private final SlowSqlStore slowSqlStore;

    public DruidSlowSqlEndPoint(SlowSqlStore slowSqlStore) {
        this.slowSqlStore = slowSqlStore;
    }

    /**
     * 查询所有
     * @param clear 是否清除
     * @return 慢SQL列表
     */
    @ReadOperation
    public List<SlowSqlData> all(@Nullable Boolean clear) {
        return this.slowSqlStore.list(Boolean.TRUE.equals(clear));
    }

    /**
     * 通过数据源名字查询
     * @return 慢SQL列表
     */
    @ReadOperation
    public List<SlowSqlData> listByDatasourceName(@Selector String name, @Nullable Boolean clear) {
        return this.slowSqlStore.listByDatasourceName(name, Boolean.TRUE.equals(clear));
    }
}
