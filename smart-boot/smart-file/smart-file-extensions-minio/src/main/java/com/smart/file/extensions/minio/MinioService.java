package com.smart.file.extensions.minio;

import com.smart.file.core.service.ActualFileService;
import io.minio.ListBucketsArgs;
import io.minio.messages.Bucket;
import org.springframework.lang.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;

/**
 * minio服务类
 * @author zhongming4762
 * 2022/12/31 21:16
 */
public interface MinioService extends ActualFileService {

    /**
     * 判断存储桶是否存在
     * @param bucketName 存储桶名字
     * @return boolean
     */
    boolean bucketExists(String bucketName);

    /**
     * 创建存储桶
     * @param bucketName 存储桶名字
     * @return boolean
     */
    boolean makeBucket(String bucketName);


    /**
     * 删除存储桶
     * @param bucketName 存储桶名字
     * @return boolean
     */
    boolean removeBucket(String bucketName);

    /**
     * 查询存储桶列表
     * @return 存储桶列表
     */
    List<Bucket> listBuckets();

    /**
     * 查询存储桶列表
     * @param args 参数
     * @return 存储桶列表
     */
    List<Bucket> listBuckets(ListBucketsArgs args);

    /**
     * 保存文件
     * @param file 文件
     * @param filename 文件名
     * @param bucketName 存储桶名字
     * @param folder 文件夹
     * @throws IOException 文件写入失败抛出异常
     * @return 文件id
     */
    @NonNull
    String save(String bucketName, @NonNull File file, String filename, String folder) throws IOException;


    /**
     * 保存文件
     * @param inputStream 文件流
     * @param filename 文件名
     * @param folder 文件夹
     * @param bucketName 存储桶名字
     * @throws IOException IOException
     * @return 文件ID
     */
    @NonNull
    String save(String bucketName, @NonNull InputStream inputStream, String filename, String folder) throws IOException;

    /**
     * 获取文件外链
     * @param bucketName 存储桶
     * @param fileId 文件ID
     * @param expiry 过期时间（单位秒）
     * @return 文件外链
     */
    String getObjectUrl(String bucketName, String fileId, Duration expiry);

    /**
     * 获取文件外链
     * @param fileId 文件ID
     * @param expiry 过期时间（单位秒）
     * @return 文件外链
     */
    String getObjectUrl(String fileId, Duration expiry);

    /**
     * 下载文件
     * @param bucketName 存储桶
     * @param id 文件ID
     * @return 文件输入流
     */
    InputStream download(String bucketName, String id);

    /**
     * 删除文件
     * @param bucketName 存储桶
     * @param id 文件ID
     */
    void delete(String bucketName, String id);
}
