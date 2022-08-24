package com.smart.file.extensions.sftp.utils;

import com.jcraft.jsch.*;
import com.smart.file.core.SmartFileProperties;
import com.smart.file.extensions.sftp.exception.SftpExceptionRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author ShiZhongMing
 * 2020/12/7 15:26
 * @since 1.0
 */
public class JschUtils {

    private JschUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static final String PATH_SPLIT = "/";

    private static final String NO_SUCH_FILE = "no such file";

    /**
     * 创建session
     * @param properties 参数
     * @return session
     */
    public static Session createSession(SmartFileProperties.SmartJschProperties properties) throws JSchException {
        final JSch jSch = new JSch();
        if (StringUtils.isNotBlank(properties.getPrivateKey())) {
            jSch.addIdentity(properties.getPrivateKey());
        }
        // 创建session
        final Session session = jSch.getSession(properties.getUsername(), properties.getHost(), properties.getPort());
        // todo: 用户信息
        if (StringUtils.isNotBlank(properties.getPassword())) {
            session.setPassword(properties.getPassword());
        }
        // 创建配置
        final Properties config = new Properties();
        // TODO: 待处理
        config.put("StrictHostKeyChecking", "no");
        config.put("PreferredAuthentications", "publickey,keyboard-interactive,password");

        session.setConfig(config);
        session.connect();
        return session;
    }

    /**
     * 创建路径
     * @param channelSftp sftp通道
     * @param path 路径
     * @return 是否创建成功
     */
    public static boolean createDirectories(@NonNull ChannelSftp channelSftp, @NonNull String path) throws SftpException {
        if (JschUtils.isDriExist(channelSftp, path)) {
            channelSftp.cd(path);
            return true;
        }
        // 获取路径列表
        final List<String> paths = Arrays.stream(path.split(PATH_SPLIT))
                .toList();
        return JschUtils.createDirectories(channelSftp, paths);
    }

    /**
     * 创建路径
     * @param channelSftp sftp通道
     * @param paths 路径列表
     * @throws SftpException SftpException
     */
    public static boolean createDirectories(@NonNull ChannelSftp channelSftp, @NonNull List<String> paths) throws SftpException {
        // 过滤路径
        final List<String> pathList = paths.stream().filter(StringUtils :: isNotBlank).toList();
        StringBuilder filePath = new StringBuilder(PATH_SPLIT);
        for (String path : pathList) {
            filePath.append(path).append(PATH_SPLIT);
            if (!JschUtils.isDriExist(channelSftp, filePath.toString())) {
                // 创建目录
                channelSftp.mkdir(filePath.toString());
            }
        }
        channelSftp.cd(filePath.toString());
        return true;
    }

    /**
     * 判断目录是否存在
     * @param directory 目录
     * @return 目录是否存在
     */
    public static boolean isDriExist(@NonNull ChannelSftp channelSftp, @NonNull String directory) {
        try {
            final SftpATTRS sftpAttrs = channelSftp.lstat(directory);
            return sftpAttrs.isDir();
        } catch (SftpException e) {
            if (!StringUtils.equals(e.getMessage().toLowerCase(), NO_SUCH_FILE)) {
                throw new SftpExceptionRuntimeException(e);
            } else {
                return false;
            }
        }
    }
}
