package com.smart.kettle.core.log.modifier;

import com.smart.kettle.core.log.table.EnhancedChannelLogTable;
import org.pentaho.di.core.logging.BaseLogTable;

/**
 * @author ShiZhongMing
 * 2021/7/22 21:33
 * @since 1.0
 */
public abstract class AbstractChannelLogRecordModifier implements LogRecordModifier {

    @Override
    public Class<? extends BaseLogTable> support() {
        return EnhancedChannelLogTable.class;
    }
}
