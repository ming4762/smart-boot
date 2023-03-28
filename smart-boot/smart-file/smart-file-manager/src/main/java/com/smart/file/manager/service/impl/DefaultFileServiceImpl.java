package com.smart.file.manager.service.impl;

import com.google.common.collect.ImmutableList;
import com.smart.file.core.exception.SmartFileException;
import com.smart.file.core.parameter.FileStorageDeleteParameter;
import com.smart.file.core.parameter.FileStorageGetParameter;
import com.smart.file.core.parameter.FileStorageSaveParameter;
import com.smart.file.core.service.FileService;
import com.smart.file.core.service.FileStorageService;
import com.smart.file.manager.model.SmartFilePO;
import com.smart.file.manager.model.SmartFileStoragePO;
import com.smart.file.manager.pojo.bo.SysFileBO;
import com.smart.file.manager.service.SmartFileService;
import com.smart.file.manager.service.SmartFileStorageService;
import com.smart.module.api.file.bo.FileDownloadResult;
import com.smart.module.api.file.bo.FileHandlerResult;
import com.smart.module.api.file.constants.FileStorageTypeEnum;
import com.smart.module.api.file.dto.FileSaveParameter;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 默认的文件存储器
 * @author zhongming4762
 * 2023/2/16
 */
public class DefaultFileServiceImpl implements FileService, ApplicationContextAware {

    private Map<FileStorageTypeEnum, FileStorageService> actualFileServiceMap;

    private final SmartFileStorageService smartFileStorageService;

    private final SmartFileService sysFileService;

    public DefaultFileServiceImpl(SmartFileStorageService fileStorageService, SmartFileService sysFileService) {
        this.smartFileStorageService = fileStorageService;
        this.sysFileService = sysFileService;
    }

    protected FileStorageSaveParameter getFileSaveStorageParameter(FileSaveParameter parameter) {
        // 获取文件存储器
        SmartFileStoragePO smartFileStorage;
        if (parameter.getFileStorageId() != null) {
            smartFileStorage = this.smartFileStorageService.getById(parameter.getFileStorageId());
        } else if (StringUtils.hasText(parameter.getFileStorageCode())) {
            smartFileStorage = this.smartFileStorageService.getDefault();
        } else {
            smartFileStorage = this.smartFileStorageService.getByCode(parameter.getFileStorageCode());
        }
        if (smartFileStorage == null) {
            throw new SmartFileException(String.format("获取文件存储器失败，请检查是否存在对应的文件存储器，存储器编码：%s", parameter.getFileStorageCode()));
        }
        return FileStorageSaveParameter.builder()
                .filename(parameter.getFilename())
                .folder(parameter.getFolder())
                .storageType(smartFileStorage.getStorageType())
                .storageProperties(smartFileStorage.getStorageConfig())
                .fileStorageId(smartFileStorage.getId())
                .build();
    }

    /**
     * 保存文件
     *
     * @param file      文件
     * @param parameter 保存参数
     * @return 文件ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileHandlerResult save(@NonNull MultipartFile file, FileSaveParameter parameter) {
        return this.saveFile(this.createSysFileBo(file, parameter));
    }

    /**
     * 保存文件
     *
     * @param inputStream 输入流
     * @param parameter   保存参数
     * @return 文件ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileHandlerResult save(@NonNull InputStream inputStream, FileSaveParameter parameter) {
        return this.saveFile(new SysFileBO(inputStream, parameter, null));
    }

    /**
     * 保存文件
     *
     * @param file      文件
     * @param parameter 保存参数
     * @return 文件ID
     */
    @SneakyThrows({IOException.class})
    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileHandlerResult save(@NonNull File file, FileSaveParameter parameter) {
        SysFileBO fileBo = new SysFileBO(Files.newInputStream(file.toPath()), parameter, null);
        if (!StringUtils.hasText(fileBo.getFile().getFilename())) {
            fileBo.getFile().setFilename(file.getName());
        }
        return this.saveFile(fileBo);
    }

