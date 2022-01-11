package com.smart.db.analysis.executor;

import com.smart.db.analysis.converter.DbJavaTypeConverter;

/**
 * mysql数据库执行器
 * @author shizhongming
 * 2020/1/19 8:17 下午
 */
public class MysqlDatabaseExecutor extends AbstractDefaultDatabaseExecutor implements DatabaseExecutor {


    public MysqlDatabaseExecutor(DbJavaTypeConverter dbJavaTypeConverter) {
        super(dbJavaTypeConverter);
    }
}
