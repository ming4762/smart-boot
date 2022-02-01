package com.smart.crud.query;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ShiZhongMing
 * 2022/1/29
 * @since 1.0
 */
@Getter
@Setter
public class IdParameter {

    @NotNull(message = "ID不能为空")
    private Serializable id;
}
