package com.smart.framework.commons.core.utils.snowflake;

/**
 * 雪花算法WORK ID分配器
 * 定义了雪花算法中WORK ID的分配策略
 * @author shizhongming
 * 2025/9/30 14:49
 * @since 5.0.0
 */
public interface SnowflakeWorkIdAllocator {

    /**
     * 分配WORK ID
     * @return WORK ID
     */
    long allocateWorkId();

}
