package com.smart.file.extensions.sftp.service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.smart.commons.core.utils.JsonUtils;
import com.smart.file.core.common.FileStorageServiceRegisterName;
import com.smart.file.core.constants.FileStorageTypeEnum;
import com.smart.file.core.parameter.FileStorageDeleteParameter;
import com.smart.file.core.parameter.FileStorageGetParameter;
import com.smart.file.core.parameter.FileStorageSaveParameter;
import com.smart.file.core.pojo.bo.DiskFilePathBO;
import com.smart.file.core.properties.SmartFileStorageSftpProperties;
import com.smart.file.core.service.FileStorageService;
import com.smart.file.extensions.sftp.provider.JschChannelProvider;
import com.smart.file.extensions.sftp.utils.JschUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author zhongming4762
 * 2023/2/17 19:57
 */
public class FileStorageNfsServiceImpl implements FileStorageService {

    private final JschChannelProvider<ChannelSftp> channelProvider;

    public FileStorageNfsServiceImpl(JschChannelProvider<ChannelSftp> channelProvider) {
        this.channelProvider = channelProvider;
    }

    protected SmartFileStorageSftpProperties getProperties(String key) {
        return JsonUtils.parse(key, SmartFileStorageSftpProperties.class);
    }

    @Override
    public FileStorageServiceRegisterName getRegisterName() {
        return FileStorageServiceRegisterName.builder()
                .beanName(FileStorageTypeEnum.SFTP.getServiceName())
                .storageType(FileStorageTypeEnum.SFTP)
                .build();
    }

    @SneakyThrows(SftpException.class)
    @Override
    public String save(@NonNull InputStream inputStream, @NonNull FileStorageSaveParameter parameter) {
        SmartFileStorageSftpProperties properties = this.getProperties(parameter.getStorageProperties());
        ChannelSftp channelSftp = this.channelProvider.getChannel(parameter.getStorageProperties());
        try {
            DiskFilePathBO diskFilePath = new DiskFilePathBO(properties.getBasePath(), parameter);
            // 创建并进入路径
            JschUtils.createDirectories(channelSftp, diskFilePath.getFolderPath());
            channelSftp.put(inputStream, diskFilePath.getDiskFilename());
            return diskFilePath.getFileId();
        } finally {
            this.channelProvider.returnChannel(parameter.getStorageProperties(), channelSftp);
        }
    }

    @Override
    @SneakyThrows(SftpException.class)
    public void delete(@NonNull FileStorageDeleteParameter parameter) {
        SmartFileStorageSftpProperties properties = this.getProperties(parameter.getStorageProperties());
        ChannelSftp channelSftp = this.channelProvider.getChannel(parameter.getStorageProperties());
        try {
            for (String key : parameter.getFileStoreKeyList()) {
                DiskFilePathBO diskFilePath = DiskFilePathBO.createById(key, properties.getBasePath());
                channelSftp.cd(diskFilePath.getFolderPath());
                channelSftp.rm(diskFilePath.getDiskFilename());
            }
        } finally {
            this.channelProvider.returnChannel(parameter.getStorageProperties(), channelSftp);
        }
    }

    @SneakyThrows(SftpException.class)
    @Override
    public InputStream download(@NonNull FileStorageGetParameter parameter) {
//        throw new UnsupportedOperationException("NFS方式不支持该方式下载文件，请通过download(String id, OutputStream outputStream)下载文件");
        SmartFileStorageSftpProperties properties = this.getProperties(parameter.getStorageProperties());
        ChannelSftp channelSftp = this.channelProvider.getChannel(parameter.getStorageProperties());
        try {
            DiskFilePathBO diskFilePath = DiskFilePathBO.createById(parameter.getFileStorageKey(), properties.getBasePath());
            channelSftp.cd(diskFilePath.getFolderPath());
            return channelSftp.get(diskFilePath.getDiskFilename());
        } finally {
            this.channelProvider.returnChannel(parameter.getStorageProperties(), channelSftp);
        }
    }

    @SneakyThrows({SftpException.class, IOException.class})
    @Override
    public void download(@NonNull FileStorageGetParameter parameter, OutputStream outputStream) {
        SmartFileStorageSftpProperties properties = this.getProperties(parameter.getStorageProperties());
        ChannelSftp channelSftp = this.channelProvider.getChannel(parameter.getStorageProperties());
        try {
            DiskFilePathBO diskFilePath = DiskFilePathBO.createById(parameter.getFileStorageKey(), properties.getBasePath());
            channelSftp.cd(diskFilePath.getFolderPath());
            try (final InputStream inputStream = channelSftp.get(diskFilePath.getDiskFilename())) {
                IOUtils.copy(inputStream, outputStream);
            }
        } finally {
            this.channelProvider.returnChannel(parameter.getStorageProperties(), channelSftp);
        }
    }

    @Override
    public String getAddress(@NonNull FileStorageGetParameter parameter) {
        SmartFileStorageSftpProperties diskProperties = this.getProperties(parameter.getStorageProperties());
        return DiskFilePathBO.createById(parameter.getFileStorageKey(), diskProperties.getBasePath()).getFilePath();
    }
}
