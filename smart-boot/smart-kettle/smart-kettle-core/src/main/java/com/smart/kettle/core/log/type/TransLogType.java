package com.smart.kettle.core.log.type;

import com.smart.kettle.core.log.table.*;
import lombok.Getter;
import org.pentaho.di.core.logging.*;
import org.pentaho.di.trans.TransMeta;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * trans日志类型
 * @author ShiZhongMing
 * 2020/8/24 13:35
 * @since 1.0
 */
@Getter
public enum TransLogType implements LogType {

    /**
     * 转换日志
     */
    TRANS_LOG(
            transMeta -> EnhancedTransLogTable.getDefault(transMeta, transMeta, transMeta.getSteps()),
            (transMeta, logTable) -> transMeta.setTransLogTable((TransLogTable) logTable)
    ),
    /**
     * 步骤日志
     */
    STEP_LOG(
            transMeta -> EnhancedStepLogTable.getDefault(transMeta, transMeta),
            (transMeta, logTable) -> transMeta.setStepLogTable((StepLogTable) logTable)
    ),
    METRICS_LOG(
            transMeta -> EnhancedMetricsLogTable.getDefault(transMeta, transMeta),
            (transMeta, logTable) -> transMeta.setMetricsLogTable((MetricsLogTable) logTable)
    ),
    /**
     * 性能日志
     */
    PERFORMANCE_LOG(
            transMeta -> EnhancedPerformanceLogTable.getDefault(transMeta, transMeta),
            (transMeta, logTable) -> transMeta.setPerformanceLogTable((PerformanceLogTable) logTable)
    ),
    /**
     * 转换日志通道
     */
    TRANS_CHANNEL_LOG(
            transMeta -> EnhancedChannelLogTable.getDefault(transMeta, transMeta),
            (transMeta, logTable) -> transMeta.setChannelLogTable((ChannelLogTable) logTable)
    )
    ;

    private final Function<TransMeta, BaseLogTable> getFunction;

    private final BiConsumer<TransMeta, BaseLogTable> setFunction;

    TransLogType(Function<TransMeta, BaseLogTable> getFunction, BiConsumer<TransMeta, BaseLogTable> setFunction) {
        this.getFunction = getFunction;
        this.setFunction = setFunction;
    }
}
