package com.smart.db.analysis.executor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.smart.commons.core.utils.ReflectUtils;
import com.smart.db.analysis.annotation.DatabaseField;
import com.smart.db.analysis.constants.ExceptionConstant;
import com.smart.db.analysis.constants.ExtMappingEnum;
import com.smart.db.analysis.constants.DbTableTypeEnum;
import com.smart.db.analysis.constants.TypeMappingEnum;
import com.smart.db.analysis.converter.DbJavaTypeConverter;
import com.smart.db.analysis.exception.SmartDatabaseException;
import com.smart.db.analysis.pojo.bo.ColumnBO;
import com.smart.db.analysis.pojo.dbo.*;
import com.smart.db.analysis.pool.DbConnectionProvider;
import com.smart.db.analysis.pool.model.DbConnectionConfig;
import com.smart.db.analysis.utils.CacheUtils;
import com.smart.db.analysis.utils.DatabaseUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2020/1/18 9:02 下午
 */
@Slf4j
public abstract class AbstractDefaultDatabaseExecutor implements DatabaseExecutor {

    private final DbJavaTypeConverter dbJavaTypeConverter;

    private DbConnectionProvider dbConnectionProvider;

    protected AbstractDefaultDatabaseExecutor(DbJavaTypeConverter dbJavaTypeConverter) {
        this.dbJavaTypeConverter = dbJavaTypeConverter;
    }

    static {
        AbstractDefaultDatabaseExecutor.mappingDatabaseFieldToCache();
        AbstractDefaultDatabaseExecutor.initTypeMappingCache();
    }

    /**
     * IN 占位符
     */
    private static final String IN_PLACEHOLDER = "%in";



    /**
     * 测试数据库连接
     * @param connection 数据库连接
     * @return 连接是否成功
     * @throws SQLException SQLException
     */
    @Override
    public boolean checkConnection(Connection connection) throws SQLException {
        return connection != null && !connection.isClosed();
    }

    @Override
    public boolean testConnection(@NonNull DbConnectionConfig connectionConfig) throws SQLException {
        Connection connection = null;
        try {
            connection = this.dbConnectionProvider.getConnection(connectionConfig);
            return this.checkConnection(connection);
        } finally {
            if (connection != null) {
                this.dbConnectionProvider.returnConnection(connectionConfig, connection);
            }
        }
    }

    /**
     * 获取表格信息
     * @param types 类型
     */
    @SneakyThrows(SQLException.class)
    @Override
    public @NonNull List<TableViewDO> listBaseTable(@NonNull DbConnectionConfig connectionConfig, @Nullable String tableNamePattern, DbTableTypeEnum... types) {
        Connection connection = this.dbConnectionProvider.getConnection(connectionConfig);
        try {
            if (!this.checkConnection(connection)|| ArrayUtils.isEmpty(types)) {
                return Lists.newArrayList();
            }
            // 转换类型
            String[] typesStr = Arrays.stream(types)
                    .map(Enum::name)
                    .toArray(String[]::new);
            // 获取resultSet
            try (ResultSet resultSet = connection.getMetaData().getTables(connection.getCatalog(), connection.getSchema(), tableNamePattern, typesStr)) {
                Map<String, Field> mapping = this.getDatabaseMapping(TableViewDO.class);
                return DatabaseUtils.resultSetToModel(resultSet, TableViewDO.class, mapping);
            }
        }  finally {
            this.dbConnectionProvider.returnConnection(connectionConfig, connection);
        }
    }

    /**
     * 查询主键信息
     * @param connectionConfig 数据库连接信息
     * @param tableName 标明
     * @return 主键列表
     */
    @SneakyThrows(SQLException.class)
    @Override
    public List<PrimaryKeyDO> listPrimaryKey(@NonNull DbConnectionConfig connectionConfig, String tableName)  {
        Connection connection = this.dbConnectionProvider.getConnection(connectionConfig);
        try {
            if (!this.checkConnection(connection)) {
                return Lists.newArrayList();
            }
            try (ResultSet resultSet = connection.getMetaData().getPrimaryKeys(connection.getCatalog(), null, tableName)) {
                Map<String, Field> mapping = CacheUtils.getFieldMapping(PrimaryKeyDO.class);
                if (mapping == null) {
                    throw new SmartDatabaseException(ExceptionConstant.DATABASE_FILE_MAPPING_NOT_FOUND, PrimaryKeyDO.class.getName());
                }
                return DatabaseUtils.resultSetToModel(resultSet, PrimaryKeyDO.class, mapping);
            }
        } finally {
            this.dbConnectionProvider.returnConnection(connectionConfig, connection);
        }
    }

