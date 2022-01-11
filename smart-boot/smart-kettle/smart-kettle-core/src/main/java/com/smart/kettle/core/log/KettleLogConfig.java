package com.smart.kettle.core.log;

import lombok.Getter;
import lombok.Setter;

/**
 * kettle 日志配置
 * @author ShiZhongMing
 * 2021/7/16 9:16
 * @since 1.0
 */
@Getter
@Setter
public class KettleLogConfig {
    /**
     * 是否启用
     */
    private Boolean enable;

    /**
     * 表名称
     */
    private String tableName;
}
