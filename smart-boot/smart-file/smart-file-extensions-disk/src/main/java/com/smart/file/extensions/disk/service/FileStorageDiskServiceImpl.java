package com.smart.file.extensions.disk.service;

import com.smart.commons.core.utils.JsonUtils;
import com.smart.file.core.common.FileStorageServiceRegisterName;
import com.smart.file.core.constants.FileStorageTypeEnum;
import com.smart.file.core.parameter.FileStorageCommonParameter;
import com.smart.file.core.parameter.FileStorageDeleteParameter;
import com.smart.file.core.parameter.FileStorageGetParameter;
import com.smart.file.core.parameter.FileStorageSaveParameter;
import com.smart.file.core.pojo.bo.DiskFilePathBO;
import com.smart.file.core.properties.SmartFileStorageDiskProperties;
import com.smart.file.core.service.FileStorageService;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author zhongming4762
 * 2023/2/16 22:01
 */
public class FileStorageDiskServiceImpl implements FileStorageService {
    /**
     * 获取注册名字
     *
     * @return 注册名字
     */
    @Override
    public FileStorageServiceRegisterName getRegisterName() {
        return FileStorageServiceRegisterName.builder()
                .storageType(FileStorageTypeEnum.DISK)
                .beanName(FileStorageTypeEnum.DISK.getServiceName())
                .build();
    }

    protected SmartFileStorageDiskProperties getDiskProperties(FileStorageCommonParameter parameter) {
        return JsonUtils.parse(parameter.getStorageProperties(), SmartFileStorageDiskProperties.class);
    }

    /**
     * 保存文件
     *
     * @param inputStream 输入流
     * @param parameter   参数
     * @return 文件存储标识
     */
    @SneakyThrows({IOException.class})
    @Override
    public String save(@NonNull InputStream inputStream, @NonNull FileStorageSaveParameter parameter) {
        SmartFileStorageDiskProperties diskProperties = this.getDiskProperties(parameter);
        DiskFilePathBO diskFilePath = new DiskFilePathBO(diskProperties.getBasePath(), parameter);
        // 获取文件路径
        final Path folderPath = Paths.get(diskFilePath.getFolderPath());
        if (Files.notExists(folderPath)) {
            Files.createDirectories(folderPath);
        }
        final String filePath = diskFilePath.getFilePath();
        final Path inPath = Paths.get(filePath);
        Files.copy(inputStream, inPath);
        return diskFilePath.getFileId();
    }

    /**
     * 删除文件
     *
     * @param parameter 删除参数
     */
    @SneakyThrows({IOException.class})
    @Override
    public void delete(@NonNull FileStorageDeleteParameter parameter) {
        SmartFileStorageDiskProperties diskProperties = this.getDiskProperties(parameter);
        for (String key : parameter.getFileStoreKeyList()) {
            String filePath = DiskFilePathBO.createById(key, diskProperties.getBasePath()).getFilePath();
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                Files.delete(path);
            }
        }
    }

    /**
     * 下载文件
     *
     * @param parameter 文件下载参数
     * @return 文件流
     */
    @SneakyThrows({IOException.class})
    @Override
    public InputStream download(@NonNull FileStorageGetParameter parameter) {
        SmartFileStorageDiskProperties diskProperties = this.getDiskProperties(parameter);
        String filePath = DiskFilePathBO.createById(parameter.getFileStorageKey(), diskProperties.getBasePath()).getFilePath();
        File file = new File(filePath);
        return Files.newInputStream(file.toPath());
    }

    /**
     * 获取文件访问地址
     *
     * @param parameter 参数
     * @return address
     */
    @Override
    public String getAddress(@NonNull FileStorageGetParameter parameter) {
        SmartFileStorageDiskProperties diskProperties = this.getDiskProperties(parameter);
        return DiskFilePathBO.createById(parameter.getFileStorageKey(), diskProperties.getBasePath()).getFilePath();
    }
}
