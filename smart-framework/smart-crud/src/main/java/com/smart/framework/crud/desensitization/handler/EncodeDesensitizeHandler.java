package com.smart.framework.crud.desensitization.handler;

import com.smart.framework.commons.core.utils.auth.AesUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.util.StringUtils;

/**
 * 编码脱敏处理器
 * @author shizhongming
 * 2025/1/9 21:09
 * @since 5.0.0
 */
public class EncodeDesensitizeHandler extends AbstractDesensitizeHandler {

    private static final String ENCODE_KEY = "1234567890123456";

    /**
     * 脱敏
     *
     * @param value 脱敏前的值
     * @return 脱敏后的值
     */
    @Override
    public String doDesensitize(@NonNull Object value) {
        String valueString = value.toString();
        if (!StringUtils.hasText(valueString)) {
            return "";
        }
        return AesUtils.encrypt(valueString, ENCODE_KEY);
    }
}
