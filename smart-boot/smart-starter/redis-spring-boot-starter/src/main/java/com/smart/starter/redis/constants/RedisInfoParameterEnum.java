package com.smart.starter.redis.constants;

import lombok.Getter;

/**
 * @author ShiZhongMing
 * 2022/2/25
 * @since 2.0.0
 */
@Getter
public enum RedisInfoParameterEnum {

    /**
     * REDIS info 参数
     */
    SERVER("server", "有关Redis服务器的常规信息"),
    KEYSPACE("keyspace", "与数据库相关的统计"),
    MEMORY("memory", "内存消耗相关信息"),
    CLIENTS("clients", "客户端连接部分"),
    PERSISTENCE("persistence", "RDB和AOF相关信息"),
    STATS("stats", "一般统计"),
    REPLICATION("replication", "主/副本复制信息"),
    CPU("cpu", "CPU消耗统计信息"),
    COMMANDSTATS("commandstats", "Redis命令统计"),
    CLUSTER("cluster", " Redis群集部分"),
    ALL("all", "返回所有部分"),
    DEFAULT_PARAMETER("default", "仅返回默认的部分集"),
    ;

    private final String parameter;

    private final String description;

    RedisInfoParameterEnum(String parameter, String description) {
        this.parameter = parameter;
        this.description = description;
    }
}
