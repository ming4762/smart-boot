package com.smart.file.extensions.sftp.pool;

import com.jcraft.jsch.Session;
import com.smart.commons.core.utils.JsonUtils;
import com.smart.file.core.properties.SmartFileStorageSftpProperties;
import com.smart.file.extensions.sftp.utils.JschUtils;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.Optional;

/**
 * JschSession连接池
 * @author zhongming4762
 * 2023/2/17
 */
public class JschSessionKeyedPooledObjectFactory implements KeyedPooledObjectFactory<String, Session> {

    protected SmartFileStorageSftpProperties getStorageProperties(String key) {
        return JsonUtils.parse(key, SmartFileStorageSftpProperties.class);
    }

    @Override
    public void activateObject(String key, PooledObject<Session> pooledObject) throws Exception {
        Session session = pooledObject.getObject();
        if (session != null && !session.isConnected()) {
            session.connect();
        }
    }

    @Override
    public void destroyObject(String key, PooledObject<Session> pooledObject) {
        Optional.ofNullable(pooledObject.getObject())
                .ifPresent(Session::disconnect);
    }

    @Override
    public PooledObject<Session> makeObject(String key) throws Exception {
        Session session = JschUtils.createSession(this.getStorageProperties(key));
        return new DefaultPooledObject<>(session);
    }

    @Override
    public void passivateObject(String key, PooledObject<Session> p) {
        // do nothing
    }

    @Override
    public boolean validateObject(String key, PooledObject<Session> p) {
        return false;
    }
}
