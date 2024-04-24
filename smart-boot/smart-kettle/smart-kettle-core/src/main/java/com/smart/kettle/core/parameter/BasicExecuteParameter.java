package com.smart.kettle.core.parameter;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.pentaho.di.core.logging.LogLevel;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * kettle 执行参数
 * @author shizhongming
 * 2024/3/13 8:57
 * @since 3.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BasicExecuteParameter implements Serializable {

    @Serial
    private static final long serialVersionUID = 5274388581745278731L;

    /**
     * 变量
     */
    private Map<String, String > variable;

    /**
     * 命名参数
     */
    private Map<String, String> namedParameter;

    @Builder.Default
    private LogLevel logLevel = LogLevel.BASIC;
}
