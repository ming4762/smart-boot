package com.smart.file.extensions.sftp.provider;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.smart.file.core.SmartFileProperties;
import com.smart.file.core.exception.SmartFileException;
import com.smart.file.extensions.sftp.constants.ChannelTypeEnum;
import com.smart.file.extensions.sftp.pool.ChannelPooledObjectFactory;
import com.smart.file.extensions.sftp.pool.JschSessionPooledObjectFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * FTP通道提供者
 * @author ShiZhongMing
 * 2020/12/7 17:57
 * @since 1.0
 */
@Slf4j
public class FtpChannelProvider implements JschChannelProvider<ChannelSftp>, InitializingBean, DisposableBean {

    private GenericObjectPool<ChannelSftp> objectPool;

    private final SmartFileProperties.SmartJschProperties properties;

    public FtpChannelProvider(SmartFileProperties.SmartJschProperties properties) {
        this.properties = properties;
    }

    @Override
    public ChannelSftp getChannel() {
        if (this.objectPool.getNumWaiters() > 0) {
            log.warn("线程池暂无空闲channel");
            return null;
        }
        try {
            return this.objectPool.borrowObject();
        } catch (Exception e) {
            log.error("从连接池获取channel发生错误", e);
            throw new SmartFileException("从连接池获取channel发生错误", e);
        }
    }

    @Override
    public void returnChannel(ChannelSftp channel) {
        this.objectPool.returnObject(channel);
    }

    @Override
    public void afterPropertiesSet() {
        // 创建session连接池
        final JschSessionPooledObjectFactory sessionPooledObjectFactory = new JschSessionPooledObjectFactory(this.properties);
        final GenericObjectPool<Session> sessionObjectPool = new GenericObjectPool<>(sessionPooledObjectFactory);
        // 创建channel连接池
        final ChannelPooledObjectFactory<ChannelSftp> channelPooledObjectFactory = new ChannelPooledObjectFactory<>(sessionObjectPool, ChannelTypeEnum.SFTP);
        this.objectPool = new GenericObjectPool<>(channelPooledObjectFactory);
    }

    /**
     * 销毁对象，关闭连接池
     */
    @Override
    public void destroy() {
        this.objectPool.close();
    }
}
