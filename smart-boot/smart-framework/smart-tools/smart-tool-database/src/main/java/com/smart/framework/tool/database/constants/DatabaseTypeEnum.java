package com.smart.framework.tool.database.constants;

import com.smart.framework.tool.database.executor.DatabaseExecutor;
import com.smart.framework.tool.database.executor.MysqlDatabaseExecutor;
import com.smart.framework.tool.database.executor.OracleDatabaseExecutor;
import com.smart.framework.tool.database.executor.SqlServerDatabaseExecutor;
import lombok.Getter;

/**
 * 数据库类型
 * @author jackson
 */
@Getter
public enum DatabaseTypeEnum {
    /**
     * mysql
     */
    MYSQL("com.mysql.cj.jdbc.Driver", MysqlDatabaseExecutor.class),
    SQL_SERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver", SqlServerDatabaseExecutor.class),
    ORACLE("oracle.jdbc.driver.OracleDriver", OracleDatabaseExecutor.class);



    private final String driverClass;

    private final Class<? extends DatabaseExecutor> executerClass;

    DatabaseTypeEnum(String driverClass, Class<? extends DatabaseExecutor> executerClass) {
        this.driverClass = driverClass;
        this.executerClass = executerClass;
    }
}
