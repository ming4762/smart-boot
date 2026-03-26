package com.smart.framework.file.extensions.disk.service;

import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.file.core.common.FileStorageServiceRegisterName;
import com.smart.framework.file.core.parameter.FileStorageDeleteParameter;
import com.smart.framework.file.core.parameter.FileStorageGetParameter;
import com.smart.framework.file.core.parameter.FileStorageInitProperties;
import com.smart.framework.file.core.parameter.FileStorageSaveParameter;
import com.smart.framework.file.core.pojo.bo.DiskFilePathBO;
import com.smart.framework.file.core.pojo.dto.FileStorageSaveResult;
import com.smart.framework.file.core.properties.SmartFileStorageDiskProperties;
import com.smart.framework.file.core.service.FileStorageService;
import com.smart.module.api.file.constants.FileStorageTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.jspecify.annotations.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhongming4762
 * 2023/2/16 22:01
 */
public class FileStorageDiskServiceImpl implements FileStorageService {

    private static final Map<Long, FileStorageDiskProperties> PROPERTIES_MAP = new ConcurrentHashMap<>();

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

    protected SmartFileStorageDiskProperties getDiskProperties(Long fileStorageId) {
        FileStorageDiskProperties fileStorageDiskProperties = PROPERTIES_MAP.get(fileStorageId);
        if (fileStorageDiskProperties == null) {
            throw new UnsupportedOperationException("未找到文件存储配置信息");
        }
        return fileStorageDiskProperties.getDiskProperties();
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
    public FileStorageSaveResult save(@NonNull InputStream inputStream, @NonNull FileStorageSaveParameter parameter) {
        SmartFileStorageDiskProperties diskProperties = this.getDiskProperties(parameter.getFileStorageId());
        DiskFilePathBO diskFilePath = new DiskFilePathBO(diskProperties.getBasePath(), parameter);
        // 获取文件路径
        final Path folderPath = Paths.get(diskFilePath.getAbsolutePath());
        if (Files.notExists(folderPath)) {
            Files.createDirectories(folderPath);
        }
        final String filePath = diskFilePath.getFilePath();
        final Path inPath = Paths.get(filePath);
        Files.copy(inputStream, inPath);
        return FileStorageSaveResult.builder()
                .fileStorageId(parameter.getFileStorageId())
                .fileStoreKey(diskFilePath.getFileId())
                .encryptedYn(PROPERTIES_MAP.get(parameter.getFileStorageId()).isEncryptedYn())
                .build();
    }

    /**
     * 删除文件
     *
     * @param parameter 删除参数
     */
    @SneakyThrows({IOException.class})
    @Override
    public void delete(@NonNull FileStorageDeleteParameter parameter) {
        SmartFileStorageDiskProperties diskProperties = this.getDiskProperties(parameter.getFileStorageId());
        for (FileStorageDeleteParameter.FileStorageDeleteItem item : parameter.getFileStoreList()) {
            String filePath = DiskFilePathBO.createById(item.getFileStoreKey(), diskProperties.getBasePath()).getFilePath();
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
        SmartFileStorageDiskProperties diskProperties = this.getDiskProperties(parameter.getFileStorageId());
        String filePath = DiskFilePathBO.createById(parameter.getStorageStoreKey(), diskProperties.getBasePath()).getFilePath();
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
        SmartFileStorageDiskProperties diskProperties = this.getDiskProperties(parameter.getFileStorageId());
        return DiskFilePathBO.createById(parameter.getStorageStoreKey(), diskProperties.getBasePath()).getFilePath();
    }

    /**
     * 初始化
     *
     * @param initProperties 初始化参数
     */
    @Override
    public void init(FileStorageInitProperties initProperties) {
        SmartFileStorageDiskProperties diskProperties = JsonUtils.parse(initProperties.getProperties(), SmartFileStorageDiskProperties.class);
        if (initProperties.isEncryptedYn()) {
            throw new UnsupportedOperationException("磁盘存储暂不支持加密");
        }
        FileStorageDiskProperties fileStorageDiskProperties = new FileStorageDiskProperties();
        BeanUtils.copyProperties(initProperties, fileStorageDiskProperties);
        fileStorageDiskProperties.setDiskProperties(diskProperties);
        PROPERTIES_MAP.put(initProperties.getFileStorageId(), fileStorageDiskProperties);
    }

    /**
     * 根据ID销毁存储器
     *
     * @param fileStorageId 存储器ID
     */
    @Override
    public void destroy(Long fileStorageId) {
        PROPERTIES_MAP.remove(fileStorageId);
    }


    @Override
    public void destroy() throws Exception {
        this.destroy(new ArrayList<>(PROPERTIES_MAP.keySet()));
    }

    @Getter
    @Setter
    private static class FileStorageDiskProperties extends FileStorageInitProperties {

       private SmartFileStorageDiskProperties diskProperties;
    }
}
