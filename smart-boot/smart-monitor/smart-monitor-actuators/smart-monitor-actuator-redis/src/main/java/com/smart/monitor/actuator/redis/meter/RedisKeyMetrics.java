package com.smart.monitor.actuator.redis.meter;

import com.smart.monitor.actuator.redis.constants.RedisMetricsConstants;
import com.smart.starter.redis.model.RedisKeySpace;
import com.smart.starter.redis.service.RedisService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * redis key meter
 * @author ShiZhongMing
 * 2022/2/25
 * @since 2.0.0
 */
public class RedisKeyMetrics implements ApplicationListener<ApplicationReadyEvent>  {

    /**
     * 已经注册的tag列表
     */
    private static final Set<String> REGISTRY_TAGS = new HashSet<>();

    private final MeterRegistry meterRegistry;

    private final RedisService redisService;

    public RedisKeyMetrics(MeterRegistry meterRegistry, RedisService redisService) {
        this.meterRegistry = meterRegistry;
        this.redisService = redisService;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        this.getKeyNum(null);
    }

    private synchronized Long getKeyNum(String tag) {
        Map<String, RedisKeySpace> keySpaceMap = this.redisService.queryKeySpace();
        if (keySpaceMap.isEmpty()) {
            keySpaceMap.put("db0", new RedisKeySpace("db0", 0L, 0L, 0L));
        }
        // 未注册的 DB 进行注册
        keySpaceMap.forEach((key, value) -> {
            if (!REGISTRY_TAGS.contains(key)) {
                Gauge.builder(RedisMetricsConstants.KEYS.getMeterName(), this, obj -> obj.getKeyNum(key))
                        .tag("db", key)
                        .description(RedisMetricsConstants.KEYS.getDescription())
                        .register(meterRegistry);
                REGISTRY_TAGS.add(key);
            }
        });
        if (keySpaceMap.containsKey(tag)) {
            return keySpaceMap.get(tag).keyNum();
        }
        return 0L;
    }
}
