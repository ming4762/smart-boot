package com.smart.kettle.core.repository.pool;

import com.smart.kettle.core.meta.EnhancedDatabaseMeta;
import com.smart.kettle.core.properties.KettleDatabaseRepositoryProperties;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;

/**
 * kettle 数据库资源库Factory
 * @author ShiZhongMing
 * 2021/7/15 11:05
 * @since 1.0
 */
public class KettleDatabaseRepositoryFactory implements KeyedPooledObjectFactory<KettleDatabaseRepositoryProperties, KettleDatabaseRepository> {
    @Override
    public PooledObject<KettleDatabaseRepository> makeObject(KettleDatabaseRepositoryProperties properties) throws Exception {
        KettleDatabaseRepository repository = new KettleDatabaseRepository();
        // 创建连接信息
        EnhancedDatabaseMeta enhancedDatabaseMeta = new EnhancedDatabaseMeta(properties.getName(), properties.getType(),
                properties.getAccess(), properties.getHost(), properties.getDb(), properties.getPort(), properties.getDbUser(), properties.getDbPassword());
        KettleDatabaseRepositoryMeta repositoryMeta = new KettleDatabaseRepositoryMeta(properties.getId(), properties.getRepositoryName(), properties.getDescription(), enhancedDatabaseMeta);
        // 初始化资源库
        repository.init(repositoryMeta);
        // 连接资源库
        repository.connect(properties.getResUser(), properties.getResPassword(), true);
        return new DefaultPooledObject<>(repository);
    }

    @Override
    public void destroyObject(KettleDatabaseRepositoryProperties kettleDatabaseRepositoryProperties, PooledObject<KettleDatabaseRepository> pooledObject) throws Exception {
        KettleDatabaseRepository repository = pooledObject.getObject();
        if (repository != null && repository.isConnected()) {
            repository.commit();
            repository.disconnect();
        }
    }

    @Override
    public boolean validateObject(KettleDatabaseRepositoryProperties kettleDatabaseRepositoryProperties, PooledObject<KettleDatabaseRepository> pooledObject) {
        KettleDatabaseRepository repository = pooledObject.getObject();
        return repository != null && repository.isConnected();
    }

    @Override
    public void activateObject(KettleDatabaseRepositoryProperties kettleDatabaseRepositoryProperties, PooledObject<KettleDatabaseRepository> pooledObject) throws Exception {
        KettleDatabaseRepository repository = pooledObject.getObject();
        if (repository != null && !repository.isConnected()) {
            repository.connect(kettleDatabaseRepositoryProperties.getResUser(), kettleDatabaseRepositoryProperties.getResPassword(), true);
        }
    }

    @Override
    public void passivateObject(KettleDatabaseRepositoryProperties kettleDatabaseRepositoryProperties, PooledObject<KettleDatabaseRepository> pooledObject) throws Exception {
        KettleDatabaseRepository repository = pooledObject.getObject();
        if (repository != null && repository.isConnected()) {
            repository.commit();
        }
    }
}
