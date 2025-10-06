package com.smart.boot.autoconfigure.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redis配置属性
 * @author shizhongming
 * 2025/9/30 18:47
 * @since 5.0.0
 */
@Getter
@Setter
@ConfigurationProperties("smart.redis")
public class SmartRedisProperties {


    /**
     * 雪花ID工作ID配置
     */
    private SnowflakeWorkId snowflakeWorkId = new SnowflakeWorkId();

    @Getter
    @Setter
    public static class SnowflakeWorkId{

        private String workspace = "default";
    }
}
