package com.smart.monitor.actuator.druid.points;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import com.smart.monitor.actuator.druid.constants.EndPointIdConstant;
import com.smart.monitor.actuator.druid.utils.DruidUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;

import java.util.List;
import java.util.Map;

/**
 * 数据源端点
 * @author ShiZhongMing
 * 2021/4/1 16:55
 * @since 1.0
 */
@Endpoint(id = EndPointIdConstant.DATASOURCE_END_POINT)
@Slf4j
public class DruidDataSourceEndPoint {

    private static final DruidStatManagerFacade STAT_MANAGER_FACADE = DruidStatManagerFacade.getInstance();

    /**
     * 名称列表
     */
    private static final String NAME_IDENT = "names";


    @ReadOperation
    public Object handler(@Selector(match = Selector.Match.ALL_REMAINING) String ident) {
        if (NAME_IDENT.equals(ident)) {
            return this.names();
        }
        return this.getDatasourceByName(ident);
    }

    /**
     * 获取数据库名称列表
     * @return 数据库名称列表
     */
    private List<String> names() {
        return STAT_MANAGER_FACADE.getDataSourceStatDataList().stream()
                .map(item -> item.get("Name").toString())
                .toList();
    }

    /**
     * 通过名字获取数据源信息
     * @param name 数据库名称
     * @return 数据源信息
     */
    private Map<String, Object> getDatasourceByName(@NonNull String name) {
        return DruidUtils.getDatasourceByName(name);
    }

    /**
     * 查询数据源列表
     * @return 数据验列表
     */
    @ReadOperation
    public List<Map<String, Object>> datasourceList() {
        return DruidUtils.datasourceList();
    }
}
