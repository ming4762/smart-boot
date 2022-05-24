package com.smart.file.extensions.nfs.pool;

import com.jcraft.jsch.Session;
import com.smart.commons.file.SmartFileProperties;
import com.smart.file.extensions.nfs.utils.JcraftUtils;
import lombok.SneakyThrows;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.Objects;
import java.util.Optional;

/**
 * Jcraft Session连接池
 * @author ShiZhongMing
 * 2020/12/7 15:36
 * @since 1.0
 */
public class JcraftSessionPooledObjectFactory implements PooledObjectFactory<Session> {

    private final SmartFileProperties.SmartJcraftProperties properties;

    public JcraftSessionPooledObjectFactory(SmartFileProperties.SmartJcraftProperties properties) {
        this.properties = properties;
    }

    @SneakyThrows
    @Override
    public PooledObject<Session> makeObject() {
        final Session session = JcraftUtils.createSession(this.properties);
        return new DefaultPooledObject<>(session);
    }

    @Override
    public void destroyObject(PooledObject<Session> pooledObject) {
        Optional.ofNullable(pooledObject.getObject())
                .ifPresent(Session::disconnect);
    }

    @Override
    public boolean validateObject(PooledObject<Session> pooledObject) {
        final Session session = pooledObject.getObject();
        return Objects.nonNull(session) && session.isConnected();
    }

    @SneakyThrows
    @Override
    public void activateObject(PooledObject<Session> pooledObject) {
        Session object = pooledObject.getObject();
        if (Objects.nonNull(object) && !object.isConnected()) {
            object.connect();
        }
    }

    @Override
    public void passivateObject(PooledObject<Session> pooledObject) {
        Session object = pooledObject.getObject();
        if (Objects.nonNull(object) && object.isConnected()) {
            object.disconnect();
        }
    }
}
