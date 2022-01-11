package org.pentaho.di.core.logging;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.metrics.MetricsSnapshotInterface;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
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
public class MetricsLogTable extends BaseLogTable implements Cloneable, LogTableInterface {
    private static Class<?> PKG = MetricsLogTable.class;
    public static final String XML_TAG = "metrics-log-table";

    protected MetricsLogTable(VariableSpace space, HasDatabasesInterface databasesInterface) {
        super(space, databasesInterface, (String)null, (String)null, (String)null);
    }
    @Override
    public Object clone() {
        try {
            MetricsLogTable table = (MetricsLogTable)super.clone();
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
        retval.append("      ").append(XMLHandler.openTag("metrics-log-table")).append(Const.CR);
        retval.append("        ").append(XMLHandler.addTagValue("connection", this.connectionName));
        retval.append("        ").append(XMLHandler.addTagValue("schema", this.schemaName));
        retval.append("        ").append(XMLHandler.addTagValue("table", this.tableName));
        retval.append("        ").append(XMLHandler.addTagValue("timeout_days", this.timeoutInDays));
        retval.append(super.getFieldsXML());
        retval.append("      ").append(XMLHandler.closeTag("metrics-log-table")).append(Const.CR);
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
        if (logTableInterface instanceof MetricsLogTable) {
            MetricsLogTable logTable = (MetricsLogTable)logTableInterface;
            super.replaceMeta(logTable);
        }
    }

    public static MetricsLogTable getDefault(VariableSpace space, HasDatabasesInterface databasesInterface) {
        MetricsLogTable table = new MetricsLogTable(space, databasesInterface);
        table.fields.add(new LogTableField(ID.ID_BATCH.id, true, false, "ID_BATCH", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.IdBatch", new String[0]), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.IdBatch", new String[0]), 5, 8));
        table.fields.add(new LogTableField(ID.CHANNEL_ID.id, true, false, "CHANNEL_ID", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.ChannelId", new String[0]), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.ChannelId", new String[0]), 2, 255));
        table.fields.add(new LogTableField(ID.LOG_DATE.id, true, false, "LOG_DATE", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.LogDate", new String[0]), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.LogDate", new String[0]), 3, -1));
        table.fields.add(new LogTableField(ID.METRICS_DATE.id, true, false, "METRICS_DATE", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.MetricsDate", new String[0]), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.MetricsDate", new String[0]), 3, -1));
        table.fields.add(new LogTableField(ID.METRICS_CODE.id, true, false, "METRICS_CODE", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.MetricsDescription", new String[0]), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.MetricsCode", new String[0]), 2, 255));
        table.fields.add(new LogTableField(ID.METRICS_DESCRIPTION.id, true, false, "METRICS_DESCRIPTION", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.MetricsDescription", new String[0]), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.MetricsDescription", new String[0]), 2, 255));
        table.fields.add(new LogTableField(ID.METRICS_SUBJECT.id, true, false, "METRICS_SUBJECT", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.MetricsSubject", new String[0]), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.MetricsSubject", new String[0]), 2, 255));
        table.fields.add(new LogTableField(ID.METRICS_TYPE.id, true, false, "METRICS_TYPE", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.MetricsType", new String[0]), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.MetricsType", new String[0]), 2, 255));
        table.fields.add(new LogTableField(ID.METRICS_VALUE.id, true, false, "METRICS_VALUE", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.MetricsValue", new String[0]), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.MetricsValue", new String[0]), 5, 12));
        table.findField(ID.LOG_DATE.id).setLogDateField(true);
        table.findField(ID.ID_BATCH.id).setKey(true);
        return table;
    }
    @Override
    public RowMetaAndData getLogRecord(LogStatus status, Object subject, Object parent) {
        if (subject != null && !(subject instanceof LoggingMetric)) {
            return null;
        } else {
            LoggingMetric loggingMetric = (LoggingMetric)subject;
            MetricsSnapshotInterface snapshot = null;
            if (subject != null) {
                snapshot = loggingMetric.getSnapshot();
            }

            RowMetaAndData row = new RowMetaAndData();
            Iterator var7 = this.fields.iterator();

            while(var7.hasNext()) {
                LogTableField field = (LogTableField)var7.next();
                if (field.isEnabled()) {
                    Object value = null;
                    if (subject != null) {
                        switch(ID.valueOf(field.getId())) {
                            case ID_BATCH:
                                value = new Long(loggingMetric.getBatchId());
                                break;
                            case CHANNEL_ID:
                                value = snapshot.getLogChannelId();
                                break;
                            case LOG_DATE:
                                value = new Date();
                                break;
                            case METRICS_DATE:
                                value = snapshot.getDate();
                                break;
                            case METRICS_CODE:
                                value = snapshot.getMetric().getCode();
                                break;
                            case METRICS_DESCRIPTION:
                                value = snapshot.getMetric().getDescription();
                                break;
                            case METRICS_SUBJECT:
                                value = snapshot.getSubject();
                                break;
                            case METRICS_TYPE:
                                value = snapshot.getMetric().getType().name();
                                break;
                            case METRICS_VALUE:
                                value = snapshot.getValue();
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
        return "METRICS";
    }
    @Override
    public String getLogTableType() {
        return BaseMessages.getString(PKG, "MetricsLogTable.Type.Description", new String[0]);
    }
    @Override
    public String getConnectionNameVariable() {
        return "KETTLE_METRICS_LOG_DB";
    }
    @Override
    public String getSchemaNameVariable() {
        return "KETTLE_METRICS_LOG_SCHEMA";
    }
    @Override
    public String getTableNameVariable() {
        return "KETTLE_METRICS_LOG_TABLE";
    }
    @Override
    public List<RowMetaInterface> getRecommendedIndexes() {
        List<RowMetaInterface> indexes = new ArrayList();
        return indexes;
    }

    /**
     * ID
     */
    public static enum ID {
        /**
         * id
         */
        ID_BATCH("ID_BATCH"),
        CHANNEL_ID("CHANNEL_ID"),
        LOG_DATE("LOG_DATE"),
        METRICS_DATE("METRICS_DATE"),
        METRICS_CODE("METRICS_CODE"),
        METRICS_DESCRIPTION("METRICS_DESCRIPTION"),
        METRICS_SUBJECT("METRICS_SUBJECT"),
        METRICS_TYPE("METRICS_TYPE"),
        METRICS_VALUE("METRICS_VALUE");

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
