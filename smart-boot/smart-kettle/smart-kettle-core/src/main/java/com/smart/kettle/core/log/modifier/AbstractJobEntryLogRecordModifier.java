package com.smart.kettle.core.log.modifier;

import com.smart.kettle.core.log.table.EnhancedJobEntryLogTable;
import org.pentaho.di.core.logging.BaseLogTable;

/**
 * @author ShiZhongMing
 * 2021/7/22 21:44
 * @since 1.0
 */
public abstract class AbstractJobEntryLogRecordModifier implements LogRecordModifier {

    @Override
    public Class<? extends BaseLogTable> support() {
        return EnhancedJobEntryLogTable.class;
    }
}
