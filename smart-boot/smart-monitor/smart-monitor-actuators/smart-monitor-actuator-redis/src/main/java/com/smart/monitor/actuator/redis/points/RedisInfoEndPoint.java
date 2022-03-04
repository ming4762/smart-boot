package com.smart.monitor.actuator.redis.points;

import com.smart.commons.core.message.Result;
import com.smart.monitor.actuator.redis.constants.RedisEndPointIdConstant;
import com.smart.starter.redis.constants.RedisInfoParameterEnum;
import com.smart.starter.redis.constants.RedisInfoResultEnum;
import com.smart.starter.redis.model.RedisInfo;
import com.smart.starter.redis.service.RedisService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Redis INFO端点
 * @author ShiZhongMing
 * 2022/2/24
 * @since 2.0.0
 */
@Endpoint(id = RedisEndPointIdConstant.INFO)
public class RedisInfoEndPoint {

    private static final String DB_PREFIX = "db";

    private static final Map<String, RedisInfoResultEnum> REDIS_INFO_RESULT_MAP = Arrays.stream(RedisInfoResultEnum.values())
            .collect(Collectors.toMap(RedisInfoResultEnum::getValue, item -> item));

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
        return this.convertRedisInfo(this.redisService.info(parameterList.get(0)));
    }

    @ReadOperation
    public Object defaultInfo() {
        return this.convertRedisInfo(this.redisService.info(null));
    }


    private List<RedisInfo> convertRedisInfo(Properties properties) {
        return properties.entrySet().stream()
                .map(item -> {
                    RedisInfoResultEnum redisInfoResultEnum = REDIS_INFO_RESULT_MAP.get(item.getKey().toString());
                    if (item.getKey().toString().startsWith(DB_PREFIX)) {
                        return new RedisInfo(RedisInfoParameterEnum.KEYSPACE.getParameter(), item.getKey().toString(), item.getValue().toString(), RedisInfoParameterEnum.KEYSPACE.getDescription());
                    }
                    if (redisInfoResultEnum == null) {
                        return null;
                    }
                    return new RedisInfo(redisInfoResultEnum.getParameter().getParameter(), item.getKey().toString(), item.getValue().toString(), redisInfoResultEnum.getDescription());
                }).filter(Objects::nonNull)
                .sorted(Comparator.comparing(RedisInfo::getGroup).thenComparing(RedisInfo::getKey))
                .collect(Collectors.toList());
    }
}
