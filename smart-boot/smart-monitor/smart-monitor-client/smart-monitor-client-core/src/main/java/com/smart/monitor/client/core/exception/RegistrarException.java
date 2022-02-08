package com.smart.monitor.client.core.exception;

import com.smart.commons.core.message.Result;
import lombok.Getter;

/**
 * @author ShiZhongMing
 * 2021/3/22 11:22
 * @since 2.0.0
 */
public class RegistrarException extends RuntimeException {

    private static final long serialVersionUID = -6717677133918741958L;
    @Getter
    private final Result<?> data;

    public RegistrarException(Result<?> data) {
        super(data.getMessage());
        this.data = data;
    }
}
