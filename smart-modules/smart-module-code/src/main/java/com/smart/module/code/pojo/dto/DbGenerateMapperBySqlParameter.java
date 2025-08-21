package com.smart.module.code.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 *
 * @author shizhongming
 * 2025/8/19 11:25
 * @since 5.0.0
 */
@Getter
@Setter
public class DbGenerateMapperBySqlParameter implements Serializable {

    /**
     * 数据库连接ID
     */
    @NotNull(message = "数据库连接ID不能为空")
    private Long dbConnectionId;

    @NotBlank(message = "SQL不能为空")
    private String sql;

    @NotBlank(message = "包名不能为空")
    private String packageName;

    @NotBlank(message = "类名不能为空")
    private String className;

    @NotBlank(message = "方法名不能为空")
    private String methodName;
}
