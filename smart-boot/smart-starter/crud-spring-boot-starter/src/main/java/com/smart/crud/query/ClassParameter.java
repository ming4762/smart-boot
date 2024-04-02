package com.smart.crud.query;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 类反射查询参数
 * @author shizhongming
 * 2024/3/19 15:08
 * @since 3.0.0
 */
@Getter
public class ClassParameter implements Serializable {
    @Serial
    private static final long serialVersionUID = -6089195011861283130L;

    @NotNull(message = "类型不能为空")
    private String className;
}
