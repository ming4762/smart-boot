package com.smart.monitor.client.core.actuator.points;

import com.smart.monitor.client.core.trace.http.EnhanceHttpTraceRepository;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.boot.actuate.web.exchanges.HttpExchangesEndpoint;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author ShiZhongMing
 * 2022/3/28
 * @since 2.0
 */
public class EnhanceHttpTraceEndpoint extends HttpExchangesEndpoint {

    private final EnhanceHttpTraceRepository repository;

    /**
     * Create a new {@link HttpExchangesEndpoint} instance.
     *
     * @param repository the trace repository
     */
    public EnhanceHttpTraceEndpoint(EnhanceHttpTraceRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @ReadOperation
    public List<HttpExchange> listAndClear(@Nullable Boolean clear) {
        if (Boolean.TRUE.equals(clear)) {
            return this.repository.removeAll();
        }
        return this.repository.findAll();
    }
}
