package com.smart.monitor.actuator.druid.support;

import com.alibaba.druid.proxy.jdbc.StatementProxy;
import lombok.NonNull;
import org.springframework.core.Ordered;

/**
 * @author ShiZhongMing
 * 2021/4/2 9:29
 * @since 1.0
 */
public interface SlowSqlHandler extends Ordered {

    /**
     * 慢SQL执行器
     * @param statementProxy statementProxy
     * @param millis 执行毫秒数
     * @param parameter 参数
     */
    void handler(@NonNull StatementProxy statementProxy, long millis, String parameter);
}
