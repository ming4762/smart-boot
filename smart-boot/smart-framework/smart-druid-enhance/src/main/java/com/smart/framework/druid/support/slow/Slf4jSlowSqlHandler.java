package com.smart.framework.druid.support.slow;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * 日志记录慢SQL
 * @author ShiZhongMing
 * 2021/4/2 11:17
 * @since 1.0
 */
@Slf4j
public class Slf4jSlowSqlHandler extends AbstractSlowSqlHandler {

    /**
     * 慢SQL处理
     *
     * @param slowSqlData 慢SQL数据
     */
    @Override
    protected void doHandler(@NonNull SlowSqlData slowSqlData) {
        log.warn("Slow Sql: {}", slowSqlData);
    }
}
