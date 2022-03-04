package com.smart.monitor.actuator.druid.points;

import com.smart.monitor.actuator.druid.constants.EndPointIdConstant;
import com.smart.monitor.actuator.druid.utils.DruidUtils;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * druid 防火墙端点
 * @author ShiZhongMing
 * 2021/4/2 13:53
 * @since 1.0
 */
@Endpoint(id = EndPointIdConstant.DRUID_WALL_END_POINT)
public class DruidWallEndPoint {

    @ReadOperation
    public Map<String, Object> handler(@Selector String datasourceName) {
        final List<Map<String, Object>> result = DruidUtils.getWallStat(datasourceName);
        if (result.isEmpty()) {
            return new HashMap<>(0);
        }
        return result.get(0) == null ? new HashMap<>(0) : result.get(0);
    }
}
