package com.smart.framework.commons.core.utils;

import com.smart.framework.commons.core.utils.snowflake.AbstractSnowflakeWorkIdAllocator;
import com.smart.framework.commons.core.utils.snowflake.SnowflakeIdGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * @author shizhongming
 * 2024/7/7 1:02
 * @since 3.0.0
 */
@Slf4j
public class SmartIdGenerator {

    private SmartIdGenerator() {
        throw new IllegalStateException("Utility class");
    }

    private static SnowflakeIdGenerator snowflakeIdGenerator = null;

    private static final long WORKER_ID_BITS = 5L;

    private static final Supplier<SnowflakeIdGenerator> SNOWFLAKE_ID_GENERATOR_SUPPLIER = () -> {
        if (snowflakeIdGenerator != null) {
            return snowflakeIdGenerator;
        }
        AbstractSnowflakeWorkIdAllocator snowflakeWorkIdAllocator = ApplicationContextUtils.getBean(AbstractSnowflakeWorkIdAllocator.class);
        if (snowflakeWorkIdAllocator == null) {
            throw new IllegalArgumentException("SnowflakeWorkIdAllocator is null.");
        }
        snowflakeWorkIdAllocator.setWorkerIdBits(WORKER_ID_BITS);
        snowflakeIdGenerator = new SnowflakeIdGenerator(
                WORKER_ID_BITS,
                21L - WORKER_ID_BITS,
                1000L,
                1L,
                snowflakeWorkIdAllocator
        );
        return snowflakeIdGenerator;
    };

    /**
     * 生成ID
     * @return ID
     */
    public static synchronized long nextId() {
        return SNOWFLAKE_ID_GENERATOR_SUPPLIER.get().nextId();
    }
}
