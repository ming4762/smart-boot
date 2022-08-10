package org.pentaho.di.core.logging;

import lombok.EqualsAndHashCode;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.RepositoryAttributeInterface;
import org.pentaho.di.trans.HasDatabasesInterface;
import org.pentaho.di.trans.performance.StepPerformanceSnapShot;
import org.pentaho.di.trans.step.StepMeta;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;


/**
 * @author pentaho
 * @author shizhongming
 */
@EqualsAndHashCode(callSuper = false)
public class PerformanceLogTable extends BaseLogTable implements Cloneable, LogTableInterface {
    private static Class<?> PKG = PerformanceLogTable.class;
    public static final String XML_TAG = "perf-log-table";
    private String logInterval;

    protected PerformanceLogTable(VariableSpace space, HasDatabasesInterface databasesInterface) {
        super(space, databasesInterface, null, null, null);
    }
    @Override
    public Object clone() {
        try {
            PerformanceLogTable table = (PerformanceLogTable)super.clone();
            table.fields = new ArrayList<>();

            for (LogTableField field : this.fields) {
                table.fields.add((LogTableField) field.clone());
            }

            return table;
        } catch (CloneNotSupportedException var4) {
            return null;
        }
    }
    @Override
    public String getXML() {
        return "      " + XMLHandler.openTag("perf-log-table") + Const.CR +
                "        " + XMLHandler.addTagValue("connection", this.connectionName) +
                "        " + XMLHandler.addTagValue("schema", this.schemaName) +
                "        " + XMLHandler.addTagValue("table", this.tableName) +
                "        " + XMLHandler.addTagValue("interval", this.logInterval) +
                "        " + XMLHandler.addTagValue("timeout_days", this.timeoutInDays) +
                super.getFieldsXML() +
                "      " + XMLHandler.closeTag("perf-log-table") + Const.CR;
    }
    @Override
    public void loadXML(Node node, List<DatabaseMeta> databases, List<StepMeta> steps) {
        this.connectionName = XMLHandler.getTagValue(node, "connection");
        this.schemaName = XMLHandler.getTagValue(node, "schema");
        this.tableName = XMLHandler.getTagValue(node, "table");
        this.logInterval = XMLHandler.getTagValue(node, "interval");
        this.timeoutInDays = XMLHandler.getTagValue(node, "timeout_days");
        super.loadFieldsXML(node);
    }
    @Override
    public void saveToRepository(RepositoryAttributeInterface attributeInterface) throws KettleException {
        super.saveToRepository(attributeInterface);
        attributeInterface.setAttribute(this.getLogTableCode() + PROP_LOG_TABLE_INTERVAL, this.logInterval);
    }
    @Override
    public void loadFromRepository(RepositoryAttributeInterface attributeInterface) throws KettleException {
        super.loadFromRepository(attributeInterface);
        this.logInterval = attributeInterface.getAttributeString(this.getLogTableCode() + PROP_LOG_TABLE_INTERVAL);
    }
    @Override
    public void replaceMeta(LogTableCoreInterface logTableInterface) {
        if (logTableInterface instanceof PerformanceLogTable logTable) {
            super.replaceMeta(logTable);
        }
    }

