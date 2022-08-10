package org.pentaho.di.core.logging;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.gui.JobTracker;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaBase;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobEntryResult;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.trans.HasDatabasesInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author pentaho
 * @author shizhongming
 */
public class JobEntryLogTable extends BaseLogTable implements Cloneable, LogTableInterface {
    private static final Class<?> PKG = JobEntryLogTable.class;

    protected JobEntryLogTable(VariableSpace space, HasDatabasesInterface databasesInterface) {
        super(space, databasesInterface, null, null, null);
    }
    @Override
    public Object clone() {
        try {
            JobEntryLogTable table = (JobEntryLogTable)super.clone();
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
        return "      " + XMLHandler.openTag("jobentry-log-table") + Const.CR +
                "        " + XMLHandler.addTagValue("connection", this.connectionName) +
                "        " + XMLHandler.addTagValue("schema", this.schemaName) +
                "        " + XMLHandler.addTagValue("table", this.tableName) +
                "        " + XMLHandler.addTagValue("timeout_days", this.timeoutInDays) +
                super.getFieldsXML() +
                "      " + XMLHandler.closeTag("jobentry-log-table") + Const.CR;
    }
    @Override
    public void replaceMeta(LogTableCoreInterface logTableInterface) {
        if (logTableInterface instanceof JobEntryLogTable logTable) {
            super.replaceMeta(logTable);
        }
    }
    @Override
    public void loadXML(Node jobnode, List<DatabaseMeta> databases, List<StepMeta> steps) {
        Node node = XMLHandler.getSubNode(jobnode, "jobentry-log-table");
        if (node != null) {
            this.connectionName = XMLHandler.getTagValue(node, "connection");
            this.schemaName = XMLHandler.getTagValue(node, "schema");
            this.tableName = XMLHandler.getTagValue(node, "table");
            this.timeoutInDays = XMLHandler.getTagValue(node, "timeout_days");
            super.loadFieldsXML(node);
        }
    }

    public static JobEntryLogTable getDefault(VariableSpace space, HasDatabasesInterface databasesInterface) {
        JobEntryLogTable table = new JobEntryLogTable(space, databasesInterface);
        table.fields.add(new LogTableField(ID.ID_BATCH.id, true, false, "ID_BATCH", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.IdBatch"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.IdBatch"), 5, 8));
        table.fields.add(new LogTableField(ID.CHANNEL_ID.id, true, false, "CHANNEL_ID", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.ChannelId"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.ChannelId"), 2, 255));
        table.fields.add(new LogTableField(ID.LOG_DATE.id, true, false, "LOG_DATE", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.LogDate"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.LogDate"), 3, -1));
        table.fields.add(new LogTableField(ID.JOBNAME.id, true, false, "TRANSNAME", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.JobName"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.JobName"), 2, 255));
        table.fields.add(new LogTableField(ID.JOBENTRYNAME.id, true, false, "STEPNAME", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.JobEntryName"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.JobEntryName"), 2, 255));
        table.fields.add(new LogTableField(ID.LINES_READ.id, true, false, "LINES_READ", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.LinesRead"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.LinesRead"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_WRITTEN.id, true, false, "LINES_WRITTEN", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.LinesWritten"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.LinesWritten"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_UPDATED.id, true, false, "LINES_UPDATED", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.LinesUpdated"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.LinesUpdated"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_INPUT.id, true, false, "LINES_INPUT", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.LinesInput"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.LinesInput"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_OUTPUT.id, true, false, "LINES_OUTPUT", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.LinesOutput"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.LinesOutput"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_REJECTED.id, true, false, "LINES_REJECTED", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.LinesRejected"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.LinesRejected"), 5, 18));
        table.fields.add(new LogTableField(ID.ERRORS.id, true, false, "ERRORS", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.Errors"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.Errors"), 5, 18));
        table.fields.add(new LogTableField(ID.RESULT.id, true, false, "RESULT", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.Result"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.Result"), 2, 5));
        table.fields.add(new LogTableField(ID.NR_RESULT_ROWS.id, true, false, "NR_RESULT_ROWS", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.NrResultRows"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.NrResultRows"), 5, 18));
        table.fields.add(new LogTableField(ID.NR_RESULT_FILES.id, true, false, "NR_RESULT_FILES", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.NrResultFiles"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.NrResultFiles"), 5, 18));
        table.fields.add(new LogTableField(ID.LOG_FIELD.id, false, false, "LOG_FIELD", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.LogField"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.LogField"), 2, 9999999));
        table.fields.add(new LogTableField(ID.COPY_NR.id, false, false, "COPY_NR", BaseMessages.getString(PKG, "JobEntryLogTable.FieldName.CopyNr"), BaseMessages.getString(PKG, "JobEntryLogTable.FieldDescription.CopyNr"), 5, 8));
        table.findField(ID.JOBNAME.id).setNameField(true);
        table.findField(ID.LOG_DATE.id).setLogDateField(true);
        table.findField(ID.ID_BATCH.id).setKey(true);
        table.findField(ID.CHANNEL_ID.id).setVisible(false);
        table.findField(ID.LOG_FIELD.id).setLogField(true);
        table.findField(ID.ERRORS.id).setErrorsField(true);
        return table;
    }
    @Override
    public RowMetaAndData getLogRecord(LogStatus status, Object subject, Object parent) {
        if (subject != null && !(subject instanceof JobEntryCopy)) {
            return null;
        } else {
            JobEntryCopy jobEntryCopy = (JobEntryCopy)subject;
            Job parentJob = (Job)parent;
            RowMetaAndData row = new RowMetaAndData();
            Iterator<LogTableField> var7 = this.fields.iterator();

            while(true) {
                LogTableField field;
                do {
                    if (!var7.hasNext()) {
                        return row;
                    }

                    field = var7.next();
                } while(!field.isEnabled());

                Object value = null;
                if (subject != null) {
                    JobEntryInterface jobEntry = jobEntryCopy.getEntry();
                    JobTracker jobTracker = parentJob.getJobTracker();
                    JobTracker entryTracker = jobTracker.findJobTracker(jobEntryCopy);
                    JobEntryResult jobEntryResult = null;
                    if (entryTracker != null) {
                        jobEntryResult = entryTracker.getJobEntryResult();
                    }

                    Result result = null;
                    if (jobEntryResult != null) {
                        result = jobEntryResult.getResult();
                    }

                    switch(ID.valueOf(field.getId())) {
                        case ID_BATCH:
                            value = parentJob.getBatchId();
                            break;
                        case CHANNEL_ID:
                            value = jobEntry.getLogChannel().getLogChannelId();
                            break;
                        case LOG_DATE:
                            value = new Date();
                            break;
                        case JOBNAME:
                            value = parentJob.getJobname();
                            break;
                        case JOBENTRYNAME:
                            value = jobEntry.getName();
                            break;
                        case LINES_READ:
                            value = result != null ? result.getNrLinesRead() : 0L;
                            break;
                        case LINES_WRITTEN:
                            value = result != null ? result.getNrLinesWritten() : 0L;
                            break;
                        case LINES_UPDATED:
                            value = result != null ? result.getNrLinesUpdated() : 0L;
                            break;
                        case LINES_INPUT:
                            value = result != null ? result.getNrLinesInput() : 0L;
                            break;
                        case LINES_OUTPUT:
                            value = result != null ? result.getNrLinesOutput() : 0L;
                            break;
                        case LINES_REJECTED:
                            value = result != null ? result.getNrLinesRejected() : 0L;
                            break;
                        case ERRORS:
                            value = result != null ? result.getNrErrors() : 0L;
                            break;
                        case RESULT:
                            value = result != null && result.getResult();
                            break;
                        case NR_RESULT_FILES:
                            value = result != null && result.getResultFiles() != null ? (long) result.getResultFiles().size() : 0L;
                            break;
                        case NR_RESULT_ROWS:
                            value = result != null && result.getRows() != null ? (long) result.getRows().size() : 0L;
                            break;
                        case LOG_FIELD:
                            if (result != null) {
                                value = result.getLogText();
                            }
                            break;
                        case COPY_NR:
                            value = (long) jobEntryCopy.getNr();
                            break;
                        default:
                    }
                }

                row.addValue(field.getFieldName(), field.getDataType(), value);
                row.getRowMeta().getValueMeta(row.size() - 1).setLength(field.getLength());
            }
        }
    }
    @Override
    public String getLogTableCode() {
        return "JOB_ENTRY";
    }
    @Override
    public String getLogTableType() {
        return BaseMessages.getString(PKG, "JobEntryLogTable.Type.Description");
    }
    @Override
    public String getConnectionNameVariable() {
        return "KETTLE_JOBENTRY_LOG_DB";
    }
    @Override
    public String getSchemaNameVariable() {
        return "KETTLE_JOBENTRY_LOG_SCHEMA";
    }
    @Override
    public String getTableNameVariable() {
        return "KETTLE_JOBENTRY_LOG_TABLE";
    }
    @Override
    public List<RowMetaInterface> getRecommendedIndexes() {
        List<RowMetaInterface> indexes = new ArrayList<>();
        LogTableField keyField = this.getKeyField();
        if (keyField.isEnabled()) {
            RowMetaInterface batchIndex = new RowMeta();
            ValueMetaInterface keyMeta = new ValueMetaBase(keyField.getFieldName(), keyField.getDataType());
            keyMeta.setLength(keyField.getLength());
            batchIndex.addValueMeta(keyMeta);
            indexes.add(batchIndex);
        }

        return indexes;
    }

    /**
     * ID
     */
    public enum ID {
        /**
         * ID信息
         */
        ID_BATCH("ID_BATCH"),
        CHANNEL_ID("CHANNEL_ID"),
        LOG_DATE("LOG_DATE"),
        JOBNAME("JOBNAME"),
        JOBENTRYNAME("JOBENTRYNAME"),
        LINES_READ("LINES_READ"),
        LINES_WRITTEN("LINES_WRITTEN"),
        LINES_UPDATED("LINES_UPDATED"),
        LINES_INPUT("LINES_INPUT"),
        LINES_OUTPUT("LINES_OUTPUT"),
        LINES_REJECTED("LINES_REJECTED"),
        ERRORS("ERRORS"),
        RESULT("RESULT"),
        NR_RESULT_ROWS("NR_RESULT_ROWS"),
        NR_RESULT_FILES("NR_RESULT_FILES"),
        LOG_FIELD("LOG_FIELD"),
        COPY_NR("COPY_NR");

        private final String id;

        ID(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return this.id;
        }
    }
}
