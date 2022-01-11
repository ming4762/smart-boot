package com.smart.kettle.core.log.table;

import com.smart.kettle.core.log.modifier.LogModifierHandler;
import com.smart.kettle.core.log.modifier.LogModifierHandlerSetter;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.logging.LogStatus;
import org.pentaho.di.core.logging.LogTableField;
import org.pentaho.di.core.logging.StepLogTable;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.HasDatabasesInterface;

/**
 * @author ShiZhongMing
 * 2021/7/22 14:42
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
public class EnhancedStepLogTable extends StepLogTable implements LogModifierHandlerSetter {

    private static final Class<?> PKG = EnhancedStepLogTable.class;

    private LogModifierHandler logModifierHandler;

    private EnhancedStepLogTable(VariableSpace space, HasDatabasesInterface databasesInterface) {
        super(space, databasesInterface);
    }

    public static EnhancedStepLogTable getDefault(VariableSpace space, HasDatabasesInterface databasesInterface) {
        EnhancedStepLogTable table = new EnhancedStepLogTable(space, databasesInterface);
        table.fields.add(new LogTableField(ID.ID_BATCH.toString(), true, false, "ID_BATCH", BaseMessages.getString(PKG, "StepLogTable.FieldName.IdBatch"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.IdBatch"), 5, 8));
        table.fields.add(new LogTableField(ID.CHANNEL_ID.toString(), true, false, "CHANNEL_ID", BaseMessages.getString(PKG, "StepLogTable.FieldName.ChannelId"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.ChannelId"), 2, 255));
        table.fields.add(new LogTableField(ID.LOG_DATE.toString(), true, false, "LOG_DATE", BaseMessages.getString(PKG, "StepLogTable.FieldName.LogDate"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.LogDate"), 3, -1));
        table.fields.add(new LogTableField(ID.TRANSNAME.toString(), true, false, "TRANSNAME", BaseMessages.getString(PKG, "StepLogTable.FieldName.TransName"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.TransName"), 2, 255));
        table.fields.add(new LogTableField(ID.STEPNAME.toString(), true, false, "STEPNAME", BaseMessages.getString(PKG, "StepLogTable.FieldName.StepName"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.StepName"), 2, 255));
        table.fields.add(new LogTableField(ID.STEP_COPY.toString(), true, false, "STEP_COPY", BaseMessages.getString(PKG, "StepLogTable.FieldName.StepCopy"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.StepCopy"), 5, 3));
        table.fields.add(new LogTableField(ID.LINES_READ.toString(), true, false, "LINES_READ", BaseMessages.getString(PKG, "StepLogTable.FieldName.LinesRead"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.LinesRead"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_WRITTEN.toString(), true, false, "LINES_WRITTEN", BaseMessages.getString(PKG, "StepLogTable.FieldName.LinesWritten"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.LinesWritten"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_UPDATED.toString(), true, false, "LINES_UPDATED", BaseMessages.getString(PKG, "StepLogTable.FieldName.LinesUpdated"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.LinesUpdated"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_INPUT.toString(), true, false, "LINES_INPUT", BaseMessages.getString(PKG, "StepLogTable.FieldName.LinesInput"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.LinesInput"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_OUTPUT.toString(), true, false, "LINES_OUTPUT", BaseMessages.getString(PKG, "StepLogTable.FieldName.LinesOutput"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.LinesOutput"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_REJECTED.toString(), true, false, "LINES_REJECTED", BaseMessages.getString(PKG, "StepLogTable.FieldName.LinesRejected"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.LinesRejected"), 5, 18));
        table.fields.add(new LogTableField(ID.ERRORS.toString(), true, false, "ERRORS", BaseMessages.getString(PKG, "StepLogTable.FieldName.Errors"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.Errors"), 5, 18));
        table.fields.add(new LogTableField(ID.LOG_FIELD.toString(), false, false, "LOG_FIELD", BaseMessages.getString(PKG, "StepLogTable.FieldName.LogField"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.LogField"), 2, 9999999));
        table.findField(ID.TRANSNAME.toString()).setNameField(true);
        table.findField(ID.LOG_DATE.toString()).setLogDateField(true);
        table.findField(ID.ID_BATCH.toString()).setKey(true);
        table.findField(ID.CHANNEL_ID.toString()).setVisible(false);
        table.findField(ID.LOG_FIELD.toString()).setLogField(true);
        table.findField(ID.ERRORS.toString()).setErrorsField(true);
        return table;
    }

    @SneakyThrows
    @Override
    public RowMetaAndData getLogRecord(LogStatus status, Object subject, Object parent) {
        RowMetaAndData rowMetaAndData = super.getLogRecord(status, subject, parent);
        if (this.logModifierHandler != null) {
            return this.logModifierHandler.modifyLogRecord(this.getClass(), rowMetaAndData, status, subject, parent);
        }
        return rowMetaAndData;
    }

    @Override
    public void setLogModifierHandler(LogModifierHandler logModifierHandler) {
        this.logModifierHandler = logModifierHandler;
    }
}
