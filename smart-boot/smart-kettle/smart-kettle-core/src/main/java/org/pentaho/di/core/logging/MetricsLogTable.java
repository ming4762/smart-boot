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
import java.util.List;

/**
 * @author pentaho
 * @author shizhongming
 */
public class MetricsLogTable extends BaseLogTable implements Cloneable, LogTableInterface {
    private static final Class<?> PKG = MetricsLogTable.class;
    public static final String XML_TAG = "metrics-log-table";

    protected MetricsLogTable(VariableSpace space, HasDatabasesInterface databasesInterface) {
        super(space, databasesInterface, null, null, null);
    }
    @Override
    public Object clone() {
        try {
            MetricsLogTable table = (MetricsLogTable)super.clone();
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
        return "      " + XMLHandler.openTag("metrics-log-table") + Const.CR +
                "        " + XMLHandler.addTagValue("connection", this.connectionName) +
                "        " + XMLHandler.addTagValue("schema", this.schemaName) +
                "        " + XMLHandler.addTagValue("table", this.tableName) +
                "        " + XMLHandler.addTagValue("timeout_days", this.timeoutInDays) +
                super.getFieldsXML() +
                "      " + XMLHandler.closeTag("metrics-log-table") + Const.CR;
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
        if (logTableInterface instanceof MetricsLogTable logTable) {
            super.replaceMeta(logTable);
        }
    }

    public static MetricsLogTable getDefault(VariableSpace space, HasDatabasesInterface databasesInterface) {
        MetricsLogTable table = new MetricsLogTable(space, databasesInterface);
        table.fields.add(new LogTableField(ID.ID_BATCH.id, true, false, "ID_BATCH", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.IdBatch"), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.IdBatch"), 5, 8));
        table.fields.add(new LogTableField(ID.CHANNEL_ID.id, true, false, "CHANNEL_ID", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.ChannelId"), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.ChannelId"), 2, 255));
        table.fields.add(new LogTableField(ID.LOG_DATE.id, true, false, "LOG_DATE", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.LogDate"), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.LogDate"), 3, -1));
        table.fields.add(new LogTableField(ID.METRICS_DATE.id, true, false, "METRICS_DATE", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.MetricsDate"), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.MetricsDate"), 3, -1));
        table.fields.add(new LogTableField(ID.METRICS_CODE.id, true, false, "METRICS_CODE", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.MetricsDescription"), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.MetricsCode"), 2, 255));
        table.fields.add(new LogTableField(ID.METRICS_DESCRIPTION.id, true, false, "METRICS_DESCRIPTION", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.MetricsDescription"), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.MetricsDescription"), 2, 255));
        table.fields.add(new LogTableField(ID.METRICS_SUBJECT.id, true, false, "METRICS_SUBJECT", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.MetricsSubject"), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.MetricsSubject"), 2, 255));
        table.fields.add(new LogTableField(ID.METRICS_TYPE.id, true, false, "METRICS_TYPE", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.MetricsType"), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.MetricsType"), 2, 255));
        table.fields.add(new LogTableField(ID.METRICS_VALUE.id, true, false, "METRICS_VALUE", BaseMessages.getString(PKG, "MetricsLogTable.FieldName.MetricsValue"), BaseMessages.getString(PKG, "MetricsLogTable.FieldDescription.MetricsValue"), 5, 12));
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

            for (LogTableField field : this.fields) {
                if (field.isEnabled()) {
                    Object value = null;
                    if (subject != null) {
                        switch (ID.valueOf(field.getId())) {
                            case ID_BATCH -> value = loggingMetric.getBatchId();
                            case CHANNEL_ID -> value = snapshot.getLogChannelId();
                            case LOG_DATE -> value = new Date();
                            case METRICS_DATE -> value = snapshot.getDate();
                            case METRICS_CODE -> value = snapshot.getMetric().getCode();
                            case METRICS_DESCRIPTION -> value = snapshot.getMetric().getDescription();
                            case METRICS_SUBJECT -> value = snapshot.getSubject();
                            case METRICS_TYPE -> value = snapshot.getMetric().getType().name();
                            case METRICS_VALUE -> value = snapshot.getValue();
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
        return "METRICS";
    }
    @Override
    public String getLogTableType() {
        return BaseMessages.getString(PKG, "MetricsLogTable.Type.Description");
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
        return new ArrayList<>();
    }

    /**
     * ID
     */
    public enum ID {
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
