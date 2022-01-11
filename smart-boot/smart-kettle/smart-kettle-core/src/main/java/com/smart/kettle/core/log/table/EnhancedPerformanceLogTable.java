package com.smart.kettle.core.log.table;

import com.smart.kettle.core.log.modifier.LogModifierHandler;
import com.smart.kettle.core.log.modifier.LogModifierHandlerSetter;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.logging.LogStatus;
import org.pentaho.di.core.logging.LogTableField;
import org.pentaho.di.core.logging.PerformanceLogTable;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.HasDatabasesInterface;

/**
 * @author ShiZhongMing
 * 2021/7/22 16:26
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = false)
public class EnhancedPerformanceLogTable extends PerformanceLogTable implements LogModifierHandlerSetter {

    private static final Class<?> PKG = PerformanceLogTable.class;

    private LogModifierHandler logModifierHandler;

    protected EnhancedPerformanceLogTable(VariableSpace space, HasDatabasesInterface databasesInterface) {
        super(space, databasesInterface);
    }

    public static EnhancedPerformanceLogTable getDefault(VariableSpace space, HasDatabasesInterface databasesInterface) {
        EnhancedPerformanceLogTable table = new EnhancedPerformanceLogTable(space, databasesInterface);
        table.fields.add(new LogTableField(ID.ID_BATCH.toString(), true, false, "ID_BATCH", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.BatchID"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.BatchID"), 5, 8));
        table.fields.add(new LogTableField(ID.SEQ_NR.toString(), true, false, "SEQ_NR", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.SeqNr"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.SeqNr"), 5, 8));
        table.fields.add(new LogTableField(ID.LOGDATE.toString(), true, false, "LOGDATE", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.LogDate"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.LogDate"), 3, -1));
        table.fields.add(new LogTableField(ID.TRANSNAME.toString(), true, false, "TRANSNAME", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.TransName"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.TransName"), 2, 255));
        table.fields.add(new LogTableField(ID.STEPNAME.toString(), true, false, "STEPNAME", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.StepName"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.StepName"), 2, 255));
        table.fields.add(new LogTableField(ID.STEP_COPY.toString(), true, false, "STEP_COPY", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.StepCopy"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.StepCopy"), 5, 8));
        table.fields.add(new LogTableField(ID.LINES_READ.toString(), true, false, "LINES_READ", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.LinesRead"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.LinesRead"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_WRITTEN.toString(), true, false, "LINES_WRITTEN", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.LinesWritten"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.LinesWritten"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_UPDATED.toString(), true, false, "LINES_UPDATED", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.LinesUpdated"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.LinesUpdated"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_INPUT.toString(), true, false, "LINES_INPUT", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.LinesInput"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.LinesInput"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_OUTPUT.toString(), true, false, "LINES_OUTPUT", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.LinesOutput"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.LinesOutput"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_REJECTED.toString(), true, false, "LINES_REJECTED", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.LinesRejected"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.LinesRejected"), 5, 18));
        table.fields.add(new LogTableField(ID.ERRORS.toString(), true, false, "ERRORS", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.Errors"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.Errors"), 5, 18));
        table.fields.add(new LogTableField(ID.INPUT_BUFFER_ROWS.toString(), true, false, "INPUT_BUFFER_ROWS", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.InputBufferRows"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.InputBufferRows"), 5, 18));
        table.fields.add(new LogTableField(ID.OUTPUT_BUFFER_ROWS.toString(), true, false, "OUTPUT_BUFFER_ROWS", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.OutputBufferRows"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.OutputBufferRows"), 5, 18));
        table.findField(ID.ID_BATCH.toString()).setKey(true);
        table.findField(ID.LOGDATE.toString()).setLogDateField(true);
        table.findField(ID.TRANSNAME.toString()).setNameField(true);
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
