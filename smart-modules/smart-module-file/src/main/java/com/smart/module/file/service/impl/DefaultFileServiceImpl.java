package com.smart.module.file.service.impl;

import com.smart.framework.file.core.exception.SmartFileException;
import com.smart.framework.file.core.parameter.FileStorageDeleteParameter;
import com.smart.framework.file.core.parameter.FileStorageGetParameter;
import com.smart.framework.file.core.parameter.FileStorageSaveParameter;
import com.smart.framework.file.core.pojo.dto.FileStorageSaveResult;
import com.smart.framework.file.core.service.FileService;
import com.smart.framework.file.core.service.FileStorageService;
import com.smart.module.api.file.bo.FileDownloadResult;
import com.smart.module.api.file.bo.FileHandlerResult;
import com.smart.module.api.file.dto.FileSaveParameter;
import com.smart.module.file.model.SmartFilePO;
import com.smart.module.file.model.SmartFileStoragePO;
import com.smart.module.file.pojo.FileStorageServiceCacheData;
import com.smart.module.file.pojo.bo.SysFileBO;
import com.smart.module.file.service.SmartFileService;
import com.smart.module.file.service.SmartFileStorageService;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 默认的文件存储器
 * @author zhongming4762
 * 2023/2/16
 */
public class DefaultFileServiceImpl implements FileService {

    private final SmartFileStorageService smartFileStorageService;

    private final SmartFileService sysFileService;

    public DefaultFileServiceImpl(SmartFileStorageService fileStorageService, SmartFileService sysFileService) {
        this.smartFileStorageService = fileStorageService;
        this.sysFileService = sysFileService;
    }

