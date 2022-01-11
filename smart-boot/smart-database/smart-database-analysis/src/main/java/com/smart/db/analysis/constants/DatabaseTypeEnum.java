package com.smart.db.analysis.constants;

import com.smart.db.analysis.executor.DatabaseExecutor;
import com.smart.db.analysis.executor.MysqlDatabaseExecutor;
import com.smart.db.analysis.executor.OracleDatabaseExecutor;
import com.smart.db.analysis.executor.SqlServerDatabaseExecutor;
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
