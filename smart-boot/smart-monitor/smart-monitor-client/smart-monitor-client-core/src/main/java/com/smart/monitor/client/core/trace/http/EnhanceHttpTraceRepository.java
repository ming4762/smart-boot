package com.smart.monitor.client.core.trace.http;

import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;

import java.util.List;

/**
 * 增强的HttpTraceRepository
 * @author ShiZhongMing
 * 2022/3/28
 * @since 2.0.0
 */
public interface EnhanceHttpTraceRepository extends HttpTraceRepository {

    /**
     * 删除所有
     * @return 删除的元素
     */
    List<HttpTrace> removeAll();

    /**
     * 删除指定元素
     * @param httpTrace HttpTrace
     */
    void remove(HttpTrace httpTrace);
}