    public static PerformanceLogTable getDefault(VariableSpace space, HasDatabasesInterface databasesInterface) {
        PerformanceLogTable table = new PerformanceLogTable(space, databasesInterface);
        table.fields.add(new LogTableField(ID.ID_BATCH.id, true, false, "ID_BATCH", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.BatchID"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.BatchID"), 5, 8));
        table.fields.add(new LogTableField(ID.SEQ_NR.id, true, false, "SEQ_NR", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.SeqNr"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.SeqNr"), 5, 8));
        table.fields.add(new LogTableField(ID.LOGDATE.id, true, false, "LOGDATE", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.LogDate"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.LogDate"), 3, -1));
        table.fields.add(new LogTableField(ID.TRANSNAME.id, true, false, "TRANSNAME", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.TransName"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.TransName"), 2, 255));
        table.fields.add(new LogTableField(ID.STEPNAME.id, true, false, "STEPNAME", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.StepName"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.StepName"), 2, 255));
        table.fields.add(new LogTableField(ID.STEP_COPY.id, true, false, "STEP_COPY", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.StepCopy"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.StepCopy"), 5, 8));
        table.fields.add(new LogTableField(ID.LINES_READ.id, true, false, "LINES_READ", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.LinesRead"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.LinesRead"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_WRITTEN.id, true, false, "LINES_WRITTEN", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.LinesWritten"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.LinesWritten"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_UPDATED.id, true, false, "LINES_UPDATED", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.LinesUpdated"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.LinesUpdated"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_INPUT.id, true, false, "LINES_INPUT", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.LinesInput"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.LinesInput"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_OUTPUT.id, true, false, "LINES_OUTPUT", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.LinesOutput"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.LinesOutput"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_REJECTED.id, true, false, "LINES_REJECTED", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.LinesRejected"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.LinesRejected"), 5, 18));
        table.fields.add(new LogTableField(ID.ERRORS.id, true, false, "ERRORS", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.Errors"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.Errors"), 5, 18));
        table.fields.add(new LogTableField(ID.INPUT_BUFFER_ROWS.id, true, false, "INPUT_BUFFER_ROWS", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.InputBufferRows"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.InputBufferRows"), 5, 18));
        table.fields.add(new LogTableField(ID.OUTPUT_BUFFER_ROWS.id, true, false, "OUTPUT_BUFFER_ROWS", BaseMessages.getString(PKG, "PerformanceLogTable.FieldName.OutputBufferRows"), BaseMessages.getString(PKG, "PerformanceLogTable.FieldDescription.OutputBufferRows"), 5, 18));
        table.findField(ID.ID_BATCH.id).setKey(true);
        table.findField(ID.LOGDATE.id).setLogDateField(true);
        table.findField(ID.TRANSNAME.id).setNameField(true);
        return table;
    }

    public void setLogInterval(String logInterval) {
        this.logInterval = logInterval;
    }

    public String getLogInterval() {
        return this.logInterval;
    }
    @Override
    public RowMetaAndData getLogRecord(LogStatus status, Object subject, Object parent) {
        if (subject != null && !(subject instanceof StepPerformanceSnapShot)) {
            return null;
        } else {
            StepPerformanceSnapShot snapShot = (StepPerformanceSnapShot)subject;
            RowMetaAndData row = new RowMetaAndData();

            for (LogTableField field : this.fields) {
                if (field.isEnabled()) {
                    Object value = null;
                    if (subject != null) {
                        switch (ID.valueOf(field.getId())) {
                            case ID_BATCH -> value = snapShot.getBatchId();
                            case SEQ_NR -> value = (long) snapShot.getSeqNr();
                            case LOGDATE -> value = snapShot.getDate();
                            case TRANSNAME -> value = snapShot.getTransName();
                            case STEPNAME -> value = snapShot.getStepName();
                            case STEP_COPY -> value = (long) snapShot.getStepCopy();
                            case LINES_READ -> value = snapShot.getLinesRead();
                            case LINES_WRITTEN -> value = snapShot.getLinesWritten();
                            case LINES_INPUT -> value = snapShot.getLinesInput();
                            case LINES_OUTPUT -> value = snapShot.getLinesOutput();
                            case LINES_UPDATED -> value = snapShot.getLinesUpdated();
                            case LINES_REJECTED -> value = snapShot.getLinesRejected();
                            case ERRORS -> value = snapShot.getErrors();
                            case INPUT_BUFFER_ROWS -> value = snapShot.getInputBufferSize();
                            case OUTPUT_BUFFER_ROWS -> value = snapShot.getOutputBufferSize();
                            default -> {
                            }
                        }
                    }

                    row.addValue(field.getFieldName(), field.getDataType(), value);
                    row.getRowMeta().getValueMeta(row.size() - 1).setLength(field.getLength());
                }
            }

            return row;
        }
    }
    @Override
    public String getLogTableCode() {
        return "PERFORMANCE";
    }
    @Override
    public String getLogTableType() {
        return BaseMessages.getString(PKG, "PerformanceLogTable.Type.Description");
    }
    @Override
    public String getConnectionNameVariable() {
        return "KETTLE_TRANS_PERFORMANCE_LOG_DB";
    }
    @Override
    public String getSchemaNameVariable() {
        return "KETTLE_TRANS_PERFORMANCE_LOG_SCHEMA";
    }
    @Override
    public String getTableNameVariable() {
        return "KETTLE_TRANS_PERFORMANCE_LOG_TABLE";
    }
    @Override
    public List<RowMetaInterface> getRecommendedIndexes() {
        return new ArrayList<>();
    }
    @Override
    public void setAllGlobalParametersToNull() {
        boolean clearGlobalVariables = Boolean.parseBoolean(System.getProperties().getProperty("KETTLE_GLOBAL_LOG_VARIABLES_CLEAR_ON_EXPORT", "false"));
        if (clearGlobalVariables) {
            super.setAllGlobalParametersToNull();
            this.logInterval = this.isGlobalParameter(this.logInterval) ? null : this.logInterval;
        }

    }

    /**
     * ID
     */
    public enum ID {
        /**
         * ID
         */
        ID_BATCH("ID_BATCH"),
        SEQ_NR("SEQ_NR"),
        LOGDATE("LOGDATE"),
        TRANSNAME("TRANSNAME"),
        STEPNAME("STEPNAME"),
        STEP_COPY("STEP_COPY"),
        LINES_READ("LINES_READ"),
        LINES_WRITTEN("LINES_WRITTEN"),
        LINES_UPDATED("LINES_UPDATED"),
        LINES_INPUT("LINES_INPUT"),
        LINES_OUTPUT("LINES_OUTPUT"),
        LINES_REJECTED("LINES_REJECTED"),
        ERRORS("ERRORS"),
        INPUT_BUFFER_ROWS("INPUT_BUFFER_ROWS"),
        OUTPUT_BUFFER_ROWS("OUTPUT_BUFFER_ROWS");

        private String id;

        private ID(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return this.id;
        }
    }
}
