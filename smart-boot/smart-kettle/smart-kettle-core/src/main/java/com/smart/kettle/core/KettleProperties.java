package com.smart.kettle.core;

import com.smart.kettle.core.properties.LogDatabaseProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ShiZhongMing
 * 2021/7/16 13:03
 * @since 1.0
 */
@Getter
@Setter
public class KettleProperties {

    /**
     * 日志配置信息
     */
    private LogDatabaseProperties log = new LogDatabaseProperties();
}
