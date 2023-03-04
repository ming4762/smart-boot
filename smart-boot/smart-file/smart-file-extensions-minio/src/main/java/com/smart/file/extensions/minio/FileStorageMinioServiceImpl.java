package com.smart.file.extensions.minio;

import com.smart.commons.core.utils.JsonUtils;
import com.smart.file.core.common.FileStorageServiceRegisterName;
import com.smart.file.core.constants.FileStorageTypeEnum;
import com.smart.file.core.parameter.FileStorageCommonParameter;
import com.smart.file.core.parameter.FileStorageDeleteParameter;
import com.smart.file.core.parameter.FileStorageGetParameter;
import com.smart.file.core.parameter.FileStorageSaveParameter;
import com.smart.file.core.pojo.bo.DiskFilePathBO;
import com.smart.file.core.properties.SmartFileStorageMinioProperties;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhongming4762
 * 2023/2/16 22:22
 */
public class FileStorageMinioServiceImpl implements MinioService {

    private static final Map<String, MinioClientCache> MINIO_CLIENT_MAP = new ConcurrentHashMap<>();

    protected MinioClientCache getMinioClientCache(String minioProperties) {
        return MINIO_CLIENT_MAP.computeIfAbsent(minioProperties, key -> {
            SmartFileStorageMinioProperties properties = JsonUtils.parse(minioProperties, SmartFileStorageMinioProperties.class);
            MinioClient client = MinioClient.builder()
                    .endpoint(properties.getEndpoint())
                    .credentials(properties.getAccessKey(), properties.getSecretKey())
                    .build();
            return new MinioClientCache(client, properties);
        });
    }

    @Getter
    @AllArgsConstructor
    private static class MinioClientCache {
        private MinioClient minioClient;

        private SmartFileStorageMinioProperties minioProperties;
    }

    /**
     * 获取注册名字
     *
     * @return 注册名字
     */
    @Override
    public FileStorageServiceRegisterName getRegisterName() {
        return FileStorageServiceRegisterName.builder()
                .storageType(FileStorageTypeEnum.MINIO)
                .beanName(FileStorageTypeEnum.MINIO.getServiceName())
                .build();
    }

    /**
     * 保存文件
     *
     * @param inputStream 输入流
     * @param parameter   参数
     * @return 文件存储标识
     */
    @Override
    public String save(@NonNull InputStream inputStream, @NonNull FileStorageSaveParameter parameter) {
        return this.save(parameter, null, inputStream);
    }

    /**
     * 删除文件
     *
     * @param parameter 删除参数
     */
    @Override
    public void delete(@NonNull FileStorageDeleteParameter parameter) {
        this.delete(parameter, null);
    }

    /**
     * 下载文件
     *
     * @param parameter 文件下载参数
     * @return 文件流
     */
    @Override
    public InputStream download(@NonNull FileStorageGetParameter parameter) {
        return this.download(parameter, (String) null);
    }

    /**
     * 获取文件访问地址
     *
     * @param parameter 参数
     * @return address
     */
    @Override
    public String getAddress(@NonNull FileStorageGetParameter parameter) {
        return this.getObjectUrl(parameter, null);
    }

