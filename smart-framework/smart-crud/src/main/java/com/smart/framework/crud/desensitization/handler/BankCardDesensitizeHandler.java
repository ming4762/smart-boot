package com.smart.framework.crud.desensitization.handler;

import com.smart.framework.commons.core.utils.DesensitizeUtils;
import org.jspecify.annotations.NonNull;

/**
 * 银行卡脱敏处理器
 * @author shizhongming
 * 2025/1/9 21:09
 * @since 5.0.0
 */
public class BankCardDesensitizeHandler extends AbstractDesensitizeHandler {

    /**
     * 脱敏
     *
     * @param value 脱敏前的值
     * @return 脱敏后的值
     */
    @Override
    public String doDesensitize(@NonNull Object value) {
        return DesensitizeUtils.bankCard(value.toString());
    }
}
