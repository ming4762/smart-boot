package com.smart.file.extensions.s3;

import com.smart.file.core.parameter.FileStorageDeleteParameter;
import com.smart.file.core.parameter.FileStorageGetParameter;
import com.smart.file.core.parameter.FileStorageSaveParameter;
import com.smart.file.core.service.FileStorageService;
import org.springframework.lang.NonNull;

import java.io.InputStream;

/**
 * AmazonS3 服务类
 * @author shizhongming
 * 2024/4/24 20:34
 * @since 3.0.0
 */
public interface AmazonS3Service extends FileStorageService {

    /**
     * 保存文件
     * @param parameter 参数
     * @param inputStream 文件流
     * @param bucketName 存储桶名字
     * @return 文件ID
     */
    @NonNull
    String save(@NonNull InputStream inputStream, FileStorageSaveParameter parameter, String bucketName);

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
