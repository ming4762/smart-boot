package org.pentaho.di.core.logging;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.HasDatabasesInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaDataCombi;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 重写StepLogTable
 * 构造函数改为protected 解决无法被继承的问题
 * @author shizhongming
 */
public class StepLogTable extends BaseLogTable implements Cloneable, LogTableInterface {
    private static final Class<?> PKG = StepLogTable.class;
    public static final String XML_TAG = "step-log-table";

    protected StepLogTable(VariableSpace space, HasDatabasesInterface databasesInterface) {
        super(space, databasesInterface, (String)null, (String)null, (String)null);
    }

    @Override
    public Object clone() {
        try {
            StepLogTable table = (StepLogTable)super.clone();
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
        StringBuilder retval = new StringBuilder();
        retval.append("      ").append(XMLHandler.openTag("step-log-table")).append(Const.CR);
        retval.append("        ").append(XMLHandler.addTagValue("connection", this.connectionName));
        retval.append("        ").append(XMLHandler.addTagValue("schema", this.schemaName));
        retval.append("        ").append(XMLHandler.addTagValue("table", this.tableName));
        retval.append("        ").append(XMLHandler.addTagValue("timeout_days", this.timeoutInDays));
        retval.append(super.getFieldsXML());
        retval.append("      ").append(XMLHandler.closeTag("step-log-table")).append(Const.CR);
        return retval.toString();
    }

    @Override
    public void loadXML(Node node, List<DatabaseMeta> databases, List<StepMeta> steps) {
        this.connectionName = XMLHandler.getTagValue(node, "connection");
        this.schemaName = XMLHandler.getTagValue(node, "schema");
        this.tableName = XMLHandler.getTagValue(node, "table");
        this.timeoutInDays = XMLHandler.getTagValue(node, "timeout_days");
        super.loadFieldsXML(node);
    }

    @Override
    public void replaceMeta(LogTableCoreInterface logTableInterface) {
        if (logTableInterface instanceof StepLogTable) {
            StepLogTable logTable = (StepLogTable)logTableInterface;
            super.replaceMeta(logTable);
        }
    }

    public static StepLogTable getDefault(VariableSpace space, HasDatabasesInterface databasesInterface) {
        StepLogTable table = new StepLogTable(space, databasesInterface);
        table.fields.add(new LogTableField(ID.ID_BATCH.id, true, false, "ID_BATCH", BaseMessages.getString(PKG, "StepLogTable.FieldName.IdBatch"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.IdBatch", new String[0]), 5, 8));
        table.fields.add(new LogTableField(ID.CHANNEL_ID.id, true, false, "CHANNEL_ID", BaseMessages.getString(PKG, "StepLogTable.FieldName.ChannelId"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.ChannelId", new String[0]), 2, 255));
        table.fields.add(new LogTableField(ID.LOG_DATE.id, true, false, "LOG_DATE", BaseMessages.getString(PKG, "StepLogTable.FieldName.LogDate"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.LogDate", new String[0]), 3, -1));
        table.fields.add(new LogTableField(ID.TRANSNAME.id, true, false, "TRANSNAME", BaseMessages.getString(PKG, "StepLogTable.FieldName.TransName"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.TransName", new String[0]), 2, 255));
        table.fields.add(new LogTableField(ID.STEPNAME.id, true, false, "STEPNAME", BaseMessages.getString(PKG, "StepLogTable.FieldName.StepName"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.StepName", new String[0]), 2, 255));
        table.fields.add(new LogTableField(ID.STEP_COPY.id, true, false, "STEP_COPY", BaseMessages.getString(PKG, "StepLogTable.FieldName.StepCopy"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.StepCopy", new String[0]), 5, 3));
        table.fields.add(new LogTableField(ID.LINES_READ.id, true, false, "LINES_READ", BaseMessages.getString(PKG, "StepLogTable.FieldName.LinesRead"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.LinesRead", new String[0]), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_WRITTEN.id, true, false, "LINES_WRITTEN", BaseMessages.getString(PKG, "StepLogTable.FieldName.LinesWritten"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.LinesWritten", new String[0]), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_UPDATED.id, true, false, "LINES_UPDATED", BaseMessages.getString(PKG, "StepLogTable.FieldName.LinesUpdated"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.LinesUpdated", new String[0]), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_INPUT.id, true, false, "LINES_INPUT", BaseMessages.getString(PKG, "StepLogTable.FieldName.LinesInput"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.LinesInput", new String[0]), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_OUTPUT.id, true, false, "LINES_OUTPUT", BaseMessages.getString(PKG, "StepLogTable.FieldName.LinesOutput"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.LinesOutput"), 5, 18));
        table.fields.add(new LogTableField(ID.LINES_REJECTED.id, true, false, "LINES_REJECTED", BaseMessages.getString(PKG, "StepLogTable.FieldName.LinesRejected"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.LinesRejected"), 5, 18));
        table.fields.add(new LogTableField(ID.ERRORS.id, true, false, "ERRORS", BaseMessages.getString(PKG, "StepLogTable.FieldName.Errors"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.Errors"), 5, 18));
        table.fields.add(new LogTableField(ID.LOG_FIELD.id, false, false, "LOG_FIELD", BaseMessages.getString(PKG, "StepLogTable.FieldName.LogField"), BaseMessages.getString(PKG, "StepLogTable.FieldDescription.LogField", new String[0]), 2, 9999999));
        table.findField(ID.TRANSNAME.id).setNameField(true);
        table.findField(ID.LOG_DATE.id).setLogDateField(true);
        table.findField(ID.ID_BATCH.id).setKey(true);
        table.findField(ID.CHANNEL_ID.id).setVisible(false);
        table.findField(ID.LOG_FIELD.id).setLogField(true);
        table.findField(ID.ERRORS.id).setErrorsField(true);
        return table;
    }

    @Override
    public RowMetaAndData getLogRecord(LogStatus status, Object subject, Object parent) {
        if (subject != null && !(subject instanceof StepMetaDataCombi)) {
            return null;
        } else {
            StepMetaDataCombi combi = (StepMetaDataCombi)subject;
            RowMetaAndData row = new RowMetaAndData();

            for (LogTableField field : this.fields) {
                if (field.isEnabled()) {
                    Object value = null;
                    if (subject != null) {
                        switch (ID.valueOf(field.getId())) {
                            case ID_BATCH:
                                value = combi.step.getTrans().getBatchId();
                                break;
                            case CHANNEL_ID:
                                value = combi.step.getLogChannel().getLogChannelId();
                                break;
                            case LOG_DATE:
                                value = new Date();
                                break;
                            case TRANSNAME:
                                value = combi.step.getTrans().getName();
                                break;
                            case STEPNAME:
                                value = combi.stepname;
                                break;
                            case STEP_COPY:
                                value = (long) combi.copy;
                                break;
                            case LINES_READ:
                                value = combi.step.getLinesRead();
                                break;
                            case LINES_WRITTEN:
                                value = combi.step.getLinesWritten();
                                break;
                            case LINES_UPDATED:
                                value = combi.step.getLinesUpdated();
                                break;
                            case LINES_INPUT:
                                value = combi.step.getLinesInput();
                                break;
                            case LINES_OUTPUT:
                                value = combi.step.getLinesOutput();
                                break;
                            case LINES_REJECTED:
                                value = combi.step.getLinesRejected();
                                break;
                            case ERRORS:
                                value = combi.step.getErrors();
                                break;
                            case LOG_FIELD:
                                value = this.getLogBuffer(combi.step, combi.step.getLogChannel().getLogChannelId(), status, null);
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
        return "STEP";
    }

    @Override
    public String getLogTableType() {
        return BaseMessages.getString(PKG, "StepLogTable.Type.Description");
    }

    @Override
    public String getConnectionNameVariable() {
        return "KETTLE_STEP_LOG_DB";
    }

    @Override
    public String getSchemaNameVariable() {
        return "KETTLE_STEP_LOG_SCHEMA";
    }

    @Override
    public String getTableNameVariable() {
        return "KETTLE_STEP_LOG_TABLE";
    }

    @Override
    public List<RowMetaInterface> getRecommendedIndexes() {
        List<RowMetaInterface> indexes = new ArrayList<>();
        return indexes;
    }

    /**
     * ID
     */
    public static enum ID {
        /**
         * ID
         */
        ID_BATCH("ID_BATCH"),
        CHANNEL_ID("CHANNEL_ID"),
        LOG_DATE("LOG_DATE"),
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
        LOG_FIELD("LOG_FIELD");

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
