package com.smart.file.extensions.sftp.provider;

import com.jcraft.jsch.Channel;

/**
 * JcraftChannel 提供器
 * @author ShiZhongMing
 * 2020/12/7 17:55
 * @since 1.0
 */
public interface JschChannelProvider<T extends Channel> {

    /**
     * 获取Jcraft通道
     * @param key key
     * @return Jcraft通道
     */
    T getChannel(String key);

    /**
     * 归还通道
     * @param key key
     * @param channel 通道
     */
    void returnChannel(String key, T channel);
}