    /**
     * 批量删除文件信息
     *
     * @param fileIds 文件ID列表
     * @return 删除结果
     */
    @Override
    public List<FileHandlerResult> batchDelete(@NonNull Collection<Long> fileIds) {
        List<SmartFilePO> sysFileList = this.sysFileService.listByIds(fileIds);
        if (CollectionUtils.isEmpty(sysFileList)) {
            return Collections.emptyList();
        }
        // 获取文件存储器
        Set<Long> fileStorageIds = sysFileList.stream().map(SmartFilePO::getFileStorageId).collect(Collectors.toSet());
        List<SmartFileStoragePO> fileStorageList = this.smartFileStorageService.listByIds(fileStorageIds);
        // 验证文件存储器是否缺少
        Set<Long> validateFileStorageIds = fileStorageList.stream().map(SmartFileStoragePO::getId).collect(Collectors.toSet());
        if (fileStorageIds.size() > validateFileStorageIds.size()) {
            throw new SmartFileException("缺少文件存储器信息");
        }
        // 删除文件信息
        this.sysFileService.removeByIds(fileIds);
        // 将文件存储器转为map
        Map<Long, SmartFileStoragePO> smartFileStorageMap = fileStorageList.stream().collect(Collectors.toMap(SmartFileStoragePO::getId, item -> item));
        // 删除实际文件
        sysFileList.stream()
                .collect(Collectors.groupingBy(SmartFilePO::getFileStorageId))
                .forEach((storageId, list) -> {
                    SmartFileStoragePO fileStorage = smartFileStorageMap.get(storageId);
                    FileStorageService fileStorageService = this.getFileStorageService(fileStorage.getStorageType());
                    fileStorageService.delete(
                            FileStorageDeleteParameter.builder()
                                    .storageProperties(fileStorage.getStorageConfig())
                                    .storageType(fileStorage.getStorageType())
                                    .fileStoreKeyList(list.stream().map(SmartFilePO::getStorageStoreKey).collect(Collectors.toList()))
                                    .build()
                    );
                });

        return sysFileList.stream().map(item -> {
            FileHandlerResult result = new FileHandlerResult();
            BeanUtils.copyProperties(item, result);
            return result;
        }).collect(Collectors.toList());
    }

    /**
     * 下载文件
     *
     * @param id 文件ID
     * @return 下载内容
     */
    @Override
    public FileDownloadResult download(@NonNull Long id) {
        SmartFilePO sysFileData = this.sysFileService.getById(id);
        if (sysFileData == null) {
            return null;
        }
        SmartFileStoragePO fileStorageData = this.smartFileStorageService.getById(sysFileData.getFileStorageId());
        if (fileStorageData == null) {
            throw new SmartFileException("未找到文件存储器");
        }
        FileStorageService fileStorageService = this.getFileStorageService(fileStorageData.getStorageType());
        InputStream inputStream = fileStorageService.download(
                FileStorageGetParameter.builder()
                        .storageType(fileStorageData.getStorageType())
                        .storageProperties(fileStorageData.getStorageConfig())
                        .fileStorageKey(sysFileData.getStorageStoreKey())
                        .build()
        );
        if (inputStream == null) {
            throw new SmartFileException("下载文件失败，未找到文件，文件可能损坏或丢失");
        }
        FileDownloadResult result = new FileDownloadResult();
        BeanUtils.copyProperties(sysFileData, result);
        result.setInputStream(inputStream);
        return result;
    }

    protected FileHandlerResult saveFile(@NonNull SysFileBO file) {
        FileStorageSaveParameter fileSaveStorageParameter = this.getFileSaveStorageParameter(file.getParameter());
        if (!StringUtils.hasText(fileSaveStorageParameter.getFilename())) {
            fileSaveStorageParameter.setFilename(file.getFile().getFilename());
        }
        file.getFile().setFileStorageId(fileSaveStorageParameter.getFileStorageId());
        // 获取文件存储器
        FileStorageService fileStorageService = this.getFileStorageService(fileSaveStorageParameter.getStorageType());
        // 保存文件
        String storageStoreKey = null;
        try (InputStream inputStream = file.getInputStream()) {
            storageStoreKey = fileStorageService.save(inputStream, fileSaveStorageParameter);
            file.getFile().setStorageStoreKey(storageStoreKey);
            this.sysFileService.save(file.getFile());
            FileHandlerResult fileSaveResult = new FileHandlerResult();
            BeanUtils.copyProperties(file.getFile(), fileSaveResult);
            return fileSaveResult;
        } catch (Exception e) {
            if (storageStoreKey != null) {
                fileStorageService.delete(
                        FileStorageDeleteParameter.builder()
                                .fileStoreKeyList(ImmutableList.of(storageStoreKey))
                                .storageType(fileSaveStorageParameter.getStorageType())
                                .storageProperties(fileSaveStorageParameter.getStorageProperties())
                                .build()
                );
            }
            throw new SmartFileException(e);
        }
    }

    @SneakyThrows(IOException.class)
    protected SysFileBO createSysFileBo(MultipartFile file, FileSaveParameter parameter) {
        SysFileBO sysFileBo = new SysFileBO(file.getInputStream(), parameter, file.getContentType());
        if (!StringUtils.hasText(sysFileBo.getFile().getFilename())) {
            sysFileBo.getFile().setFilename(file.getOriginalFilename());
        }
        sysFileBo.getFile().setFileSize(file.getSize());
        return sysFileBo;
    }

    /**
     * 获取文件存储器服务
     * @param fileStorageType  文件存储器类型
     * @return 文件存储器服务
     */
    protected FileStorageService getFileStorageService(FileStorageTypeEnum fileStorageType) {
        FileStorageService fileStorageService = this.actualFileServiceMap.get(fileStorageType);
        if (fileStorageService == null) {
            throw new SmartFileException(String.format("获取文件存储器失败，未找到对应的文件执行器，执行器名称：%s", fileStorageType.name()));
        }
        return fileStorageService;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.actualFileServiceMap = applicationContext.getBeansOfType(FileStorageService.class)
                .values().stream()
                .collect(Collectors.toMap(item -> item.getRegisterName().getStorageType(), item -> item));
    }

}
