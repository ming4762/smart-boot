package com.smart.kettle.core.log.table;

import com.smart.kettle.core.log.modifier.LogModifierHandler;
import com.smart.kettle.core.log.modifier.LogModifierHandlerSetter;
import lombok.EqualsAndHashCode;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.logging.LogStatus;
import org.pentaho.di.core.logging.LogTableField;
import org.pentaho.di.core.logging.MetricsLogTable;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.HasDatabasesInterface;

/**
 * @author ShiZhongMing
 * 2021/7/22 16:03
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
public class EnhancedMetricsLogTable extends MetricsLogTable implements LogModifierHandlerSetter {

    private static final Class<?> PKG = MetricsLogTable.class;

    private LogModifierHandler logModifierHandler;

    protected EnhancedMetricsLogTable(VariableSpace space, HasDatabasesInterface databasesInterface) {
        super(space, databasesInterface);
    }

    public static EnhancedMetricsLogTable getDefault(VariableSpace space, HasDatabasesInterface databasesInterface) {
        EnhancedMetricsLogTable table = new EnhancedMetricsLogTable(space, databasesInterface);
        table.fields.add(new LogTableField(ID.ID_BATCH.toString(), true, false, "ID_BATCH", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.IdBatch"), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.IdBatch"), 5, 8));
        table.fields.add(new LogTableField(ID.CHANNEL_ID.toString(), true, false, "CHANNEL_ID", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.ChannelId"), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.ChannelId"), 2, 255));
        table.fields.add(new LogTableField(ID.LOG_DATE.toString(), true, false, "LOG_DATE", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.LogDate"), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.LogDate"), 3, -1));
        table.fields.add(new LogTableField(ID.METRICS_DATE.toString(), true, false, "METRICS_DATE", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.MetricsDate"), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.MetricsDate"), 3, -1));
        table.fields.add(new LogTableField(ID.METRICS_CODE.toString(), true, false, "METRICS_CODE", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.MetricsDescription"), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.MetricsCode"), 2, 255));
        table.fields.add(new LogTableField(ID.METRICS_DESCRIPTION.toString(), true, false, "METRICS_DESCRIPTION", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.MetricsDescription"), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.MetricsDescription"), 2, 255));
        table.fields.add(new LogTableField(ID.METRICS_SUBJECT.toString(), true, false, "METRICS_SUBJECT", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.MetricsSubject"), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.MetricsSubject"), 2, 255));
        table.fields.add(new LogTableField(ID.METRICS_TYPE.toString(), true, false, "METRICS_TYPE", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.MetricsType"), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.MetricsType"), 2, 255));
        table.fields.add(new LogTableField(ID.METRICS_VALUE.toString(), true, false, "METRICS_VALUE", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.MetricsValue"), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.MetricsValue"), 5, 12));
        table.findField(ID.LOG_DATE.toString()).setLogDateField(true);
        table.findField(ID.ID_BATCH.toString()).setKey(true);
        return table;
    }

    @Override
    public RowMetaAndData getLogRecord(LogStatus status, Object subject, Object parent) {
        RowMetaAndData rowMetaAndData = super.getLogRecord(status, subject, parent);
        if (this.logModifierHandler != null) {
            this.logModifierHandler.modifyLogRecord(this.getClass(), rowMetaAndData, status, subject, parent);
        }
        return rowMetaAndData;
    }

    @Override
    public void setLogModifierHandler(LogModifierHandler logModifierHandler) {
        this.logModifierHandler = logModifierHandler;
    }
}
