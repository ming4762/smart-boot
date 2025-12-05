package com.smart.framework.commons.core.utils.snowflake;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 默认实现类雪花ID工作ID分配器
 * 从主机名中提取数字作为WORK ID，若主机名中不包含数字，则返回0
 * @author shizhongming
 * 2025/9/30 15:07
 * @since 5.0.0
 */
@Slf4j
public class DefaultSnowflakeWorkIdAllocator extends AbstractSnowflakeWorkIdAllocator {

    private static final Pattern PATTERN_HOSTNAME = Pattern.compile("^.*?\\D+(\\d+)$");

    /**
     * 分配WORK ID
     *
     * @return WORK ID
     */
    @Override
    public synchronized long allocateWorkId() {
        long workId = getServerIdAsLong();
        if (workId > workerIdMax) {
            workId = workId % workerIdMax;
        }
        return workId;
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
