package com.smart.file.extensions.disk.service;

import com.smart.commons.core.utils.DigestUtils;
import com.smart.commons.file.SmartFileProperties;
import com.smart.commons.file.common.ActualFileServiceRegisterName;
import com.smart.commons.file.constants.ActualFileServiceEnum;
import com.smart.commons.file.pojo.bo.DiskFilePathBO;
import com.smart.commons.file.service.ActualFileService;
import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

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
        this.basePath = properties.getBasePath();
    }

    /**
     * 保存文件
     * @param file 文件
     * @param filename 文件名
     * @return 文件id
     */
    @Override
    public @NonNull String save(@NonNull File file, String filename) throws IOException {
        try (
                FileInputStream inputStream = new FileInputStream(file);
                FileInputStream md5InputStream = new FileInputStream(file)
                ) {
            String message = DigestUtils.sha256(md5InputStream);
            return this.save(inputStream, !StringUtils.hasText(filename) ? file.getName() : filename, message);
        }
    }

    /**
     * 保存文件
     * @param inputStream 文件流
     * @param filename 文件名
     * @return 文件ID
     */
    @SneakyThrows(IOException.class)
    @Override
    public @NonNull String save(@NonNull InputStream inputStream, String filename, String md5) {
        // 计算md5
        final DiskFilePathBO diskFilePath = new DiskFilePathBO(this.basePath, md5, filename);
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
    @SneakyThrows(IOException.class)
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
    public InputStream download(@NonNull String id) throws FileNotFoundException {
        final String filePath = DiskFilePathBO.createById(id, this.basePath).getFilePath();
        final File file = new File(filePath);
        return new FileInputStream(file);
    }

    /**
     * 下载文件
     * @param id 文件ID
     * @param outputStream 输出流，文件信息会写入输出流
     */
    @SneakyThrows(IOException.class)
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
