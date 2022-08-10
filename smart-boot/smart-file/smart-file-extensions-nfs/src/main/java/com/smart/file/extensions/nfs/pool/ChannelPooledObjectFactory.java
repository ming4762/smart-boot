package com.smart.file.extensions.nfs.pool;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.smart.file.extensions.nfs.constants.ChannelTypeEnum;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.Objects;

/**
 * Jcraft channel连接池
 * @author ShiZhongMing
 * 2020/12/7 17:26
 * @since 1.0
 */
public class ChannelPooledObjectFactory<T extends Channel> implements PooledObjectFactory<T> {

    private final GenericObjectPool<Session> sessionGenericObjectPool;

    private final ChannelTypeEnum type;


    public ChannelPooledObjectFactory(GenericObjectPool<Session> sessionGenericObjectPool, ChannelTypeEnum type) {
        this.sessionGenericObjectPool = sessionGenericObjectPool;
        this.type = type;
    }

    @Override
    public PooledObject<T> makeObject() throws Exception {
        final Session session = this.sessionGenericObjectPool.borrowObject();
        T channel = (T) session.openChannel(type.name());
        return new DefaultPooledObject<>(channel);
    }


    @Override
    public void destroyObject(PooledObject<T> pooledObject) throws Exception {
        T object = pooledObject.getObject();
        if (Objects.nonNull(object)) {
            object.disconnect();
            object.getSession().disconnect();
        }
    }

    @Override
    public boolean validateObject(PooledObject<T> pooledObject) {
        final Channel channel = pooledObject.getObject();
        return (Objects.nonNull(channel)) && channel.isConnected();
    }

    @Override
    public void activateObject(PooledObject<T> pooledObject) throws JSchException {
        final Channel object = pooledObject.getObject();
        if (Objects.nonNull(object) && !object.isConnected()) {
            // 获取session，并且连接session
            final Session session = object.getSession();
            if (!session.isConnected()) {
                session.connect();
            }
            object.connect();
        }
    }

    @Override
    public void passivateObject(PooledObject<T> pooledObject) throws JSchException {
        final Channel object = pooledObject.getObject();
        if (Objects.nonNull(object) && object.isConnected()) {
            final Session session = object.getSession();
            if (session.isConnected()) {
                session.disconnect();
            }
            object.disconnect();
        }
    }
}
