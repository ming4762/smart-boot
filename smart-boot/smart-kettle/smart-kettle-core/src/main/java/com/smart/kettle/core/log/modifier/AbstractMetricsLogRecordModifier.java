package com.smart.kettle.core.log.modifier;

import com.smart.kettle.core.log.table.EnhancedMetricsLogTable;
import org.pentaho.di.core.logging.BaseLogTable;

/**
 * @author ShiZhongMing
 * 2021/7/22 21:07
 * @since 1.0
 */
public abstract class AbstractMetricsLogRecordModifier implements LogRecordModifier {
    @Override
    public Class<? extends BaseLogTable> support() {
        return EnhancedMetricsLogTable.class;
    }
}
