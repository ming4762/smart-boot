package com.smart.monitor.actuator.druid.meter;

import com.alibaba.druid.pool.DruidDataSource;
import com.smart.monitor.actuator.druid.constants.DatasourceMetricsConstants;
import com.smart.monitor.actuator.druid.utils.DruidUtils;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据源meter绑定
 * @author ShiZhongMing
 * 2021/4/2 14:21
 * @since 1.0
 */
public class DruidDatasourceMetrics implements ApplicationListener<ApplicationReadyEvent> {

    private final MeterRegistry meterRegistry;

    public DruidDatasourceMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    /**
     * 获取指标数据
     * @param datasourceMeter 数据源指标信息
     * @return 指标数据
     */
    private Double getMeterData(DatasourceMetricsConstants datasourceMeter, String name) {
        Map<String, Object> db = DruidUtils.getDatasourceByName(name);
        if (db == null) {
            return 0.0;
        }
        return Double.parseDouble(db.get(datasourceMeter.getProperty()).toString());
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        final List<String> dataSourceNameList = this.getDataSourceNames(event.getApplicationContext());
        // 获取所有数据源
        for (String name : dataSourceNameList) {
            for (DatasourceMetricsConstants item : DatasourceMetricsConstants.values()) {
                Gauge.builder(item.getMeterName(), this, obj -> obj.getMeterData(item, name))
                        .tag("name", name)
                        .description(item.getDescription())
                        .register(this.meterRegistry);
            }
        }
    }

    /**
     * 获取所有数据源名字
     * @param applicationContext ApplicationContext
     * @return druid 数据源名字
     */
    private List<String> getDataSourceNames(@NonNull ApplicationContext applicationContext) {
        return Arrays.stream(applicationContext.getBeanNamesForType(DataSource.class))
                .map(name -> applicationContext.getBean(name, DataSource.class))
                .filter(DruidUtils::isDruidDatasource)
                .map(dataSource -> ((DruidDataSource)dataSource).getName())
                .collect(Collectors.toList());
    }
}
