package com.smart.file.extensions.nfs.constants;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import lombok.Getter;

/**
 * Channel 类型
 * @author ShiZhongMing
 * 2020/12/7 17:43
 * @since 1.0
 */
@Getter
public enum ChannelTypeEnum {

    /**
     * Channel 类型 sftp
     */
    SFTP(ChannelSftp.class);

    private final Class<? extends Channel> channelClass;

    ChannelTypeEnum(Class<? extends Channel> channelClass) {
        this.channelClass = channelClass;
    }
}
