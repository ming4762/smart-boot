package com.smart.file.extensions.nfs.provider;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.smart.commons.core.exception.BaseException;
import com.smart.commons.file.SmartFileProperties;
import com.smart.file.extensions.nfs.constants.ChannelTypeEnum;
import com.smart.file.extensions.nfs.pool.ChannelPooledObjectFactory;
import com.smart.file.extensions.nfs.pool.JcraftSessionPooledObjectFactory;
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
public class FtpChannelProvider implements JcraftChannelProvider<ChannelSftp>, InitializingBean, DisposableBean {

    private GenericObjectPool<ChannelSftp> objectPool;

    private final SmartFileProperties.SmartJcraftProperties properties;

    public FtpChannelProvider(SmartFileProperties.SmartJcraftProperties properties) {
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
            throw new BaseException("从连接池获取channel发生错误", e);
        }
    }

    @Override
    public void returnChannel(ChannelSftp channel) {
        this.objectPool.returnObject(channel);
    }

    @Override
    public void afterPropertiesSet() {
        // 创建session连接池
        final JcraftSessionPooledObjectFactory sessionPooledObjectFactory = new JcraftSessionPooledObjectFactory(this.properties);
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
