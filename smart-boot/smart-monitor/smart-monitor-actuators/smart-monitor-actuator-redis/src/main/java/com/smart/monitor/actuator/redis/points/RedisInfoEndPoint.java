package com.smart.monitor.actuator.redis.points;

import com.smart.commons.core.message.Result;
import com.smart.monitor.actuator.redis.constants.RedisEndPointIdConstant;
import com.smart.starter.redis.constants.RedisInfoParameterEnum;
import com.smart.starter.redis.service.RedisService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Reids INFO端点
 * @author ShiZhongMing
 * 2022/2/24
 * @since 2.0.0
 */
@Endpoint(id = RedisEndPointIdConstant.INFO)
@Component
public class RedisInfoEndPoint {

    private final RedisService redisService;

    public RedisInfoEndPoint(RedisService redisService) {
        this.redisService = redisService;
    }

    @ReadOperation
    public Object info(@Selector String parameter) {
        List<RedisInfoParameterEnum> parameterList = Arrays.stream(RedisInfoParameterEnum.values())
                .filter(item -> item.getParameter().equals(parameter))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(parameterList)) {
            return Result.failure(String.format("redis info 参数错误，只能是以下值[%s]", Arrays.stream(RedisInfoParameterEnum.values()).map(RedisInfoParameterEnum::getParameter).collect(Collectors.joining(","))));
        }
        return this.redisService.info(parameterList.get(0));
    }

    @ReadOperation
    public Object defaultInfo() {
        return this.redisService.info(null);
    }
}
