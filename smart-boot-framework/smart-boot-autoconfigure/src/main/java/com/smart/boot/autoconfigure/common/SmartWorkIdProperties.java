package com.smart.boot.autoconfigure.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @author shizhongming
 * 2025/10/13 09:20
 * @since 5.0.0
 */
@Getter
@Setter
@ConfigurationProperties("smart.snowflake-work-id")
public class SmartWorkIdProperties {
    /**
     * 雪花ID工作ID配置
     */
    private RedisWorkId redis = new RedisWorkId();

    @Getter
    @Setter
    public static class RedisWorkId{
        private String workspace = "default";
    }
}
