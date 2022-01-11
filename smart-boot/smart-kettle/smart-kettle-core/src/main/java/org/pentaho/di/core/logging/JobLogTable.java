package org.pentaho.di.core.logging;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.KettleClientEnvironment;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaBase;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.job.Job;
import org.pentaho.di.repository.RepositoryAttributeInterface;
import org.pentaho.di.trans.HasDatabasesInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author pentaho
 * @author shizhongming
 */
public class JobLogTable extends BaseLogTable implements Cloneable, LogTableInterface {
    private static Class<?> PKG = JobLogTable.class;
    public static final String XML_TAG = "job-log-table";
    private String logInterval;
    private String logSizeLimit;

    protected JobLogTable(VariableSpace space, HasDatabasesInterface databasesInterface) {
        super(space, databasesInterface, (String)null, (String)null, (String)null);
    }
    @Override
    public Object clone() {
        try {
            JobLogTable table = (JobLogTable)super.clone();
            table.fields = new ArrayList();
            Iterator var2 = this.fields.iterator();

            while(var2.hasNext()) {
                LogTableField field = (LogTableField)var2.next();
                table.fields.add((LogTableField)field.clone());
            }

            return table;
        } catch (CloneNotSupportedException var4) {
            return null;
        }
    }
    @Override
    public String getXML() {
        StringBuilder retval = new StringBuilder();
        retval.append("      ").append(XMLHandler.openTag("job-log-table")).append(Const.CR);
        retval.append("        ").append(XMLHandler.addTagValue("connection", this.connectionName));
        retval.append("        ").append(XMLHandler.addTagValue("schema", this.schemaName));
        retval.append("        ").append(XMLHandler.addTagValue("table", this.tableName));
        retval.append("        ").append(XMLHandler.addTagValue("size_limit_lines", this.logSizeLimit));
        retval.append("        ").append(XMLHandler.addTagValue("interval", this.logInterval));
        retval.append("        ").append(XMLHandler.addTagValue("timeout_days", this.timeoutInDays));
        retval.append(super.getFieldsXML());
        retval.append("      ").append(XMLHandler.closeTag("job-log-table")).append(Const.CR);
        return retval.toString();
    }
    @Override
    public void loadXML(Node node, List<DatabaseMeta> databases, List<StepMeta> steps) {
        this.connectionName = XMLHandler.getTagValue(node, "connection");
        this.schemaName = XMLHandler.getTagValue(node, "schema");
        this.tableName = XMLHandler.getTagValue(node, "table");
        this.logSizeLimit = XMLHandler.getTagValue(node, "size_limit_lines");
        this.logInterval = XMLHandler.getTagValue(node, "interval");
        this.timeoutInDays = XMLHandler.getTagValue(node, "timeout_days");
        super.loadFieldsXML(node);
    }
    @Override
    public void saveToRepository(RepositoryAttributeInterface attributeInterface) throws KettleException {
        super.saveToRepository(attributeInterface);
        attributeInterface.setAttribute(this.getLogTableCode() + PROP_LOG_TABLE_INTERVAL, this.logInterval);
        attributeInterface.setAttribute(this.getLogTableCode() + PROP_LOG_TABLE_SIZE_LIMIT, this.logSizeLimit);
    }
    @Override
    public void loadFromRepository(RepositoryAttributeInterface attributeInterface) throws KettleException {
        super.loadFromRepository(attributeInterface);
        this.logInterval = attributeInterface.getAttributeString(this.getLogTableCode() + PROP_LOG_TABLE_INTERVAL);
        this.logSizeLimit = attributeInterface.getAttributeString(this.getLogTableCode() + PROP_LOG_TABLE_SIZE_LIMIT);
    }
    @Override
    public void replaceMeta(LogTableCoreInterface logTableInterface) {
        if (logTableInterface instanceof JobLogTable) {
            JobLogTable logTable = (JobLogTable)logTableInterface;
            super.replaceMeta(logTable);
            this.logInterval = logTable.logInterval;
            this.logSizeLimit = logTable.logSizeLimit;
        }
    }