    protected FileStorageSaveParameter getFileSaveStorageParameter(FileSaveParameter parameter) {
        return FileStorageSaveParameter.builder()
                .filename(parameter.getFilename())
                .folder(parameter.getFolder())
                .useOriginalFilename(parameter.isUseOriginalFilename())
                .fileStorageId(parameter.getFileStorageId())
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
    @Transactional(rollbackFor = Exception.class)
    public List<FileHandlerResult> batchDelete(@NonNull Collection<Long> fileIds) {
        List<SmartFilePO> sysFileList = this.sysFileService.listByIds(fileIds);
        if (CollectionUtils.isEmpty(sysFileList)) {
            return Collections.emptyList();
        }
        // 删除文件信息
        this.sysFileService.removeByIds(fileIds);
        // 删除实际文件
        sysFileList.stream()
                .collect(Collectors.groupingBy(SmartFilePO::getFileStorageId))
                .forEach((storageId, list) -> {
                    FileStorageService fileStorageService = this.getFileStorageService(storageId, null).getFileStorageService();
                    fileStorageService.delete(
                            FileStorageDeleteParameter.builder()
                                    .fileStorageId(storageId)
                                    .fileStoreList(
                                            list.stream()
                                                    .map(item -> new FileStorageDeleteParameter.FileStorageDeleteItem(item.getStorageStoreKey(), Boolean.TRUE.equals(item.getEncryptedYn())))
                                                    .toList()
                                    )
                                    .build()
                    );
                });

        return sysFileList.stream().map(item -> {
            FileHandlerResult result = new FileHandlerResult();
            BeanUtils.copyProperties(item, result);
            return result;
        }).toList();
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
        return this.doDownload(sysFileData);
    }

    /**
     * 下载文件
     *
     * @param fileStorageCode 文件存储器代码
     * @param filename        文件名
     * @return 下载内容
     */
    @Override
    public FileDownloadResult download(@NonNull String fileStorageCode, @NonNull String filename) {
        SmartFileStoragePO fileStorage = this.smartFileStorageService.getByCode(fileStorageCode);
        if (fileStorage == null) {
            throw new SmartFileException("文件存储器不存在，文件存储器代码：" + fileStorageCode);
        }
        SmartFilePO sysFileData = this.sysFileService.lambdaQuery()
                .eq(SmartFilePO::getFileStorageId, fileStorage.getId())
                .eq(SmartFilePO::getFilename, filename)
                .one();
        return this.doDownload(sysFileData);
    }

    /**
     * 执行下载文件
     *
     * @param sysFileData 文件信息
     * @return 下载内容
     */
    private FileDownloadResult doDownload(SmartFilePO sysFileData) {
        if (sysFileData == null) {
            return null;
        }
        // 判断文件是否过期
        if (sysFileData.getExpireTime() != null && ZonedDateTime.now().isAfter(sysFileData.getExpireTime())) {
            // 文件已经过期，但是还未被删除
            return null;
        }
        FileStorageService fileStorageService = this.getFileStorageService(sysFileData.getFileStorageId(), null).getFileStorageService();
        InputStream inputStream = fileStorageService.download(
                FileStorageGetParameter.builder()
                        .fileStorageId(sysFileData.getFileStorageId())
                        .storageStoreKey(sysFileData.getStorageStoreKey())
                        .encryptedYn(Boolean.TRUE.equals(sysFileData.getEncryptedYn()))
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

    /**
     * 获取文件的访问地址
     *
     * @param idList ID 列表
     * @return 访问地址列表
     */
    @Override
    public List<String> listAddress(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        // 查询文件信息
        List<SmartFilePO> fileList = this.sysFileService.listByIds(idList);
        if (CollectionUtils.isEmpty(fileList)) {
            throw new SmartFileException("获取文件信息失败，文件ID：" + idList);
        }
        // 校验文件是否加密
        boolean hasEncrypted = fileList.stream()
                .anyMatch(item -> Boolean.TRUE.equals(item.getEncryptedYn()));
        if (hasEncrypted) {
            throw new SmartFileException("文件已加密，无法获取访问地址");
        }
        return fileList.stream()
                .map(item -> {
                    FileStorageService fileStorageService = this.getFileStorageService(item.getFileStorageId(), null).getFileStorageService();
                    return fileStorageService.getAddress(
                            FileStorageGetParameter.builder()
                                    .storageStoreKey(item.getStorageStoreKey())
                                    .encryptedYn(false)
                                    .fileStorageId(item.getFileStorageId())
                                    .build()
                    );
                }).toList();
    }

    protected FileHandlerResult saveFile(@NonNull SysFileBO file) {
        FileStorageSaveParameter fileSaveStorageParameter = this.getFileSaveStorageParameter(file.getParameter());
        if (!StringUtils.hasText(fileSaveStorageParameter.getFilename())) {
            fileSaveStorageParameter.setFilename(file.getFile().getFilename());
        }
        // 获取文件存储器
        FileStorageServiceCacheData serviceCacheData = this.getFileStorageService(file.getParameter().getFileStorageId(), file.getParameter().getFileStorageCode());
        FileStorageService fileStorageService = serviceCacheData.getFileStorageService();
        if (fileSaveStorageParameter.getFileStorageId() == null) {
            fileSaveStorageParameter.setFileStorageId(serviceCacheData.getId());
        }
        // 保存文件
        FileStorageSaveResult fileStorageSaveResult = null;
        try (InputStream inputStream = file.getInputStream()) {
            fileStorageSaveResult = fileStorageService.save(inputStream, fileSaveStorageParameter);
            file.getFile().setStorageStoreKey(fileStorageSaveResult.getFileStoreKey());
            file.getFile().setFileStorageId(fileStorageSaveResult.getFileStorageId());
            file.getFile().setEncryptedYn(fileStorageSaveResult.isEncryptedYn());
            this.sysFileService.save(file.getFile());
            FileHandlerResult fileSaveResult = new FileHandlerResult();
            BeanUtils.copyProperties(file.getFile(), fileSaveResult);
            return fileSaveResult;
        } catch (Exception e) {
            if (fileStorageSaveResult != null) {
                fileStorageService.delete(
                        FileStorageDeleteParameter.builder()
                                .fileStoreList(
                                        List.of(new FileStorageDeleteParameter.FileStorageDeleteItem(fileStorageSaveResult.getFileStoreKey(), fileStorageSaveResult.isEncryptedYn()))
                                )
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
     * 获取并初始化文件存储服务
     * @param fileStorageId 文件存储ID
     * @param fileStorageCode 文件存储编码
     * @return 文件存储服务
     */
    protected FileStorageServiceCacheData getFileStorageService(Long fileStorageId, String fileStorageCode) {
        return this.smartFileStorageService.getFileStorageService(fileStorageId, fileStorageCode);
    }
}
