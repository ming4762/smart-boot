package com.smart.kettle.core.log;

import com.smart.kettle.core.log.type.LogType;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义配置holder
 * @author ShiZhongMing
 * 2020/8/24 11:06
 * @since 1.0
 */
public class KettleLogConfigHolder {

    private static final ThreadLocal<Map<LogType, KettleLogConfig>> THREAD_LOCAL = ThreadLocal.withInitial(HashMap::new);

    private KettleLogConfigHolder() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取指定日志类型的配置信息
     * @param logType 日志类型
     * @return 配置信息
     */
    public static KettleLogConfig getConfig(@NonNull LogType logType) {
        Map<LogType, KettleLogConfig> map = THREAD_LOCAL.get();
        map.computeIfAbsent(logType, key -> new KettleLogConfig());
        return map.get(logType);
    }

    public static void clean() {
        THREAD_LOCAL.remove();
    }
}
