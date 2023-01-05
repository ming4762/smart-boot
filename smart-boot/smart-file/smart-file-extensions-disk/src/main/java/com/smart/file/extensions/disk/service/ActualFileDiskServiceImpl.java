package com.smart.file.extensions.disk.service;

import com.smart.file.core.SmartFileProperties;
import com.smart.file.core.common.ActualFileServiceRegisterName;
import com.smart.file.core.constants.ActualFileServiceEnum;
import com.smart.file.core.exception.SmartFileException;
import com.smart.file.core.model.FileSaveParameter;
import com.smart.file.core.pojo.bo.DiskFilePathBO;
import com.smart.file.core.service.ActualFileService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.lang.NonNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author shizhongming
 * 2020/11/5 10:54 下午
 */
public class ActualFileDiskServiceImpl implements ActualFileService {

    private final String basePath;

    public ActualFileDiskServiceImpl(SmartFileProperties properties) {
        this.basePath = properties.getDisk().getBasePath();
        if (StringUtils.isBlank(this.basePath)) {
            throw new SmartFileException("使用本地磁盘存储必须设置：smart.file.disk.base-path");
        }
    }

    /**
     * 保存文件
     * @param file 文件
     * @return 文件id
     */
    @Override
    public @NonNull String save(@NonNull File file, @NonNull FileSaveParameter parameter) throws IOException {
        try (
                FileInputStream inputStream = new FileInputStream(file)
                ) {
            return this.save(inputStream, parameter);
        }
    }

    /**
     * 保存文件
     * @param inputStream 文件流
     * @param parameter 存储参数
     * @return 文件ID
     */
    @SneakyThrows
    @Override
    public @NonNull String save(@NonNull InputStream inputStream, @NonNull FileSaveParameter parameter) {
        // 计算md5
        final DiskFilePathBO diskFilePath = new DiskFilePathBO(this.basePath, parameter);
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
     * @param id 文件ID
     */
    @SneakyThrows
    @Override
    public void delete(@NonNull String id) {
        final String filePath = DiskFilePathBO.createById(id, this.basePath).getFilePath();
        final Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    /**
     * 批量删除文件
     * @param fileIdList 文件ID
     */
    @Override
    public void batchDelete(@NonNull List<String> fileIdList) {
        fileIdList.forEach(this::delete);
    }

    /**
     * 下载文件
     * @param id 文件id
     * @return 文件流
     */
    @Override
    public InputStream download(@NonNull String id) throws IOException {
        final String filePath = DiskFilePathBO.createById(id, this.basePath).getFilePath();
        final File file = new File(filePath);
        return Files.newInputStream(file.toPath());
    }

    /**
     * 下载文件
     * @param id 文件ID
     * @param outputStream 输出流，文件信息会写入输出流
     */
    @SneakyThrows
    @Override
    public void download(@NonNull String id, @NonNull OutputStream outputStream) {
        final String filePath = DiskFilePathBO.createById(id, this.basePath).getFilePath();
        final File file = new File(filePath);
        try (FileInputStream inputStream = new FileInputStream(file)) {
            IOUtils.copy(inputStream, outputStream);
        }
    }

    /**
     * 获取文件的绝对路径
     * @param id 文件ID
     * @return 文件绝对路径
     */
    @Override
    public String getAbsolutePath(@NonNull String id) {
        return DiskFilePathBO.createById(id, this.basePath).getFilePath();
    }

    /**
     * 获取注册名字
     * @return 注册名字
     */
    @Override
    public ActualFileServiceRegisterName getRegisterName() {
        return ActualFileServiceRegisterName.builder()
                .dbName(ActualFileServiceEnum.DISK.name())
                .beanName(ActualFileServiceEnum.DISK.getServiceName())
                .build();
    }
}
