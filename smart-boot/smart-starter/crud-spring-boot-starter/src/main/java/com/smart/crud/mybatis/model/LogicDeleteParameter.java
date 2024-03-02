package com.smart.crud.mybatis.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 逻辑删除参数
 * @author shizhongming
 * 2023/3/2 18:26
 * @since 3.0.0
 */
@Getter
@Setter
public class LogicDeleteParameter implements Serializable {
    @Serial
    private static final long serialVersionUID = -8255650184834970771L;

    /**
     * 删除人ID
     */
    private Serializable deleteUserId;

    /**
     * 删除人
     */
    private String deleteBy;

    /**
     * 删除时间
     */
    private LocalDateTime deleteTime;
}
