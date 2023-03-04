package com.smart.file.extensions.minio;

import com.smart.file.core.parameter.FileStorageCommonParameter;
import com.smart.file.core.parameter.FileStorageDeleteParameter;
import com.smart.file.core.parameter.FileStorageGetParameter;
import com.smart.file.core.parameter.FileStorageSaveParameter;
import com.smart.file.core.service.FileStorageService;
import io.minio.ListBucketsArgs;
import io.minio.messages.Bucket;
import org.springframework.lang.NonNull;

import java.io.File;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;

/**
 * minio服务类
 * @author zhongming4762
 * 2022/12/31 21:16
 */
public interface MinioService extends FileStorageService {

    /**
     * 判断存储桶是否存在
     * @param parameter storage参数
     * @param bucketName 存储桶名字
     * @return boolean
     */
    boolean bucketExists(FileStorageCommonParameter parameter, String bucketName);

    /**
     * 创建存储桶
     * @param parameter 参数
     * @param bucketName 存储桶名字
     * @return boolean
     */
    boolean makeBucket(FileStorageCommonParameter parameter, String bucketName);


    /**
     * 删除存储桶
     * @param parameter 参数
     * @param bucketName 存储桶名字
     * @return boolean
     */
    boolean removeBucket(FileStorageCommonParameter parameter, String bucketName);

    /**
     * 查询存储桶列表
     * @param parameter 参数
     * @return 存储桶列表
     */
    List<Bucket> listBuckets(FileStorageCommonParameter parameter);

    /**
     * 查询存储桶列表
     * @param parameter 参数
     * @param args 参数
     * @return 存储桶列表
     */
    List<Bucket> listBuckets(FileStorageCommonParameter parameter, ListBucketsArgs args);

    /**
     * 保存文件
     * @param parameter 参数
     * @param file 文件
     * @param bucketName 存储桶名字
     * @return 文件id
     */
    @NonNull
    String save(FileStorageSaveParameter parameter, String bucketName, @NonNull File file);


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
     * 获取文件外链
     * @param parameter 参数
     * @param bucketName 存储桶
     * @param expiry 过期时间（单位秒）
     * @return 文件外链
     */
    String getObjectUrl(FileStorageGetParameter parameter, String bucketName, Duration expiry);

    /**
     * 获取文件外链
     * @param parameter 参数
     * @param expiry 过期时间（单位秒）
     * @return 文件外链
     */
    String getObjectUrl(FileStorageGetParameter parameter, Duration expiry);

    /**
     * 下载文件
     * @param bucketName 存储桶
     * @param parameter 参数
     * @return 文件输入流
     */
    InputStream download(FileStorageGetParameter parameter, String bucketName);

    /**
     * 删除文件
     * @param bucketName 存储桶
     * @param parameter 参数
     */
    void delete(FileStorageDeleteParameter parameter, String bucketName);
}
