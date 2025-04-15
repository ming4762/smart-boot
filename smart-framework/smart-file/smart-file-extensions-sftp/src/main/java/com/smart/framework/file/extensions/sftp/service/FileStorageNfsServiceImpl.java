package com.smart.framework.file.extensions.sftp.service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.file.core.common.FileStorageServiceRegisterName;
import com.smart.framework.file.core.parameter.FileStorageDeleteParameter;
import com.smart.framework.file.core.parameter.FileStorageGetParameter;
import com.smart.framework.file.core.parameter.FileStorageInitProperties;
import com.smart.framework.file.core.parameter.FileStorageSaveParameter;
import com.smart.framework.file.core.pojo.bo.DiskFilePathBO;
import com.smart.framework.file.core.pojo.dto.FileStorageSaveResult;
import com.smart.framework.file.core.properties.SmartFileStorageSftpProperties;
import com.smart.framework.file.core.service.FileStorageService;
import com.smart.framework.file.extensions.sftp.provider.JschChannelProvider;
import com.smart.framework.file.extensions.sftp.utils.JschUtils;
import com.smart.module.api.file.constants.FileStorageTypeEnum;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhongming4762
 * 2023/2/17 19:57
 */
public class FileStorageNfsServiceImpl implements FileStorageService {

    private static final Map<Long, FileStorageInitProperties> PROPERTIES_MAP = new ConcurrentHashMap<>();
    private final JschChannelProvider<ChannelSftp> channelProvider;

    public FileStorageNfsServiceImpl(JschChannelProvider<ChannelSftp> channelProvider) {
        this.channelProvider = channelProvider;
    }

    protected SmartFileStorageSftpProperties getProperties(Long id) {
        return JsonUtils.parse(PROPERTIES_MAP.get(id).getProperties(), SmartFileStorageSftpProperties.class);
    }

    /**
     * 初始化
     *
     * @param initProperties 初始化参数
     */
    @Override
    public void init(FileStorageInitProperties initProperties) {
        PROPERTIES_MAP.put(initProperties.getFileStorageId(), initProperties);
    }


    @Override
    public void destroy() {
        this.destroy(new ArrayList<>(PROPERTIES_MAP.keySet()));
    }

    /**
     * 根据ID销毁存储器
     *
     * @param fileStorageId 存储器ID
     */
    @Override
    public void destroy(Long fileStorageId) {
        FileStorageInitProperties initProperties = PROPERTIES_MAP.get(fileStorageId);
        if (initProperties == null) {
            return;
        }
        this.channelProvider.destroyByKey(initProperties.getProperties());
        PROPERTIES_MAP.remove(fileStorageId);
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
    public FileStorageSaveResult save(@NonNull InputStream inputStream, @NonNull FileStorageSaveParameter parameter) {
        FileStorageInitProperties initProperties = PROPERTIES_MAP.get(parameter.getFileStorageId());
        SmartFileStorageSftpProperties properties = this.getProperties(parameter.getFileStorageId());
        ChannelSftp channelSftp = this.channelProvider.getChannel(initProperties.getProperties());
        try {
            DiskFilePathBO diskFilePath = new DiskFilePathBO(properties.getBasePath(), parameter);
            // 创建并进入路径
            JschUtils.createDirectories(channelSftp, diskFilePath.getAbsolutePath());
            channelSftp.put(inputStream, diskFilePath.getDiskFilename());
            return FileStorageSaveResult.builder()
                   .fileStoreKey(diskFilePath.getFileId())
                    .fileStorageId(parameter.getFileStorageId())
                    .encryptedYn(initProperties.isEncryptedYn())
                   .build();
        } finally {
            this.channelProvider.returnChannel(initProperties.getProperties(), channelSftp);
        }
    }

    @Override
    @SneakyThrows(SftpException.class)
    public void delete(@NonNull FileStorageDeleteParameter parameter) {
        FileStorageInitProperties initProperties = PROPERTIES_MAP.get(parameter.getFileStorageId());
        SmartFileStorageSftpProperties properties = this.getProperties(parameter.getFileStorageId());
        ChannelSftp channelSftp = this.channelProvider.getChannel(initProperties.getProperties());
        try {
            for (FileStorageDeleteParameter.FileStorageDeleteItem item : parameter.getFileStoreList()) {
                DiskFilePathBO diskFilePath = DiskFilePathBO.createById(item.getFileStoreKey(), properties.getBasePath());
                channelSftp.cd(diskFilePath.getAbsolutePath());
                channelSftp.rm(diskFilePath.getDiskFilename());
            }
        } finally {
            this.channelProvider.returnChannel(initProperties.getProperties(), channelSftp);
        }
    }

    @SneakyThrows(SftpException.class)
    @Override
    public InputStream download(@NonNull FileStorageGetParameter parameter) {
        FileStorageInitProperties initProperties = PROPERTIES_MAP.get(parameter.getFileStorageId());
        SmartFileStorageSftpProperties properties = this.getProperties(parameter.getFileStorageId());
        ChannelSftp channelSftp = this.channelProvider.getChannel(initProperties.getProperties());
        try {
            DiskFilePathBO diskFilePath = DiskFilePathBO.createById(parameter.getStorageStoreKey(), properties.getBasePath());
            channelSftp.cd(diskFilePath.getAbsolutePath());
            return channelSftp.get(diskFilePath.getDiskFilename());
        } finally {
            this.channelProvider.returnChannel(initProperties.getProperties(), channelSftp);
        }
    }

    @SneakyThrows({SftpException.class, IOException.class})
    @Override
    public void download(@NonNull FileStorageGetParameter parameter, OutputStream outputStream) {
        FileStorageInitProperties initProperties = PROPERTIES_MAP.get(parameter.getFileStorageId());
        SmartFileStorageSftpProperties properties = this.getProperties(parameter.getFileStorageId());
        ChannelSftp channelSftp = this.channelProvider.getChannel(initProperties.getProperties());
        try {
            DiskFilePathBO diskFilePath = DiskFilePathBO.createById(parameter.getStorageStoreKey(), properties.getBasePath());
            channelSftp.cd(diskFilePath.getAbsolutePath());
            try (final InputStream inputStream = channelSftp.get(diskFilePath.getDiskFilename())) {
                IOUtils.copy(inputStream, outputStream);
            }
        } finally {
            this.channelProvider.returnChannel(initProperties.getProperties(), channelSftp);
        }
    }

    @Override
    public String getAddress(@NonNull FileStorageGetParameter parameter) {
        SmartFileStorageSftpProperties diskProperties = this.getProperties(parameter.getFileStorageId());
        return DiskFilePathBO.createById(parameter.getStorageStoreKey(), diskProperties.getBasePath()).getFilePath();
    }
}
