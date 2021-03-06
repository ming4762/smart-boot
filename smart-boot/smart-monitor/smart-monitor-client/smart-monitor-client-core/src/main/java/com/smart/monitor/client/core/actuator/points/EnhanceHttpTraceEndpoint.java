package com.smart.monitor.client.core.actuator.points;

import com.smart.monitor.client.core.trace.http.EnhanceHttpTraceRepository;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceEndpoint;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author ShiZhongMing
 * 2022/3/28
 * @since 2.0
 */
public class EnhanceHttpTraceEndpoint extends HttpTraceEndpoint {

    private final EnhanceHttpTraceRepository repository;

    /**
     * Create a new {@link HttpTraceEndpoint} instance.
     *
     * @param repository the trace repository
     */
    public EnhanceHttpTraceEndpoint(EnhanceHttpTraceRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @ReadOperation
    public List<HttpTrace> listAndClear(@Nullable Boolean clear) {
        if (Boolean.TRUE.equals(clear)) {
            return this.repository.removeAll();
        }
        return this.repository.findAll();
    }

    @Override
    public HttpTraceDescriptor traces() {
        return super.traces();
    }
}
