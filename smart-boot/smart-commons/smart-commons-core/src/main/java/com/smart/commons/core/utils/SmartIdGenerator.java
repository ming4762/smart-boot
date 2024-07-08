package com.smart.commons.core.utils;

import com.smart.commons.core.exception.SystemException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shizhongming
 * 2024/7/7 1:02
 * @since 3.0.0
 */
@Slf4j
public class SmartIdGenerator {

    private SmartIdGenerator() {
        throw new IllegalStateException("Utility class");
    }

    private static final Pattern PATTERN_HOSTNAME = Pattern.compile("^.*\\D+([0-9]+)$");

    /**
     * 获取VM options前缀key
     */
    private static final String VM_KEY_PREFIX = "smart.id.";

    /**
     * 初始偏移时间戳 默认2000年
     */
    private static final long OFFSET = LocalDate.of(Integer.parseInt(getVmOptions("year", () -> "2000")), 1, 1).atStartOfDay(ZoneId.of("Z")).toEpochSecond();

    /**
     * 机器id (0~15 保留 16~31作为备份机器)
     */
    private static final long WORKER_ID;
    /**
     * 机器id所占位数 (5bit, 支持最大机器数 2^5 = 32)
     */
    private static final long WORKER_ID_BITS = 5L;
    /**
     * 自增序列所占位数 (16bit, 支持最大每秒生成 2^16 = 65536)
     */
    private static final long SEQUENCE_ID_BITS = 16L;


    /**
     * 机器id偏移位数
     */
    private static final long WORKER_SHIFT_BITS = SEQUENCE_ID_BITS;
    /**
     * 自增序列偏移位数
     */
    private static final long OFFSET_SHIFT_BITS = SEQUENCE_ID_BITS + WORKER_ID_BITS;


    /**
     * 机器标识最大值 (2^5 / 2 - 1 = 15)
     */
    private static final long WORKER_ID_MAX = ((1 << WORKER_ID_BITS) - 1) >> 1;
    /**
     * 备份机器ID开始位置 (2^5 / 2 = 16)
     */
    private static final long BACK_WORKER_ID_BEGIN = (1 << WORKER_ID_BITS) >> 1;


    /**
     * 自增序列最大值 (2^16 - 1 = 65535)
     */
    private static final long SEQUENCE_MAX = (1 << SEQUENCE_ID_BITS) - 1L;
    /**
     * 发生时间回拨时容忍的最大回拨时间 (秒)
     */
    private static final long BACK_TIME_MAX = 1L;

    /**
     * 生词生成ID的时间戳
     */
    private static long lastTimestamp = 0L;
    /**
     * 当前秒内序列 (2^16)
     */
    private static long sequence = 0L;
    /**
     * 备份机器位上次生成ID的时间戳 (秒)
     */
    private static long lastTimestampBak = 0L;
    /**
     * 备份机器当前秒内序列 (2^16)
     */
    private static long sequenceBak = 0L;

    static {
        long workerId = Long.parseLong(getVmOptions("workerId", () -> getDefaultWorkId() + ""));
        if (workerId < 0 || workerId > WORKER_ID_MAX) {
            throw new IllegalArgumentException(String.format("workerId范围: 0 ~ %d 目前: %d", WORKER_ID_MAX, workerId));
        }
        WORKER_ID = workerId;
    }

    /**
     * 生成雪花ID
     * @return 雪花ID
     */
    public static long nextId() {
        return nextId(SystemClock.now() / 1000);
    }

    /**
     * 生成雪花ID
     * @param timestamp 当前的时间戳/秒
     * @return ID
     */
    private static synchronized long nextId(long timestamp) {
        if (timestamp < lastTimestamp) {
            log.warn("时钟回拨, 启用备份机器ID: now: [{}] last: [{}]", timestamp, lastTimestamp);
            return nextIdBackup(timestamp);
        }
        // 开始下一秒
        if (timestamp != lastTimestamp) {
            lastTimestamp = timestamp;
            sequence = 0L;
        }
        if (0L == (++sequence & SEQUENCE_MAX)) {
            // 秒内序列用尽 使用备份段机器ID生成
            sequence--;
            return nextIdBackup(timestamp);
        }
        return ((timestamp - OFFSET) << OFFSET_SHIFT_BITS) | (WORKER_ID << WORKER_SHIFT_BITS) | sequence;
    }

    private static long nextIdBackup(long timestamp) {
        if (timestamp < lastTimestampBak) {
            if (lastTimestampBak - SystemClock.now() / 1000 <= BACK_TIME_MAX) {
                timestamp = lastTimestampBak;
            } else {
                throw new SystemException(String.format("时钟回拨: now: [%d] last: [%d]", timestamp, lastTimestampBak));
            }
        }
        if (timestamp != lastTimestampBak) {
            lastTimestampBak = timestamp;
            sequenceBak = 0L;
        }
        if (0L == (++sequenceBak & SEQUENCE_MAX)) {
            // 秒内序列用尽
            return nextIdBackup(timestamp + 1);
        }
        return ((timestamp - OFFSET) << OFFSET_SHIFT_BITS) | ((WORKER_ID ^ BACK_WORKER_ID_BEGIN) << WORKER_SHIFT_BITS) | sequenceBak;
    }

    private static String getVmOptions(String key, Supplier<String> defaultHandler) {
        String property = System.getProperty(VM_KEY_PREFIX + key);
        if (property != null) {
            return property;
        }
        return defaultHandler.get();
    }

    private static long getDefaultWorkId() {
        return getServerIdAsLong();
    }

    private static long getServerIdAsLong() {
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            Matcher matcher = PATTERN_HOSTNAME.matcher(hostname);
            if (matcher.matches()) {
                long n = Long.parseLong(matcher.group(1));
                if (n >= 0 && n < WORKER_ID_MAX) {
                    log.info("detect server id from host name {}: {}.", hostname, n);
                    return n;
                }
            }
        } catch (UnknownHostException e) {
            log.warn("unable to get host name. set server id = 0.");
        }
        return 0;
    }
}
