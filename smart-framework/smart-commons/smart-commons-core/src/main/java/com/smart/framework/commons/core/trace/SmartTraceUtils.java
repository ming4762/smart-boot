package com.smart.framework.commons.core.trace;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.Getter;

/**
 * 提供trace_id相关的工具类
 * @author shizhongming
 * 2025/10/9 19:13
 * @since 5.0.0
 */
public class SmartTraceUtils {

    private SmartTraceUtils() {
        throw new IllegalStateException("Utility class");
    }

    @Getter
    private static Tracer tracer;

    /**
     * 获取trace_id
     * @return trace_id
     */
    public static String getTraceId() {
        if (tracer == null) {
            throw new UnsupportedOperationException("tracer is not set");
        }
        Span currentSpan = tracer.currentSpan();
        if (currentSpan == null) {
            return null;
        }
        return currentSpan.context().traceId();
    }

    public static void setTracer(Tracer tracer) {
        SmartTraceUtils.tracer = tracer;
    }
}