    /**
     * 查询外键信息
     * @param connectionConfig 数据库连接信息
     * @param tableName 表名
     * @return 外键列表
     */
    @SneakyThrows(SQLException.class)
    @Override
    public List<ImportKeyDO> listImportedKeys(@NonNull DbConnectionConfig connectionConfig, String tableName){
        Connection connection = this.dbConnectionProvider.getConnection(connectionConfig);
        try {
            if (!this.checkConnection(connection)) {
                return Lists.newArrayList();
            }

            try (ResultSet resultSet  = connection.getMetaData().getImportedKeys(connection.getCatalog(), connection.getSchema(), tableName)) {
                Map<String, Field> mapping = CacheUtils.getFieldMapping(ImportKeyDO.class);
                if (mapping == null) {
                    throw new SmartDatabaseException(ExceptionConstant.DATABASE_FILE_MAPPING_NOT_FOUND, ImportKeyDO.class.getName());
                }
                return DatabaseUtils.resultSetToModel(resultSet, ImportKeyDO.class, mapping);
            }
        } finally {
            this.dbConnectionProvider.returnConnection(connectionConfig, connection);
        }
    }

    /**
     * 查询索引信息
     * @param connectionConfig 数据库连接信息
     * @param tableName 表名
     * @param unique 是否查询唯一索引
     * @param approximate when true, result is allowed to reflect approximate or out of data values; when false, results are requested to be accurate
     * @return 主键信息
     */
    @SneakyThrows(SQLException.class)
    @Override
    public List<IndexDO> listIndex(@NonNull DbConnectionConfig connectionConfig, String tableName, Boolean unique, Boolean approximate) {
        final Connection connection = this.dbConnectionProvider.getConnection(connectionConfig);
        try {
            if (!this.checkConnection(connection)) {
                return Lists.newArrayList();
            }
            if (approximate == null) {
                approximate = true;
            }
            if (unique == null) {
                unique = true;
            }
            try (ResultSet resultSet = connection.getMetaData().getIndexInfo(connection.getCatalog(), connection.getSchema(), tableName, unique, approximate)) {
                Map<String, Field> mapping = CacheUtils.getFieldMapping(IndexDO.class);
                if (mapping == null) {
                    throw new SmartDatabaseException(ExceptionConstant.DATABASE_FILE_MAPPING_NOT_FOUND, IndexDO.class.getName());
                }
                return DatabaseUtils.resultSetToModel(resultSet, IndexDO.class, mapping);
            }
        }  finally {
            this.dbConnectionProvider.returnConnection(connectionConfig, connection);
        }
    }

    /**
     * 查询列信息
     * @param connectionConfig 数据库连接信息
     * @param tableName 表名
     * @return 列信息
     */
    @Override
    @NonNull
    public List<ColumnBO> listColumn(@NonNull DbConnectionConfig connectionConfig, @NonNull String tableName) {

        log.info("读取表【{}】列信息", tableName);

        final String join = "#";
        final List<ColumnDO> baseColumnList = this.listBaseColumn(connectionConfig, tableName);
        final List<ColumnBO> columnList = ColumnBO.batchCreateFromDo(baseColumnList);
        if (!columnList.isEmpty()) {
            Map<String, ColumnBO> columnMap = columnList.stream().collect(Collectors.toMap(column -> String.join(join, column.getTableName(), column.getColumnName()), item -> item));
            // 查询主键信息并设置
            List<PrimaryKeyDO> primaryKeyList = this.listPrimaryKey(connectionConfig, tableName);
            primaryKeyList.forEach(item -> {
                String key = String.join(join, item.getTableName(), item.getColumnName());
                ColumnBO column = columnMap.get(key);
                if (column != null) {
                    column.setPrimaryKey(Boolean.TRUE);
                    column.setKeySeq(item.getKeySeq());
                    column.setPkName(item.getPkName());
                }
            });
            // 查询外键信息并设置
            List<ImportKeyDO> importKeyList = this.listImportedKeys(connectionConfig, tableName);
            importKeyList.forEach(item -> {
                ColumnBO column = columnMap.get(String.join(join, item.getFktableName(), item.getPkcolumnName()));
                if (column != null) {
                    column.setImportKey(Boolean.TRUE);
                    column.setImportPkName(item.getPkName());
                }
            });
            // 查询索引信息并设置
            List<IndexDO> indexList = this.listUniqueIndex(connectionConfig, tableName);
            indexList.forEach(item -> {
                ColumnBO column = columnMap.get(String.join(join, item.getTableName(), item.getColumnName()));
                if (column != null) {
                    column.setIndexed(Boolean.TRUE);
                    column.setUnique(Boolean.TRUE);
                    column.setIndexType(item.getType());
                }
            });
            // TODO:查询其他索引
            // 设置其他信息
            columnList.forEach(item -> {
                TypeMappingEnum typeMappingConstant = this.dbJavaTypeConverter.convert(item);
                if (typeMappingConstant != null) {
                    item.setJavaType(typeMappingConstant.getJavaClass().getName());
                    item.setSimpleJavaType(typeMappingConstant.getJavaClass().getSimpleName());
                    // 设置extjs类型
                    final String extType = Optional.ofNullable(CacheUtils.getExtTypeMapping(item.getJavaType()))
                            .map(ExtMappingEnum::getExtClass)
                            .orElse(null);
                    item.setExtType(extType);
                }
            });
        }
        return columnList;
    }

