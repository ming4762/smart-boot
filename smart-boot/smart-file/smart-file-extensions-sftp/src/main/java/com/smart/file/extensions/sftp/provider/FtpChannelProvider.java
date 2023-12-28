package com.smart.file.extensions.sftp.provider;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.smart.file.core.exception.SmartFileException;
import com.smart.file.extensions.sftp.constants.ChannelTypeEnum;
import com.smart.file.extensions.sftp.pool.ChannelKeyedPooledObjectFactory;
import com.smart.file.extensions.sftp.pool.JschSessionKeyedPooledObjectFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
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

    private GenericKeyedObjectPool<String, ChannelSftp> objectPool;


    @Override
    public ChannelSftp getChannel(String key) {
        Integer waitersNum = this.objectPool.getNumWaitersByKey().get(key);
        if (waitersNum != null && waitersNum > 0) {
            log.warn("线程池暂无空闲channel");
            return null;
        }
        try {
            return this.objectPool.borrowObject(key);
        } catch (Exception e) {
            log.error("从连接池获取channel发生错误", e);
            throw new SmartFileException("从连接池获取channel发生错误", e);
        }
    }

    @Override
    public void returnChannel(String key, ChannelSftp channel) {
        this.objectPool.returnObject(key, channel);
    }

    @Override
    public void afterPropertiesSet() {
        // 创建session连接池
        JschSessionKeyedPooledObjectFactory sessionKeyedPooledObjectFactory = new JschSessionKeyedPooledObjectFactory();
        GenericKeyedObjectPool<String, Session> sessionGenericKeyedObjectPool = new GenericKeyedObjectPool<>(sessionKeyedPooledObjectFactory);
        // 创建channel连接池
        ChannelKeyedPooledObjectFactory<ChannelSftp> channelKeyedPooledObjectFactory = new ChannelKeyedPooledObjectFactory<>(sessionGenericKeyedObjectPool, ChannelTypeEnum.SFTP);
        this.objectPool = new GenericKeyedObjectPool<>(channelKeyedPooledObjectFactory);
    }

    /**
     * 销毁对象，关闭连接池
     */
    @Override
    public void destroy() {
        this.objectPool.close();
    }
}
