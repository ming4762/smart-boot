package com.smart.framework.tool.database.executor;


import com.smart.framework.tool.database.converter.DbJavaTypeConverter;

/**
 * @author shizhongming
 * 2020/1/19 8:18 下午
 */
public class OracleDatabaseExecutor extends AbstractDefaultDatabaseExecutor implements DatabaseExecutor {

    public OracleDatabaseExecutor(DbJavaTypeConverter dbJavaTypeConverter) {
        super(dbJavaTypeConverter);
    }
}