    /**
     * 判断存储桶是否存在
     *
     * @param parameter  storage参数
     * @param bucketName 存储桶名字
     * @return boolean
     */
    @Override
    @SneakyThrows({
            ErrorResponseException.class,
            InsufficientDataException.class,
            InternalException.class,
            InvalidKeyException.class,
            InvalidResponseException.class,
            IOException.class,
            NoSuchAlgorithmException.class,
            ServerException.class,
            XmlParserException.class
    })
    public boolean bucketExists(FileStorageCommonParameter parameter, String bucketName) {
        return this.getMinioClientCache(parameter.getStorageProperties())
                .getMinioClient()
                .bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 创建存储桶
     *
     * @param parameter  参数
     * @param bucketName 存储桶名字
     * @return boolean
     */
    @Override
    @SneakyThrows({
            ErrorResponseException.class,
            InsufficientDataException.class,
            InternalException.class,
            InvalidKeyException.class,
            InvalidResponseException.class,
            IOException.class,
            NoSuchAlgorithmException.class,
            ServerException.class,
            XmlParserException.class
    })
    public boolean makeBucket(FileStorageCommonParameter parameter, String bucketName) {
        this.getMinioClientCache(parameter.getStorageProperties())
                .getMinioClient()
                .makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
        return true;
    }

    /**
     * 删除存储桶
     *
     * @param parameter  参数
     * @param bucketName 存储桶名字
     * @return boolean
     */
    @Override
    @SneakyThrows({
            ErrorResponseException.class,
            InsufficientDataException.class,
            InternalException.class,
            InvalidKeyException.class,
            InvalidResponseException.class,
            IOException.class,
            NoSuchAlgorithmException.class,
            ServerException.class,
            XmlParserException.class
    })
    public boolean removeBucket(FileStorageCommonParameter parameter, String bucketName) {
        this.getMinioClientCache(parameter.getStorageProperties())
                .getMinioClient()
                .removeBucket(
                        RemoveBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
        return true;
    }

    /**
     * 查询存储桶列表
     *
     * @param parameter 参数
     * @return 存储桶列表
     */
    @Override
    @SneakyThrows({
            ErrorResponseException.class,
            InsufficientDataException.class,
            InternalException.class,
            InvalidKeyException.class,
            InvalidResponseException.class,
            IOException.class,
            NoSuchAlgorithmException.class,
            ServerException.class,
            XmlParserException.class
    })
    public List<Bucket> listBuckets(FileStorageCommonParameter parameter) {
        return this.getMinioClientCache(parameter.getStorageProperties())
                .getMinioClient()
                .listBuckets();
    }

    /**
     * 查询存储桶列表
     *
     * @param parameter 参数
     * @param args      参数
     * @return 存储桶列表
     */
    @Override
    @SneakyThrows({
            ErrorResponseException.class,
            InsufficientDataException.class,
            InternalException.class,
            InvalidKeyException.class,
            InvalidResponseException.class,
            IOException.class,
            NoSuchAlgorithmException.class,
            ServerException.class,
            XmlParserException.class
    })
    public List<Bucket> listBuckets(FileStorageCommonParameter parameter, ListBucketsArgs args) {
        return this.getMinioClientCache(parameter.getStorageProperties())
                .getMinioClient()
                .listBuckets(args);
    }

    /**
     * 保存文件
     *
     * @param parameter  参数
     * @param bucketName 存储桶名字
     * @param file       文件
     * @return 文件id
     */
    @SneakyThrows(IOException.class)
    @NonNull
    @Override
    public String save(FileStorageSaveParameter parameter, String bucketName, @NonNull File file) {
        try (InputStream inputStream = Files.newInputStream(file.toPath())) {
            return this.save(parameter, bucketName, inputStream);
        }
    }

    /**
     * 保存文件
     *
     * @param parameter   参数
     * @param bucketName  存储桶名字
     * @param inputStream 文件流
     * @return 文件ID
     */
    @NonNull
    @Override
    @SneakyThrows({
            ErrorResponseException.class,
            InsufficientDataException.class,
            InternalException.class,
            InvalidKeyException.class,
            InvalidResponseException.class,
            IOException.class,
            NoSuchAlgorithmException.class,
            ServerException.class,
            XmlParserException.class
    })
    public String save(FileStorageSaveParameter parameter, String bucketName, @NonNull InputStream inputStream) {
        MinioClientCache minioClientCache = this.getMinioClientCache(parameter.getStorageProperties());
        if (bucketName == null) {
            bucketName = minioClientCache.getMinioProperties().getBucketName();
        }
        DiskFilePathBO diskFilePath = new DiskFilePathBO("", parameter);
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(diskFilePath.getFilePath())
                .contentType("application/octet-stream")
                .stream(inputStream, inputStream.available(), -1)
                .build();
        minioClientCache.getMinioClient().putObject(putObjectArgs);
        return diskFilePath.getFileId();
    }

    /**
     * 获取文件外链
     *
     * @param parameter  参数
     * @param bucketName 存储桶
     * @param expiry     过期时间（单位秒）
     * @return 文件外链
     */
    @Override
    @SneakyThrows({
            ErrorResponseException.class,
            InsufficientDataException.class,
            InternalException.class,
            InvalidKeyException.class,
            InvalidResponseException.class,
            IOException.class,
            NoSuchAlgorithmException.class,
            ServerException.class,
            XmlParserException.class
    })
    public String getObjectUrl(FileStorageGetParameter parameter, String bucketName, Duration expiry) {
        MinioClientCache minioClientCache = this.getMinioClientCache(parameter.getStorageProperties());
        if (bucketName == null) {
            bucketName = minioClientCache.getMinioProperties().getBucketName();
        }
        GetPresignedObjectUrlArgs.Builder builder = GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .object(this.getObject(parameter.getFileStorageKey()))
                .method(Method.GET);
        if (expiry != null) {
            builder.expiry(Long.valueOf(expiry.getSeconds()).intValue());
        }
        GetPresignedObjectUrlArgs urlArgs = builder.build();
        String objectUrl = minioClientCache.getMinioClient().getPresignedObjectUrl(urlArgs);
        return URLEncoder.encode(objectUrl, StandardCharsets.UTF_8.name());
    }

    /**
     * 获取文件外链
     *
     * @param parameter 参数
     * @param expiry    过期时间（单位秒）
     * @return 文件外链
     */
    @Override
    public String getObjectUrl(FileStorageGetParameter parameter, Duration expiry) {
        return this.getObjectUrl(parameter, null, expiry);
    }

    /**
     * 下载文件
     *
     * @param parameter  参数
     * @param bucketName 存储桶
     * @return 文件输入流
     */
    @Override
    @SneakyThrows({
            ErrorResponseException.class,
            InsufficientDataException.class,
            InternalException.class,
            InvalidKeyException.class,
            InvalidResponseException.class,
            IOException.class,
            NoSuchAlgorithmException.class,
            ServerException.class,
            XmlParserException.class
    })
    public InputStream download(FileStorageGetParameter parameter, String bucketName) {
        MinioClientCache minioClientCache = this.getMinioClientCache(parameter.getStorageProperties());
        if (bucketName == null) {
            bucketName = minioClientCache.getMinioProperties().getBucketName();
        }
        GetObjectArgs objectArgs = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(this.getObject(parameter.getFileStorageKey()))
                .build();
        return minioClientCache.getMinioClient().getObject(objectArgs);
    }

    /**
     * 删除文件
     *
     * @param parameter  参数
     * @param bucketName 存储桶
     */
    @Override
    @SneakyThrows({
            ErrorResponseException.class,
            InsufficientDataException.class,
            InternalException.class,
            InvalidKeyException.class,
            InvalidResponseException.class,
            IOException.class,
            NoSuchAlgorithmException.class,
            ServerException.class,
            XmlParserException.class
    })
    public void delete(FileStorageDeleteParameter parameter, String bucketName) {
        MinioClientCache minioClientCache = this.getMinioClientCache(parameter.getStorageProperties());
        if (bucketName == null) {
            bucketName = minioClientCache.getMinioProperties().getBucketName();
        }
        for (String key : parameter.getFileStoreKeyList()) {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(this.getObject(key))
                    .build();
            // TODO:minio批量删除接口
            minioClientCache.getMinioClient().removeObject(removeObjectArgs);
        }

    }

    protected String getObject(String id) {
        DiskFilePathBO filePath = DiskFilePathBO.createById(id, "");
        return filePath.getFilePath();
    }
}
