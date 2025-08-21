package com.smart.framework.commons.core.utils;

import com.smart.framework.commons.core.utils.snowflake.SnowflakeIdGenerator;
import lombok.extern.slf4j.Slf4j;

/**
 * 64位ID生成器 提供吞出量
 * 但是会导致JS超长
 * @author shizhongming
 * 2025/8/20 19:52
 * @since 5.0.0
 */
@Slf4j
public class SmartIdGenerator64 {

    private SmartIdGenerator64() {
        throw new IllegalStateException("Utility class");
    }

    private static final SnowflakeIdGenerator SNOWFLAKE_ID_GENERATOR = new SnowflakeIdGenerator(
            10L,
            12L,
            SnowflakeIdGenerator.getWorkerId(),
            1L,
            1000L
    );

    /**
     * 生成ID
     * @return ID
     */
    public static synchronized long nextId() {
        return SNOWFLAKE_ID_GENERATOR.nextId();
    }
}
