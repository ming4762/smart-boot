package com.smart.kettle.core.log.modifier;

import com.smart.kettle.core.log.table.EnhancedStepLogTable;
import org.pentaho.di.core.logging.BaseLogTable;

/**
 * @author ShiZhongMing
 * 2021/7/22 20:27
 * @since 1.0
 */
public abstract class AbstractStepLogRecordModifier implements LogRecordModifier {

    @Override
    public Class<? extends BaseLogTable> support() {
        return EnhancedStepLogTable.class;
    }
}
