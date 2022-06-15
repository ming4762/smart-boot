package com.smart.commons.core.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ShiZhongMing
 * 2022/6/15
 * @since 3.0.0
 */
@Getter
@Setter
public class ExceptionResult<T> extends Result<T> {

    @Schema(title = "异常编号")
    private Long exceptionNo;

    /**
     * 设置异常编码
     * @param no 异常编号
     * @return this
     */
    public ExceptionResult<T> exceptionNo(Long no) {
        this.exceptionNo = no;
        return this;
    }

    public ExceptionResult(Result<T> result, Long exceptionNo) {
        super(result.getCode(), result.getMessage(), result.isSuccess(), result.getData());
        this.exceptionNo = exceptionNo;
    }

    public static <T> ExceptionResult<T> build(Result<T> result, Long exceptionNo) {
        return new ExceptionResult<>(result, exceptionNo);
    }
}