    /**
     * 查询列基本信息
     * @param connectionConfig 数据库连接
     * @param tableName 表名
     * @return 列基本信息
     */
    @SneakyThrows(SQLException.class)
    @Override
    public List<ColumnDO> listBaseColumn(@NonNull DbConnectionConfig connectionConfig, @NonNull String tableName) {
        final Connection connection = this.dbConnectionProvider.getConnection(connectionConfig);
        try {
            if (!this.checkConnection(connection)) {
                return Lists.newArrayList();
            }
            try (ResultSet resultSet = connection.getMetaData().getColumns(connection.getCatalog(), connection.getSchema(), tableName, null)) {
                Map<String, Field> mapping = CacheUtils.getFieldMapping(ColumnDO.class);
                if (mapping == null) {
                    throw new SmartDatabaseException(ExceptionConstant.DATABASE_FILE_MAPPING_NOT_FOUND, ColumnBO.class.getName());
                }
                return DatabaseUtils.resultSetToModel(resultSet, ColumnDO.class, mapping);
            }
        } finally {
            this.dbConnectionProvider.returnConnection(connectionConfig, connection);
        }
    }

    /**
     * 查询唯一索引
     * @param connectionConfig 数据库连接信息
     * @param tableName 表名
     * @return 唯一索引列表
     */
    @Override
    public List<IndexDO> listUniqueIndex(@NonNull DbConnectionConfig connectionConfig, String tableName) {
        return this.listIndex(connectionConfig, tableName, true, true);
    }

    /**
     * 映射数据库字段与实体类属性关系
     */
    private static void mappingDatabaseFieldToCache() {
        if (CacheUtils.isFieldMappingEmpty()) {
            CacheUtils.setFieldMapping(TableViewDO.class, mappingDatabaseField(TableViewDO.class));
            CacheUtils.setFieldMapping(PrimaryKeyDO.class, mappingDatabaseField(PrimaryKeyDO.class));
            CacheUtils.setFieldMapping(IndexDO.class, mappingDatabaseField(IndexDO.class));
            CacheUtils.setFieldMapping(ImportKeyDO.class, mappingDatabaseField(ImportKeyDO.class));
            CacheUtils.setFieldMapping(ColumnDO.class, mappingDatabaseField(ColumnDO.class));
            CacheUtils.setFieldMapping(ColumnRemarkDO.class, mappingDatabaseField(ColumnRemarkDO.class));
            CacheUtils.setFieldMapping(TableRemarkDO.class, mappingDatabaseField(TableRemarkDO.class));
        }
    }

    /**
     * 初始化类型映射
     */
    private static void initTypeMappingCache() {
        CacheUtils.setTypeMapping(
                Arrays.stream(TypeMappingEnum.values())
                .collect(Collectors.toMap(TypeMappingEnum :: getDataType, item -> item))
        );
        CacheUtils.setExtTypeMapping(
                Arrays.stream(ExtMappingEnum.values())
                .collect(Collectors.toMap(item -> item.getJavaClass().getName(), item -> item))
        );
    }

    /**
     * 映射实体类属性
     * @param clazz Class
     * @return 实体类属性映射
     */
    private static Map<String, Field> mappingDatabaseField(Class<? extends AbstractDatabaseBaseDO> clazz) {
        final Map<String, Field> mapping = Maps.newHashMap();
        Set<Field> fieldSet = Sets.newHashSet();
        // 获取所有field
        ReflectUtils.getAllFields(clazz, fieldSet);
        fieldSet.forEach(field -> {
                    final DatabaseField databaseFieldAnnotation = AnnotationUtils.getAnnotation(field, DatabaseField.class);
                    if (databaseFieldAnnotation != null) {
                        final String value = databaseFieldAnnotation.value();
                        if (StringUtils.isNotBlank(value)) {
                            mapping.put(value, field);
                        }
                    }
                });
        return mapping;
    }

