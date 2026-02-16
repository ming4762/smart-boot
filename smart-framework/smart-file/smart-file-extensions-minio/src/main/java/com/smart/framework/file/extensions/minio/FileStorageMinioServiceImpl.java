package com.smart.framework.file.extensions.minio;

import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.file.core.common.FileStorageServiceRegisterName;
import com.smart.framework.file.core.parameter.*;
import com.smart.framework.file.core.pojo.bo.DiskFilePathBO;
import com.smart.framework.file.core.pojo.dto.FileStorageSaveResult;
import com.smart.framework.file.core.properties.SmartFileStorageMinioProperties;
import com.smart.module.api.file.constants.FileStorageTypeEnum;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhongming4762
 * 2023/2/16 22:22
 */
public class FileStorageMinioServiceImpl implements MinioService {

    private static final Map<Long, MinioClientCache> MINIO_CLIENT_MAP = new ConcurrentHashMap<>();

    private MinioClientCache getMinioClientCache(Long id) {
        return MINIO_CLIENT_MAP.get(id);
    }

    @Getter
    @AllArgsConstructor
    @Builder
    private static class MinioClientCache {
        private Long fileStorageId;
        private boolean encryptedYn;
        private SmartFileStorageMinioProperties minioProperties;
        private MinioClient minioClient;
    }

    /**
     * 初始化
     *
     * @param initProperties 初始化参数
     */
    @Override
    public void init(FileStorageInitProperties initProperties) {
        MINIO_CLIENT_MAP.computeIfAbsent(initProperties.getFileStorageId(), id -> {
            SmartFileStorageMinioProperties minioProperties = JsonUtils.parse(initProperties.getProperties(), SmartFileStorageMinioProperties.class);
            MinioClient client = MinioClient.builder()
                    .endpoint(minioProperties.getEndpoint())
                    .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                    .build();
            return MinioClientCache.builder()
                    .fileStorageId(initProperties.getFileStorageId())
                    .encryptedYn(initProperties.isEncryptedYn())
                    .minioProperties(minioProperties)
                    .minioClient(client)
                    .build();
        });
    }

    /**
     * 根据ID销毁存储器
     *
     * @param fileStorageId 存储器ID
     */
    @SneakyThrows(Exception.class)
    @Override
    public void destroy(Long fileStorageId) {
        MinioClientCache minioClientCache = MINIO_CLIENT_MAP.get(fileStorageId);
        if (minioClientCache == null) {
            return;
        }
        minioClientCache.getMinioClient().close();
        MINIO_CLIENT_MAP.remove(fileStorageId);
    }


    @Override
    public void destroy() throws Exception {
        this.destroy(new ArrayList<>(MINIO_CLIENT_MAP.keySet()));
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
    public FileStorageSaveResult save(@NonNull InputStream inputStream, @NonNull FileStorageSaveParameter parameter) {
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
        return this.getMinioClientCache(parameter.getFileStorageId())
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
        this.getMinioClientCache(parameter.getFileStorageId())
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
        this.getMinioClientCache(parameter.getFileStorageId())
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
        return this.getMinioClientCache(parameter.getFileStorageId())
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
        Iterable<Result<Bucket>> results = this.getMinioClientCache(parameter.getFileStorageId())
                .getMinioClient()
                .listBuckets(args);
        List<Bucket> resultList = new ArrayList<>(10);
        while (results.iterator().hasNext()) {
            resultList.add(results.iterator().next().get());
        }
        return resultList;
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
    public FileStorageSaveResult save(FileStorageSaveParameter parameter, String bucketName, @NonNull File file) {
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
    public FileStorageSaveResult save(FileStorageSaveParameter parameter, String bucketName, @NonNull InputStream inputStream) {
        MinioClientCache minioClientCache = this.getMinioClientCache(parameter.getFileStorageId());
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
        return FileStorageSaveResult.builder()
                .fileStoreKey(diskFilePath.getFileId())
                .fileStorageId(parameter.getFileStorageId())
                .encryptedYn(minioClientCache.isEncryptedYn())
                .build();
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
        MinioClientCache minioClientCache = this.getMinioClientCache(parameter.getFileStorageId());
        if (bucketName == null) {
            bucketName = minioClientCache.getMinioProperties().getBucketName();
        }
        GetPresignedObjectUrlArgs.Builder builder = GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .object(this.getObject(parameter.getStorageStoreKey()))
                .method(Method.GET);
        if (expiry != null) {
            builder.expiry((int) expiry.getSeconds());
        }
        GetPresignedObjectUrlArgs urlArgs = builder.build();
        String objectUrl = minioClientCache.getMinioClient().getPresignedObjectUrl(urlArgs);
        return URLEncoder.encode(objectUrl, StandardCharsets.UTF_8);
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
        MinioClientCache minioClientCache = this.getMinioClientCache(parameter.getFileStorageId());
        if (bucketName == null) {
            bucketName = minioClientCache.getMinioProperties().getBucketName();
        }
        GetObjectArgs objectArgs = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(this.getObject(parameter.getStorageStoreKey()))
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
        MinioClientCache minioClientCache = this.getMinioClientCache(parameter.getFileStorageId());
        if (bucketName == null) {
            bucketName = minioClientCache.getMinioProperties().getBucketName();
        }
        for (FileStorageDeleteParameter.FileStorageDeleteItem item : parameter.getFileStoreList()) {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(this.getObject(item.getFileStoreKey()))
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
