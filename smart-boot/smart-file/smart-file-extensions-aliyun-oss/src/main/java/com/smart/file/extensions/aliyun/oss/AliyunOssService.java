package com.smart.file.extensions.aliyun.oss;

import com.aliyun.oss.OSS;
import com.smart.file.core.parameter.FileStorageDeleteParameter;
import com.smart.file.core.parameter.FileStorageGetParameter;
import com.smart.file.core.parameter.FileStorageSaveParameter;
import com.smart.file.core.service.FileStorageService;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * 阿里云OSS服务类
 * @author zhongming4762
 * 2023/3/4
 */
public interface AliyunOssService extends FileStorageService {

    /**
     * 获取oss客户端
     * @param storageProperties 配置信息
     * @return OSS客户端
     */
    OSS getOssClient(String storageProperties);

    /**
     * 保存文件
     * @param parameter 参数
     * @param file 文件
     * @param bucketName 存储桶名字
     * @return 文件id
     */
    @SneakyThrows(IOException.class)
    @NonNull
    default String save(FileStorageSaveParameter parameter, String bucketName, @NonNull File file) {
        return this.save(parameter, bucketName, Files.newInputStream(file.toPath()));
    }

    /**
     * 保存文件
     * @param parameter 参数
     * @param inputStream 文件流
     * @param bucketName 存储桶名字
     * @return 文件ID
     */
    @NonNull
    String save(FileStorageSaveParameter parameter, String bucketName, @NonNull InputStream inputStream);

    /**
     * 删除文件
     * @param bucketName 存储桶
     * @param parameter 参数
     */
    void delete(FileStorageDeleteParameter parameter, String bucketName);

    /**
     * 下载文件
     * @param bucketName 存储桶
     * @param parameter 参数
     * @return 文件输入流
     */
    InputStream download(FileStorageGetParameter parameter, String bucketName);
}
