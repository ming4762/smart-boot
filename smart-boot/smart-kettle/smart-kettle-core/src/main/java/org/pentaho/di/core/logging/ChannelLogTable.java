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
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author pentaho
 * @author shizhongming
 */
public class ChannelLogTable extends BaseLogTable implements Cloneable, LogTableInterface {
    private static final Class<?> PKG = ChannelLogTable.class;
    public static final String XML_TAG = "channel-log-table";

    protected ChannelLogTable(VariableSpace space, HasDatabasesInterface databasesInterface) {
        super(space, databasesInterface, null, null, null);
    }
    @Override
    public Object clone() {
        try {
            ChannelLogTable table = (ChannelLogTable)super.clone();
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
        retval.append("      ").append(XMLHandler.openTag("channel-log-table")).append(Const.CR);
        retval.append("        ").append(XMLHandler.addTagValue("connection", this.connectionName));
        retval.append("        ").append(XMLHandler.addTagValue("schema", this.schemaName));
        retval.append("        ").append(XMLHandler.addTagValue("table", this.tableName));
        retval.append("        ").append(XMLHandler.addTagValue("timeout_days", this.timeoutInDays));
        retval.append(super.getFieldsXML());
        retval.append("      ").append(XMLHandler.closeTag("channel-log-table")).append(Const.CR);
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
        if (logTableInterface instanceof ChannelLogTable) {
            ChannelLogTable logTable = (ChannelLogTable)logTableInterface;
            super.replaceMeta(logTable);
        }
    }

    public static ChannelLogTable getDefault(VariableSpace space, HasDatabasesInterface databasesInterface) {
        ChannelLogTable table = new ChannelLogTable(space, databasesInterface);
        table.fields.add(new LogTableField(ID.ID_BATCH.id, true, false, "ID_BATCH", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.IdBatch"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.IdBatch"), 5, 8));
        table.fields.add(new LogTableField(ID.CHANNEL_ID.id, true, false, "CHANNEL_ID", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.ChannelId"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.ChannelId"), 2, 255));
        table.fields.add(new LogTableField(ID.LOG_DATE.id, true, false, "LOG_DATE", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.LogDate"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.LogDate"), 3, -1));
        table.fields.add(new LogTableField(ID.LOGGING_OBJECT_TYPE.id, true, false, "LOGGING_OBJECT_TYPE", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.ObjectType"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.ObjectType"), 2, 255));
        table.fields.add(new LogTableField(ID.OBJECT_NAME.id, true, false, "OBJECT_NAME", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.ObjectName"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.ObjectName"), 2, 255));
        table.fields.add(new LogTableField(ID.OBJECT_COPY.id, true, false, "OBJECT_COPY", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.ObjectCopy"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.ObjectCopy"), 2, 255));
        table.fields.add(new LogTableField(ID.REPOSITORY_DIRECTORY.id, true, false, "REPOSITORY_DIRECTORY", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.RepositoryDirectory"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.RepositoryDirectory"), 2, 255));
        table.fields.add(new LogTableField(ID.FILENAME.id, true, false, "FILENAME", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.Filename"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.Filename"), 2, 255));
        table.fields.add(new LogTableField(ID.OBJECT_ID.id, true, false, "OBJECT_ID", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.ObjectId"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.ObjectId"), 2, 255));
        table.fields.add(new LogTableField(ID.OBJECT_REVISION.id, true, false, "OBJECT_REVISION", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.ObjectRevision"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.ObjectRevision"), 2, 255));
        table.fields.add(new LogTableField(ID.PARENT_CHANNEL_ID.id, true, false, "PARENT_CHANNEL_ID", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.ParentChannelId"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.ParentChannelId"), 2, 255));
        table.fields.add(new LogTableField(ID.ROOT_CHANNEL_ID.id, true, false, "ROOT_CHANNEL_ID", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.RootChannelId"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.RootChannelId"), 2, 255));
        table.findField(ID.LOG_DATE.id).setLogDateField(true);
        table.findField(ID.ID_BATCH.id).setKey(true);
        return table;
    }
    @Override
    public RowMetaAndData getLogRecord(LogStatus status, Object subject, Object parent) {
        if (subject != null && !(subject instanceof LoggingHierarchy)) {
            return null;
        } else {
            LoggingHierarchy loggingHierarchy = (LoggingHierarchy)subject;
            LoggingObjectInterface loggingObject = null;
            if (subject != null) {
                loggingObject = loggingHierarchy.getLoggingObject();
            }

            RowMetaAndData row = new RowMetaAndData();

            for (LogTableField field : this.fields) {
                if (field.isEnabled()) {
                    Object value = null;
                    if (subject != null) {
                        switch (ID.valueOf(field.getId())) {
                            case ID_BATCH:
                                value = loggingHierarchy.getBatchId();
                                break;
                            case CHANNEL_ID:
                                value = loggingObject.getLogChannelId();
                                break;
                            case LOG_DATE:
                                value = new Date();
                                break;
                            case LOGGING_OBJECT_TYPE:
                                value = loggingObject.getObjectType().toString();
                                break;
                            case OBJECT_NAME:
                                value = loggingObject.getObjectName();
                                break;
                            case OBJECT_COPY:
                                value = loggingObject.getObjectCopy();
                                break;
                            case REPOSITORY_DIRECTORY:
                                value = loggingObject.getRepositoryDirectory() == null ? null : loggingObject.getRepositoryDirectory().getPath();
                                break;
                            case FILENAME:
                                value = loggingObject.getFilename();
                                break;
                            case OBJECT_ID:
                                value = loggingObject.getObjectId() == null ? null : loggingObject.getObjectId().toString();
                                break;
                            case OBJECT_REVISION:
                                value = loggingObject.getObjectRevision() == null ? null : loggingObject.getObjectRevision().toString();
                                break;
                            case PARENT_CHANNEL_ID:
                                value = loggingObject.getParent() == null ? null : loggingObject.getParent().getLogChannelId();
                                break;
                            case ROOT_CHANNEL_ID:
                                value = loggingHierarchy.getRootChannelId();
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
        return "CHANNEL";
    }
    @Override
    public String getLogTableType() {
        return BaseMessages.getString(PKG, "ChannelLogTable.Type.Description");
    }
    @Override
    public String getConnectionNameVariable() {
        return "KETTLE_CHANNEL_LOG_DB";
    }
    @Override
    public String getSchemaNameVariable() {
        return "KETTLE_CHANNEL_LOG_SCHEMA";
    }
    @Override
    public String getTableNameVariable() {
        return "KETTLE_CHANNEL_LOG_TABLE";
    }
    @Override
    public List<RowMetaInterface> getRecommendedIndexes() {
        return new ArrayList<>();
    }

    /**
     * ID美剧
     */
    public enum ID {
        /**
         * ID字段信息
         */
        ID_BATCH("ID_BATCH"),
        CHANNEL_ID("CHANNEL_ID"),
        LOG_DATE("LOG_DATE"),
        LOGGING_OBJECT_TYPE("LOGGING_OBJECT_TYPE"),
        OBJECT_NAME("OBJECT_NAME"),
        OBJECT_COPY("OBJECT_COPY"),
        REPOSITORY_DIRECTORY("REPOSITORY_DIRECTORY"),
        FILENAME("FILENAME"),
        OBJECT_ID("OBJECT_ID"),
        OBJECT_REVISION("OBJECT_REVISION"),
        PARENT_CHANNEL_ID("PARENT_CHANNEL_ID"),
        ROOT_CHANNEL_ID("ROOT_CHANNEL_ID");

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
