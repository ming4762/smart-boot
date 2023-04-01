package com.smart.file.extensions.qiniu;

import com.smart.file.core.parameter.FileStorageDeleteParameter;
import com.smart.file.core.parameter.FileStorageGetParameter;
import com.smart.file.core.parameter.FileStorageSaveParameter;
import com.smart.file.core.service.FileStorageService;
import org.springframework.lang.NonNull;

import java.io.InputStream;
import java.time.Duration;

/**
 * @author zhongming4762
 * 2023/3/30
 */
public interface QiniuService extends FileStorageService {

    /**
     * 保存文件
     * @param inputStream 输入流
     * @param parameter 参数
     * @param bucketName bucket
     * @return key
     */
    String save(@NonNull InputStream inputStream, @NonNull FileStorageSaveParameter parameter, String bucketName);

    /**
     * 删除文件
     * @param parameter 参数
     * @param bucketName bucket
     */
    void delete(@NonNull FileStorageDeleteParameter parameter, String bucketName);

    /**
     * 下载文件
     * @param parameter 参数
     * @param bucketName bucket
     * @return 输入流
     */
    InputStream download(@NonNull FileStorageGetParameter parameter, String bucketName);

    /**
     * 获取文件外链
     *
     * @param parameter 参数
     * @param expiry    过期时间（单位秒）
     * @return 文件外链
     */
    String getObjectUrl(FileStorageGetParameter parameter, Duration expiry);
}
