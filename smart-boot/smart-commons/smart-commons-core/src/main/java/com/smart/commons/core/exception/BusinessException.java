package com.smart.commons.core.exception;

import com.smart.commons.core.message.ResultCodeEnum;
import lombok.NoArgsConstructor;

/**
 * 业务异常封装
 * @author ShiZhongMing
 * 2021/3/3 15:46
 * @since 1.0
 */
@NoArgsConstructor
public class BusinessException extends BaseException {
    private static final long serialVersionUID = -4132232198964375274L;

    public BusinessException(String message) {
        this(message, null);
    }

    public BusinessException(Throwable e) {
        this(e.getMessage(), e);
    }

    public BusinessException(String message, Throwable e) {
        super(ResultCodeEnum.BUSINESS_ERROR.getCode(), message, e);
    }
}
