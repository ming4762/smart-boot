package com.smart.file.extensions.ftp.service;

import com.smart.commons.core.utils.JsonUtils;
import com.smart.file.core.common.FileStorageServiceRegisterName;
import com.smart.file.core.exception.SmartFileException;
import com.smart.file.core.parameter.FileStorageDeleteParameter;
import com.smart.file.core.parameter.FileStorageGetParameter;
import com.smart.file.core.parameter.FileStorageSaveParameter;
import com.smart.file.core.pojo.bo.DiskFilePathBO;
import com.smart.file.core.properties.SmartFileStorageFtpProperties;
import com.smart.file.core.service.FileStorageService;
import com.smart.file.extensions.ftp.pool.FtpClientKeyedPooledObjectFactory;
import com.smart.module.api.file.constants.FileStorageTypeEnum;
import lombok.SneakyThrows;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.springframework.lang.NonNull;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author zhongming4762
 * 2023/3/21 19:57
 */
public class FileStorageFtpServiceImpl implements FileStorageService {

    private final GenericKeyedObjectPool<SmartFileStorageFtpProperties, FTPClient> objectPool;

    public FileStorageFtpServiceImpl() {
        this.objectPool = new GenericKeyedObjectPool<>(new FtpClientKeyedPooledObjectFactory());
    }


    protected SmartFileStorageFtpProperties getProperties(String key) {
        return JsonUtils.parse(key, SmartFileStorageFtpProperties.class);
    }

    /**
     * 获取注册名字
     *
     * @return 注册名字
     */
    @Override
    public FileStorageServiceRegisterName getRegisterName() {
        return FileStorageServiceRegisterName.builder()
                .beanName(FileStorageTypeEnum.FTP.getServiceName())
                .storageType(FileStorageTypeEnum.FTP)
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
    public String save(@NonNull InputStream inputStream, @NonNull FileStorageSaveParameter parameter) {
        SmartFileStorageFtpProperties properties = this.getProperties(parameter.getStorageProperties());
        FTPClient ftpClient = this.objectPool.borrowObject(properties);
        try {
            DiskFilePathBO diskFilePath = new DiskFilePathBO(properties.getBasePath(), parameter);
            String relativePath = diskFilePath.getRelativePath();
            if (!ftpClient.changeWorkingDirectory(relativePath)) {
                ftpClient.makeDirectory(diskFilePath.getRelativePath());
            }
            ftpClient.changeWorkingDirectory(relativePath);
            boolean storeFile = ftpClient.storeFile(this.getDiskFilename(diskFilePath.getDiskFilename()), inputStream);
            if (!storeFile) {
                throw new SmartFileException("FTP文件上传失败");
            }
            return diskFilePath.getFileId();
        } finally {
            this.objectPool.returnObject(properties, ftpClient);
        }
    }

    /**
     * 删除文件
     *
     * @param parameter 删除参数
     */
    @Override
    @SneakyThrows(Exception.class)
    public void delete(@NonNull FileStorageDeleteParameter parameter) {
        SmartFileStorageFtpProperties properties = this.getProperties(parameter.getStorageProperties());
        FTPClient ftpClient = this.objectPool.borrowObject(properties);
        try {
            for (String key : parameter.getFileStoreKeyList()) {
                DiskFilePathBO diskFilePath = DiskFilePathBO.createById(key, properties.getBasePath());
                ftpClient.changeWorkingDirectory(diskFilePath.getRelativePath());
                ftpClient.deleteFile(this.getDiskFilename(diskFilePath.getDiskFilename()));
            }
        } finally {
            this.objectPool.returnObject(properties, ftpClient);
        }
    }

    /**
     * 下载文件
     *
     * @param parameter 文件下载参数
     * @return 文件流
     */
    @Override
    @SneakyThrows({IOException.class})
    public InputStream download(@NonNull FileStorageGetParameter parameter) {
        File tempFile = File.createTempFile("smart-boot", "");
        try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile)) {
            this.download(parameter, fileOutputStream);
            return new FileInputStream(tempFile);
        }
    }

    /**
     * 下载文件
     *
     * @param parameter    参数
     * @param outputStream 输出流
     */
    @Override
    @SneakyThrows(Exception.class)
    public void download(FileStorageGetParameter parameter, OutputStream outputStream) {
        SmartFileStorageFtpProperties properties = this.getProperties(parameter.getStorageProperties());
        FTPClient ftpClient = this.objectPool.borrowObject(properties);
        try {
            DiskFilePathBO diskFilePath = DiskFilePathBO.createById(parameter.getFileStorageKey(), properties.getBasePath());
            ftpClient.changeWorkingDirectory(diskFilePath.getRelativePath());
            ftpClient.retrieveFile(this.getDiskFilename(diskFilePath.getDiskFilename()), outputStream);
        } finally {
            this.objectPool.returnObject(properties, ftpClient);
        }
    }

    private String getDiskFilename(String diskFilename) {
        return new String(diskFilename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
    }

    /**
     * 获取文件访问地址
     *
     * @param parameter 参数
     * @return address
     */
    @Override
    public String getAddress(@NonNull FileStorageGetParameter parameter) {
        throw new UnsupportedOperationException("FTP存储不支持获取地址");
    }
}
