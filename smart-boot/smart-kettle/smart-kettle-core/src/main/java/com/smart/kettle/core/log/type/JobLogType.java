package com.smart.kettle.core.log.type;

import com.smart.kettle.core.log.table.EnhancedChannelLogTable;
import com.smart.kettle.core.log.table.EnhancedJobEntryLogTable;
import com.smart.kettle.core.log.table.EnhancedJobLogTable;
import lombok.Getter;
import org.pentaho.di.core.logging.BaseLogTable;
import org.pentaho.di.core.logging.ChannelLogTable;
import org.pentaho.di.core.logging.JobEntryLogTable;
import org.pentaho.di.core.logging.JobLogTable;
import org.pentaho.di.job.JobMeta;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * job 日志类型
 * @author ShiZhongMing
 * 2020/8/24 14:02
 * @since 1.0
 */
@Getter
public enum JobLogType implements LogType {

    /**
     * job日志
     */
    JOB_LOG(
            (jobMate) -> EnhancedJobLogTable.getDefault(jobMate, jobMate),
            (jobMate, logTable ) -> jobMate.setJobLogTable((JobLogTable) logTable)
    ),
    /**
     * 作业项日志
     */
    JOB_ENTRY_LOG(
            (jobMate) -> EnhancedJobEntryLogTable.getDefault(jobMate, jobMate),
            (jobMate, logTable ) -> jobMate.setJobEntryLogTable((JobEntryLogTable) logTable)
    ),
    /**
     * job日志通道
     */
    JOB_CHANNEL_LOG(
            (jobMate) -> EnhancedChannelLogTable.getDefault(jobMate, jobMate),
            (jobMate, logTable ) -> jobMate.setChannelLogTable((ChannelLogTable) logTable)
    )
    ;
    private final Function<JobMeta, BaseLogTable> getFunction;

    private final BiConsumer<JobMeta, BaseLogTable> setFunction;

    JobLogType(Function<JobMeta, BaseLogTable> getFunction, BiConsumer<JobMeta, BaseLogTable> setFunction) {
        this.getFunction = getFunction;
        this.setFunction = setFunction;
    }
}
