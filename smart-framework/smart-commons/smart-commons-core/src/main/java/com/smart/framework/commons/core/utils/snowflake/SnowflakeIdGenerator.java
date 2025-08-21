package com.smart.framework.commons.core.utils.snowflake;

import com.smart.framework.commons.core.exception.SystemException;
import com.smart.framework.commons.core.utils.SystemClock;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 雪花ID生成器
 * @author shizhongming
 * 2025/8/21 09:08
 * @since 5.0.0
 */
@Slf4j
public class SnowflakeIdGenerator {

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
     * 机器id 低半区为主，高半区为备
     */
    private final long workerId;

    private final long backWorkerIdBegin;

    /**
     * 生词生成ID的时间戳
     */
    private long lastTimestamp = 0L;
    /**
     * 当前秒内序列 (2^12)
     */
    private long sequence = 0L;
    /**
     * 备份机器位上次生成ID的时间戳 (秒)
     */
    private long lastTimestampBak = 0L;
    /**
     * 备份机器当前秒内序列 (2^12)
     */
    private long sequenceBak = 0L;

    /**
     * 时钟回拨的时间单位相当于毫秒的倍数
     */
    private final long backTimeUnit;

    private final long sequenceMax;

    private final long offsetShiftBits;

    /**
     * 发生时间回拨时容忍的最大回拨时间 (毫秒)
     */
    private final long backTimeMax;
    /**
     * 机器id偏移位数
     */
    private final long workerShiftBits;

    public SnowflakeIdGenerator(long workerIdBits, long sequenceIdBits, long workerId, long backTimeUnit, long backTimeMax) {
        long workerIdMax = ((1L << workerIdBits) - 1) >> 1;
        if (workerId < 0 || workerId > workerIdMax) {
            throw new IllegalArgumentException(String.format("workerId范围: 0 ~ %d 目前: %d", workerIdMax, workerId));
        }
        this.workerId = workerId;
        this.backTimeUnit = backTimeUnit;
        this.sequenceMax = (1L << sequenceIdBits) - 1L;
        this.offsetShiftBits = sequenceIdBits + workerIdBits;
        this.backTimeMax = backTimeMax;
        this.backWorkerIdBegin = (1L << workerIdBits) >> 1;
        this.workerShiftBits = sequenceIdBits;
    }

    /**
     * 生成雪花ID
     * @return 雪花ID
     */
    public long nextId() {
        return nextId(SystemClock.now() / backTimeUnit);
    }

    private synchronized long nextId(long timestamp) {
        if (timestamp < lastTimestamp) {
            log.warn("时钟回拨, 启用备份机器ID: now: [{}] last: [{}]", timestamp, lastTimestamp);
            return nextIdBackup(timestamp);
        }
        // 开始下一秒
        if (timestamp != lastTimestamp) {
            lastTimestamp = timestamp;
            sequence = 0L;
        }
        if (0L == (++sequence & this.sequenceMax)) {
            // 秒内序列用尽 使用备份段机器ID生成
            sequence--;
            return nextIdBackup(timestamp);
        }
        return ((timestamp - OFFSET) << offsetShiftBits) | (workerId << workerShiftBits) | sequence;
    }

    private long nextIdBackup(long timestamp) {
        if (timestamp < lastTimestampBak) {
            if (lastTimestampBak - SystemClock.now() / backTimeUnit <= backTimeMax / backTimeUnit) {
                timestamp = lastTimestampBak;
            } else {
                throw new SystemException(String.format("时钟回拨: now: [%d] last: [%d]", timestamp, lastTimestampBak));
            }
        }
        if (timestamp != lastTimestampBak) {
            lastTimestampBak = timestamp;
            sequenceBak = 0L;
        }
        if (0L == (++sequenceBak & sequenceMax)) {
            // 秒内序列用尽
            return nextIdBackup(timestamp + 1);
        }
        return ((timestamp - OFFSET) << offsetShiftBits) | ((workerId ^ backWorkerIdBegin) << workerShiftBits) | sequenceBak;
    }

    /**
     * 获取当前机器ID
     * @return 当前机器ID
     */
    public static Long getWorkerId() {
        return Long.parseLong(getVmOptions("workerId", () -> getDefaultWorkId() + ""));
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
                if (n >= 0) {
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
