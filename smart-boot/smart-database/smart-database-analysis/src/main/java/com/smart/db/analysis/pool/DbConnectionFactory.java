package com.smart.db.analysis.pool;

import com.smart.db.analysis.pool.model.DbConnectionConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库连接池工厂
 * @author ShiZhongMing
 * 2021/2/10 16:27
 * @since 1.0
 */
@Slf4j
public class DbConnectionFactory implements KeyedPooledObjectFactory<DbConnectionConfig, Connection> {
    @Override
    public PooledObject<Connection> makeObject(DbConnectionConfig connectionConfig) throws Exception {
        final Connection connection = this.createConnection(connectionConfig);
        return new DefaultPooledObject<>(connection);
    }

    /**
     * 创建数据库连接
     * @param connectionConfig 连接配置
     * @return 数据库连接
     * @throws SQLException SQLException
     */
    private Connection createConnection(DbConnectionConfig connectionConfig) throws SQLException {
        final Driver driver = connectionConfig.doGetDriver();
        final Properties properties = new Properties();
        properties.setProperty("user", connectionConfig.getUsername());
        properties.setProperty("remarks", Boolean.TRUE.toString());
        properties.setProperty("useInformationSchema", Boolean.TRUE.toString());
        properties.setProperty("password", connectionConfig.getPassword());
        final Connection connection = driver.connect(connectionConfig.getUrl(), properties);
        if (StringUtils.isNotBlank(connectionConfig.getTableSchema())) {
            connection.setSchema(connectionConfig.getTableSchema());
        }
        return connection;
    }

    /**
     * 销毁连接
     * @param config 配置信息
     * @param pooledObject 池对象
     * @throws Exception Exception
     */
    @Override
    public void destroyObject(DbConnectionConfig config, PooledObject<Connection> pooledObject) throws Exception {
        final Connection connection = pooledObject.getObject();
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Override
    public boolean validateObject(DbConnectionConfig config, PooledObject<Connection> pooledObject) {
        final Connection connection = pooledObject.getObject();
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void activateObject(DbConnectionConfig config, PooledObject<Connection> pooledObject) {
        // do nothing
    }

    @Override
    public void passivateObject(DbConnectionConfig config, PooledObject<Connection> pooledObject) {
        // do nothing
    }
}
