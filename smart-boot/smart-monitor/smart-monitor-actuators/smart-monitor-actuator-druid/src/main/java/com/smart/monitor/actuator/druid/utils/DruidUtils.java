package com.smart.monitor.actuator.druid.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.DruidDataSourceStatManager;
import com.alibaba.druid.stat.DruidStatManagerFacade;
import com.alibaba.druid.util.DruidDataSourceUtils;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * druid 数据源工具类
 * @author ShiZhongMing
 * 2021/4/8 10:36
 * @since 1.0
 */
public class DruidUtils {

    private static final DruidStatManagerFacade STAT_MANAGER_FACADE = DruidStatManagerFacade.getInstance();

    /**
     * 是否是druid数据验
     * @param dataSource 数据源
     * @return true or false
     */
    public static boolean isDruidDatasource(@Nullable DataSource dataSource) {
        if (dataSource == null) {
            return false;
        }
        return dataSource instanceof DruidDataSource;
    }

    /**
     * 获取数据源列表
     * @return 数据源列表
     */
    public static List<Map<String, Object>> datasourceList() {
        return STAT_MANAGER_FACADE.getDataSourceStatDataList();
    }

    /**
     * 通过名字获取数据验信息
     * @param name 数据源名字
     * @return 数据源信息
     */
    public static Map<String, Object> getDatasourceByName(@NonNull String name) {
        List<Map<String, Object>> datasourceList = datasourceList().stream()
                .filter(item -> name.equals(item.get("Name")))
                .collect(Collectors.toList());
        if (datasourceList.size() > 0) {
            return datasourceList.get(0);
        }
        return new HashMap<>(0);
    }

    private static Set<Object> getDruidDataSourceInstances() {
        return DruidDataSourceStatManager.getInstances().keySet();
    }

    /**
     * 通过数据验名字查询SQL列表
     * @param datasourceName 数据源名字
     * @return SQL列表
     */
    public static List<Map<String, Object>> getSqlStatDataList(@Nullable String datasourceName) {
        // 获取所有数据源
        Set<Object> dataSources = getDruidDataSourceInstances();
        if (StringUtils.hasLength(datasourceName)) {
            dataSources = dataSources.stream()
                    .filter(item -> datasourceName.equals(DruidDataSourceUtils.getStatData(item).get("Name"))).collect(Collectors.toSet());

        }
        return getSqlStatDataList(dataSources);
    }

    /**
     * 通过数据源查询SQL列表
     * @param dataSources 数据源列表
     * @return SQL列表
     */
    public static List<Map<String, Object>> getSqlStatDataList(@NonNull Set<Object> dataSources) {
        if (dataSources.isEmpty()) {
            return new ArrayList<>(0);
        }
        final List<Map<String, Object>> sqlList = new ArrayList<>();
        dataSources.forEach(item -> sqlList.addAll(STAT_MANAGER_FACADE.getSqlStatDataList(item)));
        return sqlList;
    }


    /**
     * 获取防火墙信息
     * @param databaseName 数据库名字，如果databaseName为null，则查询所有
     * @return 防火墙信息
     */
    public static List<Map<String, Object>> getWallStat(@Nullable String databaseName) {
        List<Map<String, Object>> databaseList = new ArrayList<>();
        if (!StringUtils.hasLength(databaseName)) {
            databaseList.addAll(datasourceList());
        } else {
            final Map<String, Object> db = getDatasourceByName(databaseName);
            if (db != null) {
                databaseList.add(db);
            }
        }
        return databaseList.stream()
                .map(item -> STAT_MANAGER_FACADE.getWallStatMap((Integer) item.get("Identity")))
                .collect(Collectors.toList());
    }

}
