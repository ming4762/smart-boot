package com.smart.kettle.core.log.table;

import com.smart.kettle.core.log.modifier.LogModifierHandler;
import com.smart.kettle.core.log.modifier.LogModifierHandlerSetter;
import lombok.EqualsAndHashCode;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.logging.LogStatus;
import org.pentaho.di.core.logging.LogTableField;
import org.pentaho.di.core.logging.TransLogTable;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.HasDatabasesInterface;
import org.pentaho.di.trans.step.StepMeta;

import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/7/21 16:32
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = false)
public class EnhancedTransLogTable extends TransLogTable implements LogModifierHandlerSetter {
    private static final Class<?> PKG = TransLogTable.class;

    private LogModifierHandler logModifierHandler;

    public EnhancedTransLogTable(VariableSpace space, HasDatabasesInterface databasesInterface, List<StepMeta> steps) {
        super(space, databasesInterface, steps);
    }

    @Override
    public RowMetaAndData getLogRecord(LogStatus status, Object subject, Object parent) {
        RowMetaAndData rowMetaAndData = super.getLogRecord(status, subject, parent);
        if (this.logModifierHandler != null) {
            return this.logModifierHandler.modifyLogRecord(this.getClass(), rowMetaAndData, status, subject, parent);
        }
        return rowMetaAndData;
    }

