package com.smart.framework.commons.core.utils.snowflake;

/**
 * 抽象类实现雪花ID工作ID分配器
 * @author shizhongming
 * 2025/9/30 15:06
 * @since 5.0.0
 */
public abstract class AbstractSnowflakeWorkIdAllocator implements SnowflakeWorkIdAllocator {

    protected long workerIdMax;

    public void setWorkerIdBits(long workerIdBits) {
        this.workerIdMax = ((1L << workerIdBits) - 1) >> 1;
    }
}
