package com.smart.kettle.core.log.table;

import com.smart.kettle.core.log.modifier.LogModifierHandler;
import com.smart.kettle.core.log.modifier.LogModifierHandlerSetter;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.logging.JobLogTable;
import org.pentaho.di.core.logging.LogStatus;
import org.pentaho.di.core.logging.LogTableField;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.HasDatabasesInterface;

/**
 * @author ShiZhongMing
 * 2021/7/22 16:36
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = false)
public class EnhancedJobLogTable extends JobLogTable implements LogModifierHandlerSetter {
    private static final Class<?> PKG = JobLogTable.class;

    protected LogModifierHandler logModifierHandler;

    protected EnhancedJobLogTable(VariableSpace space, HasDatabasesInterface databasesInterface) {
        super(space, databasesInterface);
    }

    public static EnhancedJobLogTable getDefault(VariableSpace space, HasDatabasesInterface databasesInterface) {
        EnhancedJobLogTable table = new EnhancedJobLogTable(space, databasesInterface);
        table.fields.add(new LogTableField(ID.ID_JOB.toString(), true, false, "ID_JOB", BaseMessages.getString(PKG, "JobLogTable.FieldName.BatchID"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.BatchID"), 5, 8));
        table.fields.add(new LogTableField(ID.CHANNEL_ID.toString(), true, false, "CHANNEL_ID", BaseMessages.getString(PKG, "JobLogTable.FieldName.ChannelID"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.ChannelID"), 2, 255));
        table.fields.add(new LogTableField(ID.JOBNAME.toString(), true, false, "JOBNAME", BaseMessages.getString(PKG, "JobLogTable.FieldName.JobName"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.JobName"), 2, 255));
        table.fields.add(new LogTableField(ID.STATUS.toString(), true, false, "STATUS", BaseMessages.getString(PKG, "JobLogTable.FieldName.Status"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.Status"), 2, 15));
        table.fields.add(new LogTableField(ID.LINES_READ.toString(), true, false, "LINES_READ", BaseMessages.getString(PKG, "JobLogTable.FieldName.LinesRead"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.LinesRead"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_WRITTEN.toString(), true, false, "LINES_WRITTEN", BaseMessages.getString(PKG, "JobLogTable.FieldName.LinesWritten"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.LinesWritten"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_UPDATED.toString(), true, false, "LINES_UPDATED", BaseMessages.getString(PKG, "JobLogTable.FieldName.LinesUpdated"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.LinesUpdated"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_INPUT.toString(), true, false, "LINES_INPUT", BaseMessages.getString(PKG, "JobLogTable.FieldName.LinesInput"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.LinesInput"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_OUTPUT.toString(), true, false, "LINES_OUTPUT", BaseMessages.getString(PKG, "JobLogTable.FieldName.LinesOutput"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.LinesOutput"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_REJECTED.toString(), true, false, "LINES_REJECTED", BaseMessages.getString(PKG, "JobLogTable.FieldName.LinesRejected"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.LinesRejected"), 5, 18));
        table.fields.add(new LogTableField(ID.ERRORS.toString(), true, false, "ERRORS", BaseMessages.getString(PKG, "JobLogTable.FieldName.Errors"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.Errors"), 5, 18));
        table.fields.add(new LogTableField(ID.STARTDATE.toString(), true, false, "STARTDATE", BaseMessages.getString(PKG, "JobLogTable.FieldName.StartDateRange"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.StartDateRange"), 3, -1));
        table.fields.add(new LogTableField(ID.ENDDATE.toString(), true, false, "ENDDATE", BaseMessages.getString(PKG, "JobLogTable.FieldName.EndDateRange"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.EndDateRange"), 3, -1));
        table.fields.add(new LogTableField(ID.LOGDATE.toString(), true, false, "LOGDATE", BaseMessages.getString(PKG, "JobLogTable.FieldName.LogDate"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.LogDate"), 3, -1));
        table.fields.add(new LogTableField(ID.DEPDATE.toString(), true, false, "DEPDATE", BaseMessages.getString(PKG, "JobLogTable.FieldName.DepDate"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.DepDate"), 3, -1));
        table.fields.add(new LogTableField(ID.REPLAYDATE.toString(), true, false, "REPLAYDATE", BaseMessages.getString(PKG, "JobLogTable.FieldName.ReplayDate"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.ReplayDate"), 3, -1));
        table.fields.add(new LogTableField(ID.LOG_FIELD.toString(), true, false, "LOG_FIELD", BaseMessages.getString(PKG, "JobLogTable.FieldName.LogField"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.LogField"), 2, 9999999));
        table.fields.add(new LogTableField(ID.EXECUTING_SERVER.toString(), false, false, "EXECUTING_SERVER", BaseMessages.getString(PKG, "JobLogTable.FieldName.ExecutingServer"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.ExecutingServer"), 2, 255));
        table.fields.add(new LogTableField(ID.EXECUTING_USER.toString(), false, false, "EXECUTING_USER", BaseMessages.getString(PKG, "JobLogTable.FieldName.ExecutingUser"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.ExecutingUser"), 2, 255));
        table.fields.add(new LogTableField(ID.START_JOB_ENTRY.toString(), false, false, "START_JOB_ENTRY", BaseMessages.getString(PKG, "JobLogTable.FieldName.StartingJobEntry"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.StartingJobEntry"), 2, 255));
        table.fields.add(new LogTableField(ID.CLIENT.toString(), false, false, "CLIENT", BaseMessages.getString(PKG, "JobLogTable.FieldName.Client"), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.Client"), 2, 255));
        table.findField(ID.ID_JOB).setKey(true);
        table.findField(ID.LOGDATE).setLogDateField(true);
        table.findField(ID.LOG_FIELD).setLogField(true);
        table.findField(ID.CHANNEL_ID).setVisible(false);
        table.findField(ID.JOBNAME).setVisible(false);
        table.findField(ID.STATUS).setStatusField(true);
        table.findField(ID.ERRORS).setErrorsField(true);
        table.findField(ID.JOBNAME).setNameField(true);
        return table;
    }

    @SneakyThrows
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
