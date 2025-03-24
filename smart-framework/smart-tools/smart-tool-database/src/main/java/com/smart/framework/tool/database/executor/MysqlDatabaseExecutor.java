package com.smart.framework.tool.database.executor;

import com.smart.framework.tool.database.converter.DbJavaTypeConverter;

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
