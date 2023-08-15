package com.smart.file.extensions.aliyun.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.smart.commons.core.utils.JsonUtils;
import com.smart.file.core.common.FileStorageServiceRegisterName;
import com.smart.file.core.parameter.FileStorageDeleteParameter;
import com.smart.file.core.parameter.FileStorageGetParameter;
import com.smart.file.core.parameter.FileStorageSaveParameter;
import com.smart.file.core.pojo.bo.DiskFilePathBO;
import com.smart.file.core.properties.SmartFileStorageAliyunOssProperties;
import com.smart.module.api.file.constants.FileStorageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.lang.NonNull;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * aliyun oss服务类
 * @author zhongming4762
 * 2023/3/4
 */
@Slf4j
public class AliyunOssServiceImpl implements AliyunOssService, DisposableBean {

    private static final Map<String, OssClientCache> OSS_CLIENT_CACHE_MAP = new ConcurrentHashMap<>();

    protected OssClientCache getOssClientCache(String properties) {
        return OSS_CLIENT_CACHE_MAP.computeIfAbsent(properties, key -> {
            SmartFileStorageAliyunOssProperties ossProperties = JsonUtils.parse(key, SmartFileStorageAliyunOssProperties.class);
            OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKey(), ossProperties.getSecretKey());
            return new OssClientCache(ossClient, ossProperties);
        });
    }

    @Getter
    @AllArgsConstructor
    private static class OssClientCache {
        private OSS ossClient;

        private SmartFileStorageAliyunOssProperties properties;
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
        OssClientCache clientCache = this.getOssClientCache(parameter.getStorageProperties());

        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        URL url = clientCache.getOssClient().generatePresignedUrl(clientCache.getProperties().getBucketName(), this.getObject(parameter.getFileStorageKey()), expiration);
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
    public String save(FileStorageSaveParameter parameter, String bucketName, @NonNull InputStream inputStream) {
        OssClientCache ossClientCache = this.getOssClientCache(parameter.getStorageProperties());
        if (bucketName == null) {
            bucketName = ossClientCache.getProperties().getBucketName();
        }
        DiskFilePathBO diskFilePath = new DiskFilePathBO("", parameter);
        ossClientCache.getOssClient().putObject(bucketName, diskFilePath.getFilePath(true), inputStream);
        return diskFilePath.getFileId();
    }

    /**
     * 删除文件
     *
     * @param parameter  参数
     * @param bucketName 存储桶
     */
    @Override
    public void delete(FileStorageDeleteParameter parameter, String bucketName) {
        OssClientCache clientCache = this.getOssClientCache(parameter.getStorageProperties());
        if (bucketName == null) {
            bucketName = clientCache.getProperties().getBucketName();
        }
        for (String key : parameter.getFileStoreKeyList()) {
            clientCache.getOssClient().deleteObject(bucketName, this.getObject(key));
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
        OssClientCache clientCache = this.getOssClientCache(parameter.getStorageProperties());
        if (bucketName == null) {
            bucketName = clientCache.getProperties().getBucketName();
        }
        return clientCache.getOssClient()
                .getObject(bucketName, this.getObject(parameter.getFileStorageKey()))
                .getObjectContent();
    }

    protected String getObject(String id) {
        return DiskFilePathBO.createById(id, "").getFilePath(true);
    }

    /**
     * 获取oss客户端
     *
     * @param storageProperties 配置信息
     * @return OSS客户端
     */
    @Override
    public OSS getOssClient(String storageProperties) {
        return this.getOssClientCache(storageProperties).getOssClient();
    }

    /**
     * 对象销毁时销毁 ossclient
     */
    @Override
    public void destroy() {
        OSS_CLIENT_CACHE_MAP.forEach((key, value) -> {
            try {
                value.getOssClient().shutdown();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }
}
