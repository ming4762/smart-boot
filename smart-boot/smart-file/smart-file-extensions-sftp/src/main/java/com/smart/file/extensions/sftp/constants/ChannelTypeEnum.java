package com.smart.file.extensions.sftp.constants;

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
    SFTP("sftp", ChannelSftp.class);

    private final String type;

    private final Class<? extends Channel> channelClass;

    ChannelTypeEnum(String type, Class<? extends Channel> channelClass) {
        this.type = type;
        this.channelClass = channelClass;
    }
}
