package com.smart.kettle.core.meta;

import com.smart.kettle.core.constants.DatabaseTypeEnum;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleDatabaseException;

/**
 * @author ShiZhongMing
 * 2021/7/15 8:33
 * @since 1.0
 */
public class EnhancedDatabaseMeta extends DatabaseMeta {

    private static final String OLD_MYSQL_DRIVER = "org.gjt.mm.mysql.Driver";

    private static final String NEW_MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * mysql servertimezone
     * TODO:serverTimeZone可以自定义配置
     */
    private static final String MYSQL_TIME_ZONE = "&serverTimeZone=GMT%2B8";

    public EnhancedDatabaseMeta(String name, String type, String access, String host, String db, String port, String user, String pass) {
        super(name, type, access, host, db, port, user, pass);
    }


    @Override
    public String getDriverClass() {
        String driverClass = super.getDriverClass();
        if (OLD_MYSQL_DRIVER.equals(driverClass)) {
            driverClass = NEW_MYSQL_DRIVER;
        }
        return driverClass;
    }

    /**
     * 重写获取URL函数
     * 解决MYSQL 没有设置serverTimeZone导致报错的问题
     * @return URL
     * @throws KettleDatabaseException KettleDatabaseException
     */
    @Override
    public String getURL() throws KettleDatabaseException {
        String url = super.getURL();
        if (DatabaseTypeEnum.MySql.name().equals(this.getDatabaseInterface().getPluginName())) {
            url += MYSQL_TIME_ZONE;
        }
        return url;
    }
}
