package com.smart.kettle.core.log.table;

import com.smart.kettle.core.log.modifier.LogModifierHandler;
import com.smart.kettle.core.log.modifier.LogModifierHandlerSetter;
import lombok.EqualsAndHashCode;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.logging.JobEntryLogTable;
import org.pentaho.di.core.logging.LogStatus;
import org.pentaho.di.core.logging.LogTableField;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.HasDatabasesInterface;

/**
 * @author ShiZhongMing
 * 2021/7/22 16:42
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = false)
public class EnhancedJobEntryLogTable extends JobEntryLogTable implements LogModifierHandlerSetter {
    private static final Class<?> PKG = JobEntryLogTable.class;
    protected LogModifierHandler logModifierHandler;

    protected EnhancedJobEntryLogTable(VariableSpace space, HasDatabasesInterface databasesInterface) {
        super(space, databasesInterface);
    }

    public static JobEntryLogTable getDefault(VariableSpace space, HasDatabasesInterface databasesInterface) {
        EnhancedJobEntryLogTable table = new EnhancedJobEntryLogTable(space, databasesInterface);
        table.fields.add(new LogTableField(ID.ID_BATCH.toString(), true, false, "ID_BATCH", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.IdBatch"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.IdBatch"), 5, 8));
        table.fields.add(new LogTableField(ID.CHANNEL_ID.toString(), true, false, "CHANNEL_ID", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.ChannelId"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.ChannelId"), 2, 255));
        table.fields.add(new LogTableField(ID.LOG_DATE.toString(), true, false, "LOG_DATE", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.LogDate"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.LogDate"), 3, -1));
        table.fields.add(new LogTableField(ID.JOBNAME.toString(), true, false, "TRANSNAME", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.JobName"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.JobName"), 2, 255));
        table.fields.add(new LogTableField(ID.JOBENTRYNAME.toString(), true, false, "STEPNAME", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.JobEntryName"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.JobEntryName"), 2, 255));
        table.fields.add(new LogTableField(ID.LINES_READ.toString(), true, false, "LINES_READ", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.LinesRead"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.LinesRead"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_WRITTEN.toString(), true, false, "LINES_WRITTEN", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.LinesWritten"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.LinesWritten"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_UPDATED.toString(), true, false, "LINES_UPDATED", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.LinesUpdated"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.LinesUpdated"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_INPUT.toString(), true, false, "LINES_INPUT", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.LinesInput"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.LinesInput"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_OUTPUT.toString(), true, false, "LINES_OUTPUT", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.LinesOutput"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.LinesOutput"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_REJECTED.toString(), true, false, "LINES_REJECTED", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.LinesRejected"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.LinesRejected"), 5, 18));
        table.fields.add(new LogTableField(ID.ERRORS.toString(), true, false, "ERRORS", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.Errors"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.Errors"), 5, 18));
        table.fields.add(new LogTableField(ID.RESULT.toString(), true, false, "RESULT", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.Result"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.Result"), 2, 5));
        table.fields.add(new LogTableField(ID.NR_RESULT_ROWS.toString(), true, false, "NR_RESULT_ROWS", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.NrResultRows"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.NrResultRows"), 5, 18));
        table.fields.add(new LogTableField(ID.NR_RESULT_FILES.toString(), true, false, "NR_RESULT_FILES", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.NrResultFiles"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.NrResultFiles"), 5, 18));
        table.fields.add(new LogTableField(ID.LOG_FIELD.toString(), false, false, "LOG_FIELD", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.LogField"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.LogField"), 2, 9999999));
        table.fields.add(new LogTableField(ID.COPY_NR.toString(), false, false, "COPY_NR", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.CopyNr"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.CopyNr"), 5, 8));
        table.findField(ID.JOBNAME.toString()).setNameField(true);
        table.findField(ID.LOG_DATE.toString()).setLogDateField(true);
        table.findField(ID.ID_BATCH.toString()).setKey(true);
        table.findField(ID.CHANNEL_ID.toString()).setVisible(false);
        table.findField(ID.LOG_FIELD.toString()).setLogField(true);
        table.findField(ID.ERRORS.toString()).setErrorsField(true);
        return table;
    }

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
