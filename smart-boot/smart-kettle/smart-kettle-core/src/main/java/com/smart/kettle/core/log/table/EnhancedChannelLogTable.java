package com.smart.kettle.core.log.table;

import com.smart.kettle.core.log.modifier.LogModifierHandler;
import com.smart.kettle.core.log.modifier.LogModifierHandlerSetter;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.logging.ChannelLogTable;
import org.pentaho.di.core.logging.LogStatus;
import org.pentaho.di.core.logging.LogTableField;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.HasDatabasesInterface;

/**
 * @author ShiZhongMing
 * 2021/7/22 16:31
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
public class EnhancedChannelLogTable extends ChannelLogTable implements LogModifierHandlerSetter {

    private static final Class<?> PKG = ChannelLogTable.class;

    private LogModifierHandler logModifierHandler;

    protected EnhancedChannelLogTable(VariableSpace space, HasDatabasesInterface databasesInterface) {
        super(space, databasesInterface);
    }

    public static EnhancedChannelLogTable getDefault(VariableSpace space, HasDatabasesInterface databasesInterface) {
        EnhancedChannelLogTable table = new EnhancedChannelLogTable(space, databasesInterface);
        table.fields.add(new LogTableField(ID.ID_BATCH.toString(), true, false, "ID_BATCH", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.IdBatch"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.IdBatch"), 5, 8));
        table.fields.add(new LogTableField(ID.CHANNEL_ID.toString(), true, false, "CHANNEL_ID", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.ChannelId"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.ChannelId"), 2, 255));
        table.fields.add(new LogTableField(ID.LOG_DATE.toString(), true, false, "LOG_DATE", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.LogDate"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.LogDate"), 3, -1));
        table.fields.add(new LogTableField(ID.LOGGING_OBJECT_TYPE.toString(), true, false, "LOGGING_OBJECT_TYPE", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.ObjectType"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.ObjectType"), 2, 255));
        table.fields.add(new LogTableField(ID.OBJECT_NAME.toString(), true, false, "OBJECT_NAME", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.ObjectName"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.ObjectName"), 2, 255));
        table.fields.add(new LogTableField(ID.OBJECT_COPY.toString(), true, false, "OBJECT_COPY", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.ObjectCopy"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.ObjectCopy"), 2, 255));
        table.fields.add(new LogTableField(ID.REPOSITORY_DIRECTORY.toString(), true, false, "REPOSITORY_DIRECTORY", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.RepositoryDirectory"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.RepositoryDirectory"), 2, 255));
        table.fields.add(new LogTableField(ID.FILENAME.toString(), true, false, "FILENAME", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.Filename"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.Filename"), 2, 255));
        table.fields.add(new LogTableField(ID.OBJECT_ID.toString(), true, false, "OBJECT_ID", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.ObjectId"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.ObjectId"), 2, 255));
        table.fields.add(new LogTableField(ID.OBJECT_REVISION.toString(), true, false, "OBJECT_REVISION", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.ObjectRevision"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.ObjectRevision"), 2, 255));
        table.fields.add(new LogTableField(ID.PARENT_CHANNEL_ID.toString(), true, false, "PARENT_CHANNEL_ID", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.ParentChannelId"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.ParentChannelId"), 2, 255));
        table.fields.add(new LogTableField(ID.ROOT_CHANNEL_ID.toString(), true, false, "ROOT_CHANNEL_ID", BaseMessages.getString(PKG, "ChannelLogTable.FieldName.RootChannelId"), BaseMessages.getString(PKG, "ChannelLogTable.FieldDescription.RootChannelId"), 2, 255));
        table.findField(ID.LOG_DATE.toString()).setLogDateField(true);
        table.findField(ID.ID_BATCH.toString()).setKey(true);
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
