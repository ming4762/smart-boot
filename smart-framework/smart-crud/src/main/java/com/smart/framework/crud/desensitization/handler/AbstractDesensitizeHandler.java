package com.smart.framework.crud.desensitization.handler;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * @author shizhongming
 * 2025/1/9 21:44
 * @since 5.0.0
 */
public abstract class AbstractDesensitizeHandler implements DesensitizeHandler {

    /**
     * 脱敏
     * @param value 脱敏前的值
     * @return 脱敏后的值
     */
    @NonNull
    @Override
    public String desensitize(@Nullable Object value) {
        if (value == null) {
            return "";
        }
        String result = doDesensitize(value);
        if (result == null) {
            return "";
        }
        return result;
    }

    /**
     * 脱敏
     * @param value 脱敏前的值
     * @return 脱敏后的值
     */
    protected abstract String doDesensitize(@NonNull Object value);
}
