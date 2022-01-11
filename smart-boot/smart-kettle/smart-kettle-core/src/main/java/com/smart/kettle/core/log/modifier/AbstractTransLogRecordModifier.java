package com.smart.kettle.core.log.modifier;

import com.smart.kettle.core.log.table.EnhancedTransLogTable;
import org.pentaho.di.core.logging.BaseLogTable;

/**
 * @author ShiZhongMing
 * 2021/7/22 20:24
 * @since 1.0
 */
public abstract class AbstractTransLogRecordModifier implements LogRecordModifier {

    @Override
    public Class<? extends BaseLogTable> support() {
        return EnhancedTransLogTable.class;
    }
}
