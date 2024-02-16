package com.smart.system.pojo.dto.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author shizhongming
 * 2024/2/16 9:04
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class SysExceptionMarkResolvedParameter implements Serializable {
    @Serial
    private static final long serialVersionUID = 2979679555123237882L;

    private String resolvedMessage;

    private List<Long> exceptionIdList;
}
