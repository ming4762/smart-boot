package com.smart.framework.file.extensions.s3;

import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.file.core.common.FileStorageServiceRegisterName;
import com.smart.framework.file.core.parameter.FileStorageDeleteParameter;
import com.smart.framework.file.core.parameter.FileStorageGetParameter;
import com.smart.framework.file.core.parameter.FileStorageInitProperties;
import com.smart.framework.file.core.parameter.FileStorageSaveParameter;
import com.smart.framework.file.core.pojo.bo.DiskFilePathBO;
import com.smart.framework.file.core.pojo.dto.FileStorageSaveResult;
import com.smart.framework.file.core.properties.SmartFileStorageAmazonS3Properties;
import com.smart.module.api.file.constants.FileStorageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.jspecify.annotations.NonNull;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shizhongming
 * 2024/4/24 20:36
 * @since 3.0.0
 */
@Slf4j
public class DefaultAmazonS3ServiceImpl implements AmazonS3Service, DisposableBean {

    private static final Map<Long, ClientCache> CLIENT_CACHE = new ConcurrentHashMap<>();

    protected ClientCache getClientCache(Long id) {
        return CLIENT_CACHE.get(id);
    }

    @Getter
    @AllArgsConstructor
    @Builder
    protected static class ClientCache {
        private Long fileStorageId;
        private boolean encryptedYn;
        private SmartFileStorageAmazonS3Properties properties;
        private S3Client client;
    }

    /**
     * 初始化
     *
     * @param initProperties 初始化参数
     */
    @Override
    public void init(FileStorageInitProperties initProperties) {
        CLIENT_CACHE.computeIfAbsent(initProperties.getFileStorageId(), key -> {
            SmartFileStorageAmazonS3Properties s3Properties = JsonUtils.parse(initProperties.getProperties(), SmartFileStorageAmazonS3Properties.class);
            S3Client s3Client = S3Client.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(s3Properties.getAccessKey(), s3Properties.getSecretKey())))
                    .region(Region.AWS_GLOBAL)
                    .endpointOverride(URI.create(s3Properties.getEndpoint()))
                    .serviceConfiguration(
                            S3Configuration.builder()
                                    .pathStyleAccessEnabled(false)
                                    .chunkedEncodingEnabled(false)
                                    .build()
                    ).build();
            return ClientCache.builder()
                    .fileStorageId(initProperties.getFileStorageId())
                    .encryptedYn(initProperties.isEncryptedYn())
                    .properties(s3Properties)
                    .client(s3Client)
                    .build();
        });
    }


    @Override
    public void destroy() {
        this.destroy(new ArrayList<>(CLIENT_CACHE.keySet()));
    }

    /**
     * 根据ID销毁存储器
     *
     * @param fileStorageId 存储器ID
     */
    @Override
    public void destroy(Long fileStorageId) {
        ClientCache clientCache = CLIENT_CACHE.get(fileStorageId);
        if (clientCache == null) {
            return;
        }
        clientCache.getClient().close();
        CLIENT_CACHE.remove(fileStorageId);
    }

    /**
     * 获取注册名字
     *
     * @return 注册名字
     */
    @Override
    public FileStorageServiceRegisterName getRegisterName() {
        return FileStorageServiceRegisterName.builder()
                .storageType(FileStorageTypeEnum.AMAZON_S3)
                .beanName(FileStorageTypeEnum.AMAZON_S3.getServiceName())
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
        return this.save(inputStream, parameter, null);
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
     * 删除文件
     *
     * @param parameter  参数
     * @param bucketName 存储桶
     */
    @Override
    public void delete(FileStorageDeleteParameter parameter, String bucketName) {
        ClientCache clientCache = this.getClientCache(parameter.getFileStorageId());
        if (bucketName == null) {
            bucketName = clientCache.getProperties().getBucketName();
        }
        List<ObjectIdentifier> keys = parameter.getFileStoreList().stream()
                .map(item -> ObjectIdentifier.builder()
                        .key(this.getObjectKey(item.getFileStoreKey()))
                        .build()).toList();
        Delete del = Delete.builder()
                .objects(keys)
                .build();

        DeleteObjectsRequest multiObjectDeleteRequest = DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete(del)
                .build();
        clientCache.getClient().deleteObjects(multiObjectDeleteRequest);

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
     * 下载文件
     *
     * @param parameter  参数
     * @param bucketName 存储桶
     * @return 文件输入流
     */
    @Override
    public InputStream download(FileStorageGetParameter parameter, String bucketName) {
        ClientCache clientCache = this.getClientCache(parameter.getFileStorageId());
        if (bucketName == null) {
            bucketName = clientCache.getProperties().getBucketName();
        }
        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .key(this.getObjectKey(parameter.getStorageStoreKey()))
                .bucket(bucketName)
                .build();
        return clientCache.getClient().getObject(objectRequest);
    }

    /**
     * 获取文件访问地址
     *
     * @param parameter 参数
     * @return address
     */
    @Override
    public String getAddress(@NonNull FileStorageGetParameter parameter) {
        ClientCache clientCache = this.getClientCache(parameter.getFileStorageId());
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(clientCache.getProperties().getBucketName())
                .key(parameter.getStorageStoreKey())
                .build();

//        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
//                .signatureDuration(Duration.ofMinutes(10))  // The URL will expire in 10 minutes.
//                .getObjectRequest(objectRequest)
//                .build();
        return "";
    }

    /**
     * 保存文件
     *
     * @param inputStream 文件流
     * @param parameter   参数
     * @param bucketName  存储桶名字
     * @return 文件ID
     */
    @SneakyThrows(IOException.class)
    @NonNull
    @Override
    public FileStorageSaveResult save(@NonNull InputStream inputStream, FileStorageSaveParameter parameter, String bucketName) {
        ClientCache clientCache = this.getClientCache(parameter.getFileStorageId());
        String bucket = bucketName == null ? clientCache.getProperties().getBucketName() : bucketName;
        DiskFilePathBO diskFilePath = new DiskFilePathBO("", parameter);
        clientCache.getClient().putObject(
                s -> PutObjectRequest.builder()
                        .key(diskFilePath.getFilePath(true))
                        .bucket(bucket),
                RequestBody.fromInputStream(inputStream, inputStream.available())
        );
        return FileStorageSaveResult.builder()
                .fileStorageId(parameter.getFileStorageId())
                .fileStoreKey(diskFilePath.getFileId())
                .encryptedYn(clientCache.isEncryptedYn())
               .build();
    }

    protected String getObjectKey(String id) {
        return DiskFilePathBO.createById(id, "").getFilePath(true);
    }
}
