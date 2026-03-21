package com.smart.framework.file.extensions.sftp.sshj.service;

import com.smart.framework.commons.core.file.AutoDeleteFileInputStream;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.file.core.common.FileStorageServiceRegisterName;
import com.smart.framework.file.core.parameter.FileStorageDeleteParameter;
import com.smart.framework.file.core.parameter.FileStorageGetParameter;
import com.smart.framework.file.core.parameter.FileStorageInitProperties;
import com.smart.framework.file.core.parameter.FileStorageSaveParameter;
import com.smart.framework.file.core.pojo.bo.DiskFilePathBO;
import com.smart.framework.file.core.pojo.dto.FileStorageSaveResult;
import com.smart.framework.file.core.properties.SmartFileStorageSftpSshjProperties;
import com.smart.framework.file.core.service.FileStorageService;
import com.smart.framework.file.extensions.sftp.sshj.pool.SmartSftpConnection;
import com.smart.framework.file.extensions.sftp.sshj.pool.SmartSftpConnectionFactory;
import com.smart.module.api.file.constants.FileStorageTypeEnum;
import lombok.NonNull;
import lombok.SneakyThrows;
import net.schmizz.sshj.xfer.FileSystemFile;
import net.schmizz.sshj.xfer.InMemorySourceFile;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

import java.io.File;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SFTP文件存储服务实现类
 * 基于sshj的SFTP文件存储服务实现类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-21 19:31
 * @since 5.0.0
 */
public class FileStorageSftpSshjServiceImpl implements FileStorageService {

    private static final Map<Long, FileStorageInitProperties> PROPERTIES_MAP = new ConcurrentHashMap<>();
    private final GenericKeyedObjectPool<SmartFileStorageSftpSshjProperties, SmartSftpConnection> objectPool;

    public FileStorageSftpSshjServiceImpl() {
        // todo: 配置项
        GenericKeyedObjectPoolConfig<SmartSftpConnection> poolConfig = new GenericKeyedObjectPoolConfig<>();
        poolConfig.setMaxTotalPerKey(50);
        poolConfig.setMaxIdlePerKey(10);
        poolConfig.setMinIdlePerKey(5);
        poolConfig.setMaxWait(Duration.ofSeconds(60));

        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        this.objectPool = new GenericKeyedObjectPool<>(new SmartSftpConnectionFactory(), poolConfig);
    }

    /**
     * 获取注册名字
     *
     * @return 注册名字
     */
    @Override
    public FileStorageServiceRegisterName getRegisterName() {
        return FileStorageServiceRegisterName.builder()
                .beanName(FileStorageTypeEnum.SFTP_SSHJ.getServiceName())
                .storageType(FileStorageTypeEnum.SFTP_SSHJ)
                .build();
    }

    /**
     * 保存文件
     *
     * @param inputStream 输入流
     * @param parameter   参数
     * @return 文件存储标识
     */
    @Override
    @SneakyThrows(Exception.class)
    public FileStorageSaveResult save(@NonNull InputStream inputStream, @NonNull FileStorageSaveParameter parameter) {
        FileStorageInitProperties initProperties = PROPERTIES_MAP.get(parameter.getFileStorageId());
        SmartFileStorageSftpSshjProperties properties = this.getProperties(parameter.getFileStorageId());
        SmartSftpConnection sftpConnection = this.objectPool.borrowObject(properties);
        try {
            DiskFilePathBO diskFilePath = new DiskFilePathBO(properties.getBasePath(), parameter);
            sftpConnection.getSftp().put(
                    new InMemorySourceFile() {
                        @Override
                        public String getName() {
                            return diskFilePath.getFilename();
                        }

                        @Override
                        public long getLength() {
                            return -1;
                        }

                        @Override
                        public InputStream getInputStream() {
                            return inputStream;
                        }
                    },
                    diskFilePath.getFilePath()
            );
            return FileStorageSaveResult.builder()
                    .fileStoreKey(diskFilePath.getFileId())
                    .fileStorageId(parameter.getFileStorageId())
                    .encryptedYn(initProperties.isEncryptedYn())
                    .build();
        } finally {
            this.objectPool.returnObject(properties, sftpConnection);
        }
    }

    /**
     * 删除文件
     *
     * @param parameter 删除参数
     */
    @SneakyThrows(Exception.class)
    @Override
    public void delete(@NonNull FileStorageDeleteParameter parameter) {
        SmartFileStorageSftpSshjProperties properties = this.getProperties(parameter.getFileStorageId());
        SmartSftpConnection sftpConnection = this.objectPool.borrowObject(properties);
        try {
            for (FileStorageDeleteParameter.FileStorageDeleteItem item : parameter.getFileStoreList()) {
                DiskFilePathBO diskFilePath = DiskFilePathBO.createById(item.getFileStoreKey(), properties.getBasePath());
                sftpConnection.getSftp().rm(diskFilePath.getFilePath());
            }
        } finally {
            this.objectPool.returnObject(properties, sftpConnection);
        }
    }

    /**
     * 下载文件
     *
     * @param parameter 文件下载参数
     * @return 文件流
     */
    @SneakyThrows(Exception.class)
    @Override
    public InputStream download(@NonNull FileStorageGetParameter parameter) {
        SmartFileStorageSftpSshjProperties properties = this.getProperties(parameter.getFileStorageId());
        SmartSftpConnection sftpConnection = this.objectPool.borrowObject(properties);
        try {

            DiskFilePathBO diskFilePath = DiskFilePathBO.createById(parameter.getStorageStoreKey(), properties.getBasePath());
            File tempFile = File.createTempFile("smart-boot", "");
            FileSystemFile fileSystemFile = new FileSystemFile(tempFile);
            sftpConnection.getSftp().get(diskFilePath.getFilePath(), fileSystemFile);

            return new AutoDeleteFileInputStream(tempFile);
        } finally {
            this.objectPool.returnObject(properties, sftpConnection);
        }
    }

    /**
     * 获取文件访问地址
     *
     * @param parameter 参数
     * @return address
     */
    @Override
    public String getAddress(@NonNull FileStorageGetParameter parameter) {
        throw new UnsupportedOperationException("SFTP存储不支持获取地址");
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
        this.objectPool.clear(this.getProperties(fileStorageId));
        PROPERTIES_MAP.remove(fileStorageId);
    }

    private SmartFileStorageSftpSshjProperties getProperties(Long fileStorageId) {
        FileStorageInitProperties initProperties = PROPERTIES_MAP.get(fileStorageId);
        if (initProperties == null) {
            throw new IllegalArgumentException("Invalid file storage ID: " + fileStorageId);
        }
        return JsonUtils.parse(initProperties.getProperties(), SmartFileStorageSftpSshjProperties.class);
    }

    @Override
    public void destroy() {
        this.destroy(new ArrayList<>(PROPERTIES_MAP.keySet()));
    }
}
