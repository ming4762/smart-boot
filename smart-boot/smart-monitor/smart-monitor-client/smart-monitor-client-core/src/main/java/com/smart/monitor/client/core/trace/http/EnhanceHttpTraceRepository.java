package com.smart.monitor.client.core.trace.http;

import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;

import java.util.List;

/**
 * 增强的HttpTraceRepository
 * @author ShiZhongMing
 * 2022/3/28
 * @since 2.0.0
 */
public interface EnhanceHttpTraceRepository extends HttpExchangeRepository {

    /**
     * 删除所有
     * @return 删除的元素
     */
    List<HttpExchange> removeAll();

    /**
     * 删除指定元素
     * @param httpTrace HttpTrace
     */
    void remove(HttpExchange httpTrace);
}
