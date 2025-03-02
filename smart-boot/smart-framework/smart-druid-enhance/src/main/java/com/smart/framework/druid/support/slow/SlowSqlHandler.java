package com.smart.framework.druid.support.slow;

import com.alibaba.druid.proxy.jdbc.StatementProxy;
import lombok.NonNull;
import org.springframework.core.Ordered;

import java.time.Duration;

/**
 * 慢SQL处理器
 * @author ShiZhongMing
 * 2021/4/2 9:29
 * @since 1.0
 */
public interface SlowSqlHandler extends Ordered {

    /**
     * 慢SQL执行器
     * @param statementProxy statementProxy
     * @param useTime 执行时间
     * @param parameter 参数
     */
    void handler(@NonNull StatementProxy statementProxy, Duration useTime, String parameter);
}
