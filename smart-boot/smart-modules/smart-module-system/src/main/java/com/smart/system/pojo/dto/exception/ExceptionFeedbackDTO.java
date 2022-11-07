package com.smart.system.pojo.dto.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2022/6/14
 * @since 3.0.0
 */
@ToString
@Getter
@Setter
public class ExceptionFeedbackDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5580343384344548420L;

    private List<Long> idList;

    private String feedbackMessage;
}
