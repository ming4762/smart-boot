package com.smart.starter.log;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 日志配置类
 * @author jackson
 * 2019/12/13 15:28
 */
@ConfigurationProperties(prefix = "smart.log")
@Getter
@Setter
public class LogProperties {
    /**
     * 编码列表
     */
    private String codes;

    /**
     * 是否控制台打印
     */
    private Boolean console = Boolean.TRUE;

}