    public static JobLogTable getDefault(VariableSpace space, HasDatabasesInterface databasesInterface) {
        JobLogTable table = new JobLogTable(space, databasesInterface);
        table.fields.add(new LogTableField(ID.ID_JOB.id, true, false, "ID_JOB", BaseMessages.getString(PKG, "JobLogTable.FieldName.BatchID", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.BatchID", new String[0]), 5, 8));
        table.fields.add(new LogTableField(ID.CHANNEL_ID.id, true, false, "CHANNEL_ID", BaseMessages.getString(PKG, "JobLogTable.FieldName.ChannelID", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.ChannelID", new String[0]), 2, 255));
        table.fields.add(new LogTableField(ID.JOBNAME.id, true, false, "JOBNAME", BaseMessages.getString(PKG, "JobLogTable.FieldName.JobName", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.JobName", new String[0]), 2, 255));
        table.fields.add(new LogTableField(ID.STATUS.id, true, false, "STATUS", BaseMessages.getString(PKG, "JobLogTable.FieldName.Status", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.Status", new String[0]), 2, 15));
        table.fields.add(new LogTableField(ID.LINES_READ.id, true, false, "LINES_READ", BaseMessages.getString(PKG, "JobLogTable.FieldName.LinesRead", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.LinesRead", new String[0]), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_WRITTEN.id, true, false, "LINES_WRITTEN", BaseMessages.getString(PKG, "JobLogTable.FieldName.LinesWritten", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.LinesWritten", new String[0]), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_UPDATED.id, true, false, "LINES_UPDATED", BaseMessages.getString(PKG, "JobLogTable.FieldName.LinesUpdated", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.LinesUpdated", new String[0]), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_INPUT.id, true, false, "LINES_INPUT", BaseMessages.getString(PKG, "JobLogTable.FieldName.LinesInput", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.LinesInput", new String[0]), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_OUTPUT.id, true, false, "LINES_OUTPUT", BaseMessages.getString(PKG, "JobLogTable.FieldName.LinesOutput", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.LinesOutput", new String[0]), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_REJECTED.id, true, false, "LINES_REJECTED", BaseMessages.getString(PKG, "JobLogTable.FieldName.LinesRejected", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.LinesRejected", new String[0]), 5, 18));
        table.fields.add(new LogTableField(ID.ERRORS.id, true, false, "ERRORS", BaseMessages.getString(PKG, "JobLogTable.FieldName.Errors", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.Errors", new String[0]), 5, 18));
        table.fields.add(new LogTableField(ID.STARTDATE.id, true, false, "STARTDATE", BaseMessages.getString(PKG, "JobLogTable.FieldName.StartDateRange", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.StartDateRange", new String[0]), 3, -1));
        table.fields.add(new LogTableField(ID.ENDDATE.id, true, false, "ENDDATE", BaseMessages.getString(PKG, "JobLogTable.FieldName.EndDateRange", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.EndDateRange", new String[0]), 3, -1));
        table.fields.add(new LogTableField(ID.LOGDATE.id, true, false, "LOGDATE", BaseMessages.getString(PKG, "JobLogTable.FieldName.LogDate", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.LogDate", new String[0]), 3, -1));
        table.fields.add(new LogTableField(ID.DEPDATE.id, true, false, "DEPDATE", BaseMessages.getString(PKG, "JobLogTable.FieldName.DepDate", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.DepDate", new String[0]), 3, -1));
        table.fields.add(new LogTableField(ID.REPLAYDATE.id, true, false, "REPLAYDATE", BaseMessages.getString(PKG, "JobLogTable.FieldName.ReplayDate", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.ReplayDate", new String[0]), 3, -1));
        table.fields.add(new LogTableField(ID.LOG_FIELD.id, true, false, "LOG_FIELD", BaseMessages.getString(PKG, "JobLogTable.FieldName.LogField", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.LogField", new String[0]), 2, 9999999));
        table.fields.add(new LogTableField(ID.EXECUTING_SERVER.id, false, false, "EXECUTING_SERVER", BaseMessages.getString(PKG, "JobLogTable.FieldName.ExecutingServer", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.ExecutingServer", new String[0]), 2, 255));
        table.fields.add(new LogTableField(ID.EXECUTING_USER.id, false, false, "EXECUTING_USER", BaseMessages.getString(PKG, "JobLogTable.FieldName.ExecutingUser", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.ExecutingUser", new String[0]), 2, 255));
        table.fields.add(new LogTableField(ID.START_JOB_ENTRY.id, false, false, "START_JOB_ENTRY", BaseMessages.getString(PKG, "JobLogTable.FieldName.StartingJobEntry", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.StartingJobEntry", new String[0]), 2, 255));
        table.fields.add(new LogTableField(ID.CLIENT.id, false, false, "CLIENT", BaseMessages.getString(PKG, "JobLogTable.FieldName.Client", new String[0]), BaseMessages.getString(PKG, "JobLogTable.FieldDescription.Client", new String[0]), 2, 255));
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

    public LogTableField findField(ID id) {
        return super.findField(id.id);
    }

    public Object getSubject(ID id) {
        return super.getSubject(id.id);
    }

    public String getSubjectString(ID id) {
        return super.getSubjectString(id.id);
    }

    public void setBatchIdUsed(boolean use) {
        this.findField(ID.ID_JOB).setEnabled(use);
    }

    public boolean isBatchIdUsed() {
        return this.findField(ID.ID_JOB).isEnabled();
    }

    public void setLogFieldUsed(boolean use) {
        this.findField(ID.LOG_FIELD).setEnabled(use);
    }

    public boolean isLogFieldUsed() {
        return this.findField(ID.LOG_FIELD).isEnabled();
    }

    public String getStepnameRead() {
        return this.getSubjectString(ID.LINES_READ);
    }

    public String getStepnameWritten() {
        return this.getSubjectString(ID.LINES_WRITTEN);
    }

    public String getStepnameInput() {
        return this.getSubjectString(ID.LINES_INPUT);
    }

    public String getStepnameOutput() {
        return this.getSubjectString(ID.LINES_OUTPUT);
    }

    public String getStepnameUpdated() {
        return this.getSubjectString(ID.LINES_UPDATED);
    }

    public String getStepnameRejected() {
        return this.getSubjectString(ID.LINES_REJECTED);
    }

    public void setLogInterval(String logInterval) {
        this.logInterval = logInterval;
    }

    public String getLogInterval() {
        return this.logInterval;
    }

    public String getLogSizeLimit() {
        return this.logSizeLimit;
    }

    public void setLogSizeLimit(String logSizeLimit) {
        this.logSizeLimit = logSizeLimit;
    }
    @Override
    public RowMetaAndData getLogRecord(LogStatus status, Object subject, Object parent) {
        if (subject != null && !(subject instanceof Job)) {
            return null;
        } else {
            Job job = (Job)subject;
            Result result = null;
            if (job != null) {
                result = job.getResult();
            }

            RowMetaAndData row = new RowMetaAndData();
            Iterator var7 = this.fields.iterator();

            while(var7.hasNext()) {
                LogTableField field = (LogTableField)var7.next();
                if (field.isEnabled()) {
                    Object value = null;
                    if (job != null) {
                        switch(ID.valueOf(field.getId())) {
                            case ID_JOB:
                                value = new Long(job.getBatchId());
                                break;
                            case CHANNEL_ID:
                                value = job.getLogChannelId();
                                break;
                            case JOBNAME:
                                value = job.getJobname();
                                break;
                            case STATUS:
                                value = status.getStatus();
                                break;
                            case LINES_READ:
                                value = result == null ? null : new Long(result.getNrLinesRead());
                                break;
                            case LINES_WRITTEN:
                                value = result == null ? null : new Long(result.getNrLinesWritten());
                                break;
                            case LINES_INPUT:
                                value = result == null ? null : new Long(result.getNrLinesInput());
                                break;
                            case LINES_OUTPUT:
                                value = result == null ? null : new Long(result.getNrLinesOutput());
                                break;
                            case LINES_UPDATED:
                                value = result == null ? null : new Long(result.getNrLinesUpdated());
                                break;
                            case LINES_REJECTED:
                                value = result == null ? null : new Long(result.getNrLinesRejected());
                                break;
                            case ERRORS:
                                value = result == null ? null : new Long(result.getNrErrors());
                                break;
                            case STARTDATE:
                                value = job.getStartDate();
                                break;
                            case LOGDATE:
                                value = job.getLogDate();
                                break;
                            case ENDDATE:
                                value = job.getEndDate();
                                break;
                            case DEPDATE:
                                value = job.getDepDate();
                                break;
                            case REPLAYDATE:
                                value = job.getCurrentDate();
                                break;
                            case LOG_FIELD:
                                value = this.getLogBuffer(job, job.getLogChannelId(), status, this.logSizeLimit);
                                break;
                            case EXECUTING_SERVER:
                                value = job.getExecutingServer();
                                break;
                            case EXECUTING_USER:
                                value = job.getExecutingUser();
                                break;
                            case START_JOB_ENTRY:
                                value = job.getStartJobEntryCopy() != null ? job.getStartJobEntryCopy().getName() : null;
                                break;
                            case CLIENT:
                                value = KettleClientEnvironment.getInstance().getClient() != null ? KettleClientEnvironment.getInstance().getClient().toString() : "unknown";
                                break;
                            default:
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
        return "JOB";
    }
    @Override
    public String getLogTableType() {
        return BaseMessages.getString(PKG, "JobLogTable.Type.Description", new String[0]);
    }
    @Override
    public String getConnectionNameVariable() {
        return "KETTLE_JOB_LOG_DB";
    }
    @Override
    public String getSchemaNameVariable() {
        return "KETTLE_JOB_LOG_SCHEMA";
    }
    @Override
    public String getTableNameVariable() {
        return "KETTLE_JOB_LOG_TABLE";
    }
    @Override
    public List<RowMetaInterface> getRecommendedIndexes() {
        List<RowMetaInterface> indexes = new ArrayList();
        RowMeta lookupIndex;
        LogTableField errorsField;
        ValueMetaBase valueMeta;
        if (this.isBatchIdUsed()) {
            lookupIndex = new RowMeta();
            errorsField = this.getKeyField();
            valueMeta = new ValueMetaBase(errorsField.getFieldName(), errorsField.getDataType());
            valueMeta.setLength(errorsField.getLength());
            lookupIndex.addValueMeta(valueMeta);
            indexes.add(lookupIndex);
        }

        lookupIndex = new RowMeta();
        errorsField = this.findField(ID.ERRORS);
        if (errorsField != null) {
            valueMeta = new ValueMetaBase(errorsField.getFieldName(), errorsField.getDataType());
            valueMeta.setLength(errorsField.getLength());
            lookupIndex.addValueMeta(valueMeta);
        }

        LogTableField statusField = this.findField(ID.STATUS);
        if (statusField != null) {
            ValueMetaInterface valueMeta1 = new ValueMetaBase(statusField.getFieldName(), statusField.getDataType());
            valueMeta1.setLength(statusField.getLength());
            lookupIndex.addValueMeta(valueMeta1);
        }

        LogTableField transNameField = this.findField(ID.JOBNAME);
        if (transNameField != null) {
            ValueMetaInterface valueMeta1 = new ValueMetaBase(transNameField.getFieldName(), transNameField.getDataType());
            valueMeta1.setLength(transNameField.getLength());
            lookupIndex.addValueMeta(valueMeta1);
        }

        indexes.add(lookupIndex);
        return indexes;
    }
    @Override
    public void setAllGlobalParametersToNull() {
        boolean clearGlobalVariables = Boolean.valueOf(System.getProperties().getProperty("KETTLE_GLOBAL_LOG_VARIABLES_CLEAR_ON_EXPORT", "false"));
        if (clearGlobalVariables) {
            super.setAllGlobalParametersToNull();
            this.logInterval = this.isGlobalParameter(this.logInterval) ? null : this.logInterval;
            this.logSizeLimit = this.isGlobalParameter(this.logSizeLimit) ? null : this.logSizeLimit;
        }

    }

    /**
     * ID
     */
    public static enum ID {
        /**
         * ID
         */
        ID_JOB("ID_JOB"),
        CHANNEL_ID("CHANNEL_ID"),
        JOBNAME("JOBNAME"),
        STATUS("STATUS"),
        LINES_READ("LINES_READ"),
        LINES_WRITTEN("LINES_WRITTEN"),
        LINES_UPDATED("LINES_UPDATED"),
        LINES_INPUT("LINES_INPUT"),
        LINES_OUTPUT("LINES_OUTPUT"),
        LINES_REJECTED("LINES_REJECTED"),
        ERRORS("ERRORS"),
        STARTDATE("STARTDATE"),
        ENDDATE("ENDDATE"),
        LOGDATE("LOGDATE"),
        DEPDATE("DEPDATE"),
        REPLAYDATE("REPLAYDATE"),
        LOG_FIELD("LOG_FIELD"),
        EXECUTING_SERVER("EXECUTING_SERVER"),
        EXECUTING_USER("EXECUTING_USER"),
        START_JOB_ENTRY("START_JOB_ENTRY"),
        CLIENT("CLIENT");

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
