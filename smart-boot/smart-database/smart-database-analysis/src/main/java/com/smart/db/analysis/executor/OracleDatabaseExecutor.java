package com.smart.db.analysis.executor;

import com.smart.db.analysis.converter.DbJavaTypeConverter;

/**
 * @author shizhongming
 * 2020/1/19 8:18 下午
 */
public class OracleDatabaseExecutor extends AbstractDefaultDatabaseExecutor implements DatabaseExecutor {

    public OracleDatabaseExecutor(DbJavaTypeConverter dbJavaTypeConverter) {
        super(dbJavaTypeConverter);
    }
}
