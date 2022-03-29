package com.smart.monitor.core.utils;

import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author shizhongming
 * 2021/4/18 2:41 下午
 */
public class ConvertUtils {

    /**
     * BigDecimal 转为 Instant
     * @param value BigDecimal
     * @return Instant
     */
    public static Instant bigDecimalToInstant(@NonNull BigDecimal value) {
        final String[] values = value.toPlainString().split("\\.");
        return Instant.ofEpochSecond(Long.parseLong(values[0]), Long.parseLong(values[1]));
    }
}
