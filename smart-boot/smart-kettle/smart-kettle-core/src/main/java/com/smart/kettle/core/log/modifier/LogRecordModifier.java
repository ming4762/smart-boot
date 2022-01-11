package com.smart.kettle.core.log.modifier;

import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.logging.BaseLogTable;
import org.pentaho.di.core.logging.LogStatus;
import org.springframework.core.Ordered;

/**
 * @author ShiZhongMing
 * 2021/7/22 20:51
 * @since 1.0
 */
public interface LogRecordModifier extends Ordered {

    /**
     * 修改日志
     * @param rowMetaAndData 日志数据
     * @param status LogStatus
     * @param subject Trans
     * @param parent Object
     * @return RowMetaAndData
     */
    default RowMetaAndData modifyLogRecord(RowMetaAndData rowMetaAndData, LogStatus status, Object subject, Object parent) {
        return rowMetaAndData;
    }

    /**
     * 支持的日志类型
     * @return 支持的日志类型
     */
    Class<? extends BaseLogTable> support();

    /**
     * 修改器序号
     * 越小则执行优先级越高
     * @return int
     */
    @Override
    default int getOrder() {
        return 0;
    }
}
