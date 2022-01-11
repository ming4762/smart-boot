package com.smart.auth.core.secret.store;

import org.springframework.lang.NonNull;

/**
 * 随机字符串保存器
 * @author ShiZhongMing
 * @since 1.0
 */
public interface AppNoncestrStore {

    /**
     * 验证随机字符串
     * @param noncestr 随机字符串
     * @return 验证结果
     */
    boolean has(@NonNull String noncestr);
}
