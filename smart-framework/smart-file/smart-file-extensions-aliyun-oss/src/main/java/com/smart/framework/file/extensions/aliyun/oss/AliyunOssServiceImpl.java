package com.smart.framework.file.extensions.aliyun.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSEncryptionClient;
import com.aliyun.oss.OSSEncryptionClientBuilder;
import com.aliyun.oss.crypto.SimpleRSAEncryptionMaterials;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.commons.core.utils.auth.RsaUtils;
import com.smart.framework.file.core.common.FileStorageServiceRegisterName;
import com.smart.framework.file.core.exception.SmartFileException;
import com.smart.framework.file.core.parameter.FileStorageDeleteParameter;
import com.smart.framework.file.core.parameter.FileStorageGetParameter;
import com.smart.framework.file.core.parameter.FileStorageInitProperties;
import com.smart.framework.file.core.parameter.FileStorageSaveParameter;
import com.smart.framework.file.core.pojo.bo.DiskFilePathBO;
import com.smart.framework.file.core.pojo.dto.FileStorageSaveResult;
import com.smart.framework.file.core.properties.SmartFileStorageAliyunOssProperties;
import com.smart.module.api.file.constants.FileStorageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.io.InputStream;
import java.net.URL;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * aliyun oss服务类
 * @author zhongming4762
 * 2023/3/4
 */
@Slf4j
public class AliyunOssServiceImpl implements AliyunOssService {

    private static final Map<Long, OssClientCache> OSS_CLIENT_CACHE_MAP = new ConcurrentHashMap<>();