    public static EnhancedTransLogTable getDefault(VariableSpace space, HasDatabasesInterface databasesInterface, List<StepMeta> steps) {
        EnhancedTransLogTable table = new EnhancedTransLogTable(space, databasesInterface, steps);
        table.fields.add(new LogTableField(ID.ID_BATCH.toString(), true, false, "ID_BATCH", BaseMessages.getString(PKG, "TransLogTable.FieldName.BatchID"), BaseMessages.getString(PKG, "TransLogTable.FieldDescription.BatchID"), 5, 8));
        table.fields.add(new LogTableField(ID.CHANNEL_ID.toString(), true, false, "CHANNEL_ID", BaseMessages.getString(PKG, "TransLogTable.FieldName.ChannelID"), BaseMessages.getString(PKG, "TransLogTable.FieldDescription.ChannelID"), 2, 255));
        table.fields.add(new LogTableField(ID.TRANSNAME.toString(), true, false, "TRANSNAME", BaseMessages.getString(PKG, "TransLogTable.FieldName.TransName"), BaseMessages.getString(PKG, "TransLogTable.FieldDescription.TransName"), 2, 255));
        table.fields.add(new LogTableField(ID.STATUS.toString(), true, false, "STATUS", BaseMessages.getString(PKG, "TransLogTable.FieldName.Status"), BaseMessages.getString(PKG, "TransLogTable.FieldDescription.Status"), 2, 15));
        table.fields.add(new LogTableField(ID.LINES_READ.toString(), true, true, "LINES_READ", BaseMessages.getString(PKG, "TransLogTable.FieldName.LinesRead"), BaseMessages.getString(PKG, "TransLogTable.FieldDescription.LinesRead"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_WRITTEN.toString(), true, true, "LINES_WRITTEN", BaseMessages.getString(PKG, "TransLogTable.FieldName.LinesWritten"), BaseMessages.getString(PKG, "TransLogTable.FieldDescription.LinesWritten"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_UPDATED.toString(), true, true, "LINES_UPDATED", BaseMessages.getString(PKG, "TransLogTable.FieldName.LinesUpdated"), BaseMessages.getString(PKG, "TransLogTable.FieldDescription.LinesUpdated"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_INPUT.toString(), true, true, "LINES_INPUT", BaseMessages.getString(PKG, "TransLogTable.FieldName.LinesInput"), BaseMessages.getString(PKG, "TransLogTable.FieldDescription.LinesInput"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_OUTPUT.toString(), true, true, "LINES_OUTPUT", BaseMessages.getString(PKG, "TransLogTable.FieldName.LinesOutput"), BaseMessages.getString(PKG, "TransLogTable.FieldDescription.LinesOutput"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_REJECTED.toString(), true, true, "LINES_REJECTED", BaseMessages.getString(PKG, "TransLogTable.FieldName.LinesRejected"), BaseMessages.getString(PKG, "TransLogTable.FieldDescription.LinesRejected"), 5, 18));
        table.fields.add(new LogTableField(ID.ERRORS.toString(), true, false, "ERRORS", BaseMessages.getString(PKG, "TransLogTable.FieldName.Errors"), BaseMessages.getString(PKG, "TransLogTable.FieldDescription.Errors"), 5, 18));
        table.fields.add(new LogTableField(ID.STARTDATE.toString(), true, false, "STARTDATE", BaseMessages.getString(PKG, "TransLogTable.FieldName.StartDateRange"), BaseMessages.getString(PKG, "TransLogTable.FieldDescription.StartDateRange"), 3, -1));
        table.fields.add(new LogTableField(ID.ENDDATE.toString(), true, false, "ENDDATE", BaseMessages.getString(PKG, "TransLogTable.FieldName.EndDateRange"), BaseMessages.getString(PKG, "TransLogTable.FieldDescription.EndDateRange"), 3, -1));
        table.fields.add(new LogTableField(ID.LOGDATE.toString(), true, false, "LOGDATE", BaseMessages.getString(PKG, "TransLogTable.FieldName.LogDate"), BaseMessages.getString(PKG, "TransLogTable.FieldDescription.LogDate"), 3, -1));
        table.fields.add(new LogTableField(ID.DEPDATE.toString(), true, false, "DEPDATE", BaseMessages.getString(PKG, "TransLogTable.FieldName.DepDate"), BaseMessages.getString(PKG, "TransLogTable.FieldDescription.DepDate"), 3, -1));
        table.fields.add(new LogTableField(ID.REPLAYDATE.toString(), true, false, "REPLAYDATE", BaseMessages.getString(PKG, "TransLogTable.FieldName.ReplayDate"), BaseMessages.getString(PKG, "TransLogTable.FieldDescription.ReplayDate"), 3, -1));
        table.fields.add(new LogTableField(ID.LOG_FIELD.toString(), true, false, "LOG_FIELD", BaseMessages.getString(PKG, "TransLogTable.FieldName.LogField"), BaseMessages.getString(PKG, "TransLogTable.FieldDescription.LogField"), 2, 9999999));
        table.fields.add(new LogTableField(ID.EXECUTING_SERVER.toString(), false, false, "EXECUTING_SERVER", BaseMessages.getString(PKG, "TransLogTable.FieldName.ExecutingServer"), BaseMessages.getString(PKG, "TransLogTable.FieldDescription.ExecutingServer"), 2, 255));
        table.fields.add(new LogTableField(ID.EXECUTING_USER.toString(), false, false, "EXECUTING_USER", BaseMessages.getString(PKG, "TransLogTable.FieldName.ExecutingUser"), BaseMessages.getString(PKG, "TransLogTable.FieldDescription.ExecutingUser"), 2, 255));
        table.fields.add(new LogTableField(ID.CLIENT.toString(), false, false, "CLIENT", BaseMessages.getString(PKG, "TransLogTable.FieldName.Client"), BaseMessages.getString(PKG, "TransLogTable.FieldDescription.Client"), 2, 255));
        table.findField(ID.ID_BATCH).setKey(true);
        table.findField(ID.LOGDATE).setLogDateField(true);
        table.findField(ID.LOG_FIELD).setLogField(true);
        table.findField(ID.CHANNEL_ID).setVisible(false);
        table.findField(ID.TRANSNAME).setVisible(false);
        table.findField(ID.STATUS).setStatusField(true);
        table.findField(ID.ERRORS).setErrorsField(true);
        table.findField(ID.TRANSNAME).setNameField(true);
        return table;
    }

    @Override
    public void setLogModifierHandler(LogModifierHandler logModifierHandler) {
        this.logModifierHandler = logModifierHandler;
    }
}
