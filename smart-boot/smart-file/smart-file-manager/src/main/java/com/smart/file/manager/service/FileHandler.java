package com.smart.file.manager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.file.core.SmartFileProperties;
import com.smart.file.core.exception.SmartFileException;
import com.smart.file.core.service.ActualFileService;
import com.smart.file.manager.constants.FileTypeEnum;
import com.smart.file.manager.model.SysFilePO;
import com.smart.file.manager.pojo.bo.SysFileBO;
import com.smart.file.manager.pojo.dto.SaveFileDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文件控制类
 * @author ShiZhongMing
 * 2022/8/24
 * @since 3.0.0
 */
@Component
@Slf4j
public class FileHandler implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private Map<String, ActualFileService> actualFileServiceMap;

    private final SmartFileProperties fileProperties;

    private final SysFileService sysFileService;

    public FileHandler(SmartFileProperties fileProperties, SysFileService sysFileService) {
        this.fileProperties = fileProperties;
        this.sysFileService = sysFileService;
    }

    /**
     * 获取文件执行器
     * @return 文件执行器
     */
    protected ActualFileService getActualFileService(String dbName) {
        ActualFileService actualFileService = this.actualFileServiceMap.get(dbName);
        if (Objects.isNull(actualFileService)) {
            // 获取默认的文件执行器
            actualFileService = this.actualFileServiceMap.get(this.fileProperties.getDefaultHandler());
        }
        if (Objects.isNull(actualFileService)) {
            throw new SmartFileException(String.format("获取文件执行器失败，未找到对应的文件执行器，执行器名称：%s", dbName));
        }
        return actualFileService;
    }

    /**
     * 保存文件
     * @param multipartFile 文件信息
     * @param saveFileDto   文件信息
     * @return 文件实体
     */
    @NonNull
    @Transactional(rollbackFor = Exception.class)
    public SysFilePO saveFile(@NonNull MultipartFile multipartFile, @NonNull SaveFileDTO saveFileDto) {
        return this.saveFile(this.createSysFileBo(multipartFile, saveFileDto.getFilename(), saveFileDto.getType(), saveFileDto.getHandlerType()));
    }

    /**
     * 保存文件
     *
     * @param file 文件信息
     * @return 文件信息
     */
    @NonNull
    public SysFilePO saveFile(@NonNull SysFileBO file) {
        // 判断是否存在相同文件
        SysFilePO sameFile = this.getSameFile(file.getFile());
        if (Objects.nonNull(sameFile)) {
            return sameFile;
        }
        // 保存文件
        file.getFile().setDbId(this.saveActualFile(file));
        try {
            if (StringUtils.isEmpty(file.getFile().getType())) {
                file.getFile().setType(FileTypeEnum.TEMP.name());
            }
            this.sysFileService.saveWithUser(file.getFile(), AuthUtils.getCurrentUserId());
            return file.getFile();
        } catch (Exception e) {
            log.error("保存文件信息到数据库发生错误，删除保存的文件");
            this.getActualFileService(file.getFile().getHandlerType()).delete(file.getFile().getDbId());
            throw new SmartFileException(e);
        }
    }

    /**
     * 保存文件
     *
     * @param multipartFile 文件信息
     * @param type          文件类型
     * @param handlerType   指定文件执行器类型
     * @return 文件实体信息
     */
    public SysFilePO saveFile(@NonNull MultipartFile multipartFile, String type, String handlerType) {
        final SysFilePO file = new SysFilePO();
        file.setType(type);
        try {
            return this.saveFile(new SysFileBO(multipartFile, null, type, handlerType));
        } catch (Exception e) {
            throw new SmartFileException("系统发生未知异常", e);
        }
    }

    /**
     * 保存文件
     *
     * @param multipartFile 文件信息
     * @param type          文件类型
     * @return 文件实体信息
     */
    public SysFilePO saveFile(@NonNull MultipartFile multipartFile, String type) {
        return this.saveFile(multipartFile, type, this.fileProperties.getDefaultHandler());
    }

    /**
     * 获取相同的file
     * @param file 比对的文件信息
     * @return 相同的文件信息，返回null 则不存在相同的文件
     */
    @Nullable
    public SysFilePO getSameFile(@NonNull SysFilePO file) {
        // 根据md5判断文件是否存在
        final List<SysFilePO> md5FileList = this.sysFileService.list(
                new QueryWrapper<SysFilePO>().lambda()
                        .eq(SysFilePO :: getMd5, file.getMd5())
                        .eq(SysFilePO :: getFileName, file.getFileName())
                        .eq(SysFilePO :: getFileSize, file.getFileSize())
        );
        if (md5FileList.isEmpty()) {
            return null;
        }
        return md5FileList.iterator().next();
    }

    /**
     * 删除文件
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    @Nullable
    @Transactional(rollbackFor = Exception.class)
    public SysFilePO deleteFile(@NonNull Long fileId) {
        final SysFilePO file = this.sysFileService.getById(fileId);
        if (ObjectUtils.isNotEmpty(file)) {
            // 删除文件信息
            this.sysFileService.removeById(fileId);
            // 删除实际文件
            if (ObjectUtils.isEmpty(file.getDbId())) {
                throw new SmartFileException("实际文件ID为空，删除失败");
            }
            this.getActualFileService(file.getHandlerType()).delete(file.getDbId());
        }
        return file;
    }

    /**
     * 批量删除文件
     *
     * @param fileIds 文件id列表
     * @return 删除是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteFile(@NonNull Collection<Serializable> fileIds) {
        if (!fileIds.isEmpty()) {
            final List<SysFilePO> fileList = this.sysFileService.listByIds(fileIds);
            if (!CollectionUtils.isEmpty(fileList)) {
                this.sysFileService.removeByIds(fileIds);
                // 删除实际文件
                fileList.stream()
                        .collect(Collectors.groupingBy(SysFilePO :: getHandlerType))
                        .forEach((key, value) -> this.getActualFileService(key).batchDelete(
                                value.stream()
                                        .map(SysFilePO :: getDbId)
                                        .filter(StringUtils :: isNotEmpty)
                                        .toList()
                        ));
            }
            return true;
        }
        return false;
    }

    /**
     * 下载文件
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    public SysFileBO download(@NonNull Long fileId) {
        final SysFilePO file = this.sysFileService.getById(fileId);
        if (ObjectUtils.isNotEmpty(file)) {
            return this.download(file);
        }
        return null;
    }

    /**
     * 下载文件
     *
     * @param file 文件实体类信息
     * @return 文件信息
     */
    public SysFileBO download(@NonNull SysFilePO file) {
        Assert.notNull(file.getDbId(), "实际文件ID为空，删除失败");
        try {
            return new SysFileBO(file, this.getActualFileService(file.getHandlerType()).download(file.getDbId()));
        } catch (FileNotFoundException e) {
            throw new SmartFileException("文件未找到", e);
        }
    }

    /**
     * 获取文件的绝对路径
     *
     * @param fileId 文件ID
     * @return 文件绝对路径
     */
    public String getAbsolutePath(@NonNull Long fileId) {
        final SysFilePO file = this.sysFileService.getById(fileId);
        Assert.notNull(file.getDbId(), "获取文件信息发生错误，未找到文件实体ID");
        return this.getActualFileService(file.getHandlerType()).getAbsolutePath(file.getDbId());
    }


    /**
     * 创建 SysFileBO
     * @param file 文件
     * @param fileName 文件名
     * @param type 文件类型
     * @param handlerType 执行器类型
     * @return SysFileBO
     */
    private SysFileBO createSysFileBo(MultipartFile file, String fileName, String type, String handlerType) {
        String handler = StringUtils.isBlank(handlerType) ? this.fileProperties.getDefaultHandler() : handlerType;
        return new SysFileBO(file, fileName, type, handler);
    }

    /**
     * 保存实际文件
     * @param file 文件信息
     * @return 文件ID
     */
    @SneakyThrows(IOException.class)
    private String saveActualFile(SysFileBO file) {
        try (InputStream inputStream = file.getInputStream()) {
            return this.getActualFileService(file.getFile().getHandlerType()).save(inputStream, file.getFile().getFileName(), file.getFile().getMd5());
        }

    }

    @Override
    public void afterPropertiesSet() {
        this.actualFileServiceMap = FileHandler.initActualFileService(applicationContext);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private static Map<String, ActualFileService> initActualFileService(ApplicationContext applicationContext) {
        // 获取所有执行器
        return applicationContext.getBeansOfType(ActualFileService.class)
                .values().stream()
                .collect(Collectors.toMap(item -> item.getRegisterName().getDbName(), item -> item));
    }
}
