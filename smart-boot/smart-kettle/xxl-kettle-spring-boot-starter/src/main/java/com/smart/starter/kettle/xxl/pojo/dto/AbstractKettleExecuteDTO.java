package com.smart.starter.kettle.xxl.pojo.dto;

import com.smart.kettle.core.properties.KettleDatabaseRepositoryProperties;
import lombok.Getter;
import lombok.Setter;
import org.pentaho.di.core.logging.LogLevel;

import java.util.HashMap;
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
    private Map<String, String> variableMap;

    /**
     * 命名参数
     */
    private Map<String, String> parameterMap;

    /**
     * 日志级别
     */
    private LogLevel logLevel;

    public Map<String, String> getVariableMap() {
        if (this.variableMap == null) {
            return new HashMap<>(0);
        }
        return variableMap;
    }

    public Map<String, String> getParameterMap() {
        if (this.parameterMap == null) {
            return new HashMap<>(0);
        }
        return parameterMap;
    }

    public LogLevel getLogLevel() {
        if (this.logLevel == null) {
            return LogLevel.BASIC;
        }
        return logLevel;
    }
}
