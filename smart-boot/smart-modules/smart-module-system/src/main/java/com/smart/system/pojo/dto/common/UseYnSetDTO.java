package com.smart.system.pojo.dto.common;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 设置启停状态参数
 * @author ShiZhongMing
 * 2021/12/17
 * @since 1.0.7
 */
@Getter
@Setter
@ToString
public class UseYnSetDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 6629810837312846715L;

    /**
     * ID列表
     */
    @NotNull(message = "{message.validate.useYn.idList.notNull}")
    @NotEmpty(message = "{message.validate.useYn.idList.notNull}")
    private List<Long> idList;

    @NotNull(message = "{message.validate.useYn.status.notNull}")
    private Boolean useYn;
}
