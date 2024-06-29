package com.smart.kettle.core;

import com.smart.kettle.core.constants.DatabaseAccessEnum;
import com.smart.kettle.core.constants.DatabaseTypeEnum;
import com.smart.kettle.core.properties.LogDatabaseProperties;
import lombok.Getter;
import lombok.Setter;
import org.pentaho.di.core.logging.LogLevel;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author ShiZhongMing
 * 2021/7/16 13:03
 * @since 1.0
 */
@Getter
@Setter
@ConfigurationProperties("smart.kettle")
public class KettleProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 6156315172625795691L;
    
    /**
     * 日志配置信息
     */
    private LogDatabaseProperties log = new LogDatabaseProperties();

    private DbRepository dbRepository = new DbRepository();

    /**
     * kettle log级别
     */
    private LogLevel logLevel = LogLevel.BASIC;


    @Getter
    @Setter
    public static class DbRepository implements Serializable {
        @Serial
        private static final long serialVersionUID = -3023014824864610678L;

        private Boolean enabled = false;

        private DatabaseTypeEnum type;
        private DatabaseAccessEnum access;
        private String name;
        private String host;
        private String db;
        private String port;
        private String dbUser;
        private String dbPassword;

        private String resUser;
        private String resPassword;
        private String repositoryName;
        private String description;

        private Boolean forceIdentifiersToLowercase;
        /**
         * 数据库是否强制使用大写
         */
        private Boolean forceIdentifiersToUppercase;
    }
}
