package com.smart.file.extensions.sftp.pool;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;
import com.smart.file.extensions.sftp.constants.ChannelTypeEnum;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;

/**
 * Jcraft channel连接池
 * @author zhongming4762
 * 2023/2/17
 */
public class ChannelKeyedPooledObjectFactory<T extends Channel> implements KeyedPooledObjectFactory<String, T> {

    private final GenericKeyedObjectPool<String, Session> sessionGenericKeyedObjectPool;

    private final ChannelTypeEnum type;

    public ChannelKeyedPooledObjectFactory(GenericKeyedObjectPool<String, Session> sessionGenericKeyedObjectPool, ChannelTypeEnum type) {
        this.sessionGenericKeyedObjectPool = sessionGenericKeyedObjectPool;
        this.type = type;
    }

    @Override
    public void activateObject(String key, PooledObject<T> pooledObject) throws Exception {
        T object = pooledObject.getObject();
        if (object != null && !object.isConnected()) {
            Session session = object.getSession();
            if (!session.isConnected()) {
                session.connect();
            }
            object.connect();
        }
    }

    @Override
    public void destroyObject(String key, PooledObject<T> pooledObject) throws Exception {
        T object = pooledObject.getObject();
        if (object != null) {
            object.disconnect();
            object.getSession().disconnect();
        }
    }

    @Override
    public PooledObject<T> makeObject(String key) throws Exception {
        Session session = this.sessionGenericKeyedObjectPool.borrowObject(key);
        T channel = (T) session.openChannel(this.type.name());
        return new DefaultPooledObject<>(channel);
    }

    @Override
    public void passivateObject(String key, PooledObject<T> p) throws Exception {
        // do nothing
    }

    @Override
    public boolean validateObject(String key, PooledObject<T> pooledObject) {
        T object = pooledObject.getObject();
        return object != null && object.isConnected();
    }
}
