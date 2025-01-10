package com.smart.framework.crud.desensitization.handler;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 脱敏处理器
 * @author shizhongming
 * 2025/1/9 20:51
 * @since 5.0.0
 */
public interface DesensitizeHandler {

    /**
     * 脱敏
     * @param value 脱敏前的值
     * @return 脱敏后的值
     */
    @NonNull
    String desensitize(@Nullable Object value);

}
