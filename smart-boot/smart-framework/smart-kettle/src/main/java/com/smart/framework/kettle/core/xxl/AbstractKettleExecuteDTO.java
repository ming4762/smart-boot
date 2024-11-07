package com.smart.framework.kettle.core.xxl;

import com.smart.framework.kettle.core.properties.KettleDatabaseRepositoryProperties;
import lombok.Getter;
import lombok.Setter;
import org.pentaho.di.core.logging.LogLevel;

import java.util.Map;

/**
 * @author shizhongming
 * 2021/7/19 6:11 下午
 */
@Getter
@Setter
public abstract class AbstractKettleExecuteDTO {

    /**
     * 资源库参数
     */
    private KettleDatabaseRepositoryProperties kettleDatabaseRepositoryProperties;

    /**
     * 目录
     */
    private String directoryName;

    /**
     * 变量
     */
    private Map<String, String> variable;

    /**
     * 命名参数
     */
    private Map<String, String> namedParameter;

    /**
     * 日志级别
     */
    private LogLevel logLevel;
}