    protected OssClientCache getOssClientCache(Long id) {
        OssClientCache ossClientCache = OSS_CLIENT_CACHE_MAP.get(id);
        if (ossClientCache == null) {
            throw new IllegalArgumentException("oss client cache is null, please init oss client first");
        }
        return ossClientCache;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    protected static class OssClientCache {
        private Long fileStorageId;
        private boolean encryptedYn;
        private OSS ossClient;
        // 加密客户端
        private OSSEncryptionClient ossEncryptionClient;
        private SmartFileStorageAliyunOssProperties properties;
        private String privateKey;
        private String publicKey;
    }

    /**
     * 获取注册名字
     *
     * @return 注册名字
     */
    @Override
    public FileStorageServiceRegisterName getRegisterName() {
        return FileStorageServiceRegisterName.builder()
                .storageType(FileStorageTypeEnum.ALIYUN_OSS)
                .beanName(FileStorageTypeEnum.ALIYUN_OSS.getServiceName())
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
        if (parameter.isEncryptedYn()) {
            throw new SmartFileException("加密文件无法直接访问");
        }
        OssClientCache clientCache = this.getOssClientCache(parameter.getFileStorageId());
        Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
        URL url = this.getOssClient(parameter.getFileStorageId(), false).generatePresignedUrl(clientCache.getProperties().getBucketName(), this.getObject(parameter.getStorageStoreKey()), expiration);
        return url.toString();
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
    public FileStorageSaveResult save(FileStorageSaveParameter parameter, String bucketName, @NonNull InputStream inputStream) {
        OssClientCache ossClientCache = this.getOssClientCache(parameter.getFileStorageId());
        if (bucketName == null) {
            bucketName = ossClientCache.getProperties().getBucketName();
        }
        DiskFilePathBO diskFilePath = new DiskFilePathBO("", parameter);
        this.getOssClient(parameter.getFileStorageId(), ossClientCache.isEncryptedYn()).putObject(bucketName, diskFilePath.getFilePath(true), inputStream);
        return FileStorageSaveResult.builder()
                .fileStoreKey(diskFilePath.getFileId())
                .fileStorageId(ossClientCache.getFileStorageId())
                .encryptedYn(ossClientCache.isEncryptedYn())
                .build();
    }

    /**
     * 删除文件
     *
     * @param parameter  参数
     * @param bucketName 存储桶
     */
    @Override
    public void delete(FileStorageDeleteParameter parameter, String bucketName) {
        OssClientCache clientCache = this.getOssClientCache(parameter.getFileStorageId());
        if (bucketName == null) {
            bucketName = clientCache.getProperties().getBucketName();
        }
        for (FileStorageDeleteParameter.FileStorageDeleteItem item : parameter.getFileStoreList()) {
            this.getOssClient(parameter.getFileStorageId(), item.isEncryptedYn()).deleteObject(bucketName, this.getObject(item.getFileStoreKey()));
        }
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
        OssClientCache clientCache = this.getOssClientCache(parameter.getFileStorageId());
        if (bucketName == null) {
            bucketName = clientCache.getProperties().getBucketName();
        }
        return this.getOssClient(clientCache.getFileStorageId(), parameter.isEncryptedYn())
                .getObject(bucketName, this.getObject(parameter.getStorageStoreKey()))
                .getObjectContent();
    }

    protected String getObject(String id) {
        return DiskFilePathBO.createById(id, "").getFilePath(true);
    }

    /**
     * 获取oss客户端
     *
     * @param id 配置信息
     * @return OSS客户端
     */
    @Override
    public OSS getOssClient(Long id) {
        OssClientCache ossClientCache = this.getOssClientCache(id);
        return ossClientCache.isEncryptedYn() ? ossClientCache.getOssEncryptionClient() : ossClientCache.getOssClient();
    }

    private OSS getOssClient(Long id, boolean encryptedYn) {
        OssClientCache ossClientCache = this.getOssClientCache(id);
        if (encryptedYn) {
            return ossClientCache.getOssEncryptionClient();
        }
        return ossClientCache.getOssClient();
    }

    /**
     * 初始化
     *
     * @param initProperties 初始化参数
     */
    @Override
    public void init(FileStorageInitProperties initProperties) {
        if (OSS_CLIENT_CACHE_MAP.containsKey(initProperties.getFileStorageId())) {
            return;
        }
        SmartFileStorageAliyunOssProperties ossProperties = JsonUtils.parse(initProperties.getProperties(), SmartFileStorageAliyunOssProperties.class);
        OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKey(), ossProperties.getSecretKey());
        OssClientCache.OssClientCacheBuilder builder = OssClientCache.builder()
                .ossClient(ossClient)
                .properties(ossProperties)
                .privateKey(initProperties.getPrivateKey())
                .encryptedYn(initProperties.isEncryptedYn())
                .fileStorageId(initProperties.getFileStorageId())
                .publicKey(initProperties.getPublicKey());
        // 构建加密客户端
        if (initProperties.isEncryptedYn()) {
            PrivateKey privateKey = RsaUtils.generaPrivateKey(initProperties.getPrivateKey());
            PublicKey publicKey = RsaUtils.generaPublicKey(initProperties.getPublicKey());
            KeyPair keyPair = new KeyPair(publicKey, privateKey);

            SimpleRSAEncryptionMaterials encryptionMaterials = new SimpleRSAEncryptionMaterials(keyPair, Map.of("id", initProperties.getFileStorageId().toString()));
            OSSEncryptionClient ossEncryptionClient = new OSSEncryptionClientBuilder().
                    build(ossProperties.getEndpoint(), ossProperties.getAccessKey(), ossProperties.getSecretKey(), encryptionMaterials);
            builder.ossEncryptionClient(ossEncryptionClient);
        }
        OSS_CLIENT_CACHE_MAP.put(initProperties.getFileStorageId(), builder.build());
    }

    /**
     * 根据ID销毁存储器
     *
     * @param fileStorageId 存储器ID
     */
    @Override
    public void destroy(Long fileStorageId) {
        OssClientCache ossClientCache = OSS_CLIENT_CACHE_MAP.get(fileStorageId);
        if (ossClientCache == null) {
            return;
        }
        OSS ossClient = ossClientCache.getOssClient();
        if (ossClient != null) {
            ossClient.shutdown();
        }
        OSS ossEncryptionClient = ossClientCache.getOssEncryptionClient();
        if (ossEncryptionClient != null) {
            ossEncryptionClient.shutdown();
        }
        OSS_CLIENT_CACHE_MAP.remove(fileStorageId);
    }

    /**
     * 对象销毁时销毁 ossclient
     */
    @Override
    public void destroy() {
        this.destroy(new ArrayList<>(OSS_CLIENT_CACHE_MAP.keySet()));
    }
}
