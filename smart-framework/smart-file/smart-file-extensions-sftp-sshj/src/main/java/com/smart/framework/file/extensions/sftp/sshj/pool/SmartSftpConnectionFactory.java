package com.smart.framework.file.extensions.sftp.sshj.pool;

import com.smart.framework.file.core.properties.SmartFileStorageSftpSshjProperties;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.DestroyMode;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * 连接工厂
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-21  18:40
 * @since 5.0.0
 */
public class SmartSftpConnectionFactory extends BaseKeyedPooledObjectFactory<SmartFileStorageSftpSshjProperties, SmartSftpConnection> {

    @Override
    public void destroyObject(SmartFileStorageSftpSshjProperties key, PooledObject<SmartSftpConnection> p, DestroyMode destroyMode) throws Exception {
        p.getObject().close();
    }

    @Override
    public boolean validateObject(SmartFileStorageSftpSshjProperties key, PooledObject<SmartSftpConnection> p) {
        return p.getObject().isConnected();
    }

    @Override
    public SmartSftpConnection create(SmartFileStorageSftpSshjProperties key) throws Exception {
        return new SmartSftpConnection(key);
    }

    @Override
    public PooledObject<SmartSftpConnection> wrap(SmartSftpConnection value) {
        return new DefaultPooledObject<>(value);
    }
}
