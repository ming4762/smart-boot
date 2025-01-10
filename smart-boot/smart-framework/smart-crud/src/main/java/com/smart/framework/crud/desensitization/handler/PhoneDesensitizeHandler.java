package com.smart.framework.crud.desensitization.handler;

import com.smart.framework.commons.core.utils.DesensitizeUtils;
import org.springframework.lang.NonNull;

/**
 * 手机号脱敏处理器
 * @author shizhongming
 * 2025/1/9 21:09
 * @since 5.0.0
 */
public class PhoneDesensitizeHandler extends AbstractDesensitizeHandler {

    /**
     * 脱敏
     *
     * @param value 脱敏前的值
     * @return 脱敏后的值
     */
    @Override
    public String doDesensitize(@NonNull Object value) {
        return DesensitizeUtils.phone(value.toString());
    }
}
