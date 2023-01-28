package com.smart.monitor.client.core.trace.http;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2022/3/28
 * @since 2.0.0
 */
public class InMemoryEnhanceHttpTraceRepository implements EnhanceHttpTraceRepository {

    /**
     * 内存存储器 最多存储50000条HttpTrace
     */
    private static final Collection<HttpExchange> CIRCULAR_FIFO_QUEUE = Collections.synchronizedCollection(new CircularFifoQueue<>(50000));

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * 排除的URL
     */
    private final List<String> excludeUrls;

    public InMemoryEnhanceHttpTraceRepository(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    @Override
    public List<HttpExchange> removeAll() {
        List<HttpExchange> result = this.findAll();
        CIRCULAR_FIFO_QUEUE.clear();
        return result;
    }

    @Override
    public void remove(HttpExchange httpTrace) {
        CIRCULAR_FIFO_QUEUE.remove(httpTrace);
    }

    @Override
    public List<HttpExchange> findAll() {
        return new ArrayList<>(CIRCULAR_FIFO_QUEUE);
    }

    @Override
    public void add(HttpExchange trace) {
        boolean exclude = this.excludeUrls.stream().anyMatch(item -> PATH_MATCHER.match(item, trace.getRequest().getUri().getPath()));
        if (!exclude) {
            CIRCULAR_FIFO_QUEUE.add(trace);
        }
    }
}
