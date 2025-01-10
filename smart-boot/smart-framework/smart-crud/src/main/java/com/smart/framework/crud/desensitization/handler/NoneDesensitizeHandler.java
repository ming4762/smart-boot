package com.smart.framework.crud.desensitization.handler;

import org.springframework.lang.NonNull;

/**
 * 不脱敏处理器
 * @author shizhongming
 * 2025/1/9 20:55
 * @since 5.0.0
 */
public class NoneDesensitizeHandler implements DesensitizeHandler{

    /**
     * 脱敏
     *
     * @param value 脱敏前的值
     * @return 脱敏后的值
     */
    @Override
    @NonNull
    public String desensitize(Object value) {
        throw new UnsupportedOperationException("NoneDesensitizeHandler不支持脱敏");
    }
}
