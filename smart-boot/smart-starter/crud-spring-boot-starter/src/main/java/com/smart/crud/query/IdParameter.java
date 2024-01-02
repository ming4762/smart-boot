package com.smart.crud.query;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ShiZhongMing
 * 2022/1/29
 * @since 1.0
 */
@Getter
@Setter
public class IdParameter {

    @NotNull(message = "ID不能为空")
    private Long id;
}
