package com.smart.auth.core.secret.store;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.time.Instant;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
@Slf4j
public abstract class AbstractAppNoncestrStore implements AppNoncestrStore {

    private static final String APP_NONCESTR_KEY = "noncestr";

    private final Duration noncestrTimeout;

    private final String prefix;

    protected AbstractAppNoncestrStore(String prefix, Duration noncestrTimeout) {
        this.noncestrTimeout = noncestrTimeout;
        this.prefix = prefix;
    }

    /**
     * 存储随机串
     * @param key key
     * @param now 当前时间
     * @param timeout 过期时间
     */
    protected abstract void doSave(@NonNull String key, @NonNull Instant now, @NonNull Duration timeout);

    @Override
    public boolean has(@NonNull String noncestr) {
        String key = this.getPrefixKey(noncestr);
        Instant createTime = this.get(key);
        if (createTime == null) {
            this.doSave(key, Instant.now(), this.noncestrTimeout);
            return false;
        }
        boolean timeout = createTime.plus(this.noncestrTimeout).isBefore(Instant.now());
        if (timeout) {
            this.delete(key);
        }
        return !timeout;
    }

    /**
     * 获取随机串插入时间
     * @param key key
     * @return 随机串插入时间
     */
    @Nullable
    protected abstract Instant get(@NonNull String key);

    /**
     * 删除随机串
     * @param key 用随机串生成的key
     */
    protected abstract void delete(@NonNull String key);

    protected String getPrefixKey(@NonNull String key) {
        return String.format("%s:%s:%s", this.prefix, APP_NONCESTR_KEY, key);
    }
}