    /**
     * 查询表格备注
     * @param tableList 表格列表
     * @param commentSql 查询sql
     */
    @SneakyThrows(SQLException.class)
    protected void queryTableRemark(@NonNull DbConnectionConfig connectionConfig, @NonNull List<TableViewDO> tableList, @NonNull String commentSql) {
        if (tableList.isEmpty()) {
            return;
        }
        // 获取数据库连接
        final Connection connection = this.dbConnectionProvider.getConnection(connectionConfig);
        // 获取表名
        List<String> tableNameList = tableList.stream()
                .map(TableViewDO::getTableName).toList();
        try (
                PreparedStatement psmt = this.setInParameter(connection, commentSql, tableNameList);
                ResultSet rs = psmt.executeQuery()
        ) {
            final List<TableRemarkDO> tableRemarkList = DatabaseUtils.resultSetToModel(rs, TableRemarkDO.class, this.getDatabaseMapping(TableRemarkDO.class));
            // 转为map
            if (!CollectionUtils.isEmpty(tableRemarkList)) {
                Map<String, String> tableRemarkMap = tableRemarkList.stream()
                        .collect(Collectors.toMap(TableRemarkDO :: getTableName, TableRemarkDO :: getRemark));
                // 遍历设置备注
                tableList.forEach(table -> table.setRemarks(tableRemarkMap.get(table.getTableName())));
            }
        } finally {
            this.dbConnectionProvider.returnConnection(connectionConfig, connection);
        }
    }

    /**
     * 查询列备注
     * @param connectionConfig 数据库连接
     * @param columnList 表格列表
     * @param commentSql 查询sql
     */
    @SneakyThrows({SQLException.class})
    protected void queryColumnRemark(@NonNull DbConnectionConfig connectionConfig, @NonNull List<ColumnDO> columnList, @NonNull String commentSql) {
        if (columnList.isEmpty()) {
            return;
        }
        // 获取数据库连接
        final Connection connection = this.dbConnectionProvider.getConnection(connectionConfig);
        // 获取表名
        Set<String> tableNames = columnList.stream()
                .map(ColumnDO::getTableName).collect(Collectors.toSet());
        try (
                PreparedStatement psmt = this.setInParameter(connection, commentSql, tableNames);
                ResultSet rs = psmt.executeQuery()
                ) {
            final List<ColumnRemarkDO> columnRemarkList = DatabaseUtils.resultSetToModel(rs, ColumnRemarkDO.class, this.getDatabaseMapping(ColumnRemarkDO.class));
            if (!CollectionUtils.isEmpty(columnRemarkList)) {
                Map<String, String> columnRemarkMap = columnRemarkList.stream()
                        .collect(Collectors.toMap(item -> item.getTableName() + item.getColumnName(), ColumnRemarkDO::getRemark));
                columnList.forEach(column -> column.setRemarks(columnRemarkMap.get(column.getTableName() + column.getColumnName())));
            }
        } finally {
            this.dbConnectionProvider.returnConnection(connectionConfig, connection);
        }
    }

    /**
     * 设置IN参数
     * @param connection 数据库连接
     * @param commentSql sql
     * @param parameterList 参数列表
     * @return PreparedStatement
     */
    @SneakyThrows(SQLException.class)
    private PreparedStatement setInParameter(@NonNull Connection connection, @NonNull String commentSql, @NonNull Collection<String> parameterList) {
        if (!CollectionUtils.isEmpty(parameterList) && commentSql.contains(IN_PLACEHOLDER)) {
            commentSql = commentSql.replace(IN_PLACEHOLDER, String.format(" (%s) ", parameterList.stream().map(item -> "?").collect(Collectors.joining(","))));
        }
        final PreparedStatement preparedStatement = connection.prepareStatement(commentSql);
        // 设置参数值
        AtomicInteger atomicInteger = new AtomicInteger(0);
        for (String parameter : parameterList) {
            preparedStatement.setString(atomicInteger.incrementAndGet(), parameter);
        }
        return preparedStatement;
    }

    /**
     * 获取数据库实体映射
     * @param clazz 实体类
     */
    protected Map<String, Field> getDatabaseMapping(Class<? extends AbstractDatabaseBaseDO> clazz) {
        final Map<String, Field> fieldMap = CacheUtils.getFieldMapping(clazz);
        if (CollectionUtils.isEmpty(fieldMap)) {
            throw new SmartDatabaseException(ExceptionConstant.DATABASE_FILE_MAPPING_NOT_FOUND, clazz.getName());
        }
        return fieldMap;
    }


    @Autowired
    public void setDbConnectionProvider(DbConnectionProvider dbConnectionProvider) {
        this.dbConnectionProvider = dbConnectionProvider;
    }
}
