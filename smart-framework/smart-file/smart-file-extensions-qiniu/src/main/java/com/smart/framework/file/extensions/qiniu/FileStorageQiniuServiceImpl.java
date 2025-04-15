package com.smart.framework.file.extensions.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.DownloadUrl;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.commons.core.utils.RestUtils;
import com.smart.framework.file.core.common.FileStorageServiceRegisterName;
import com.smart.framework.file.core.parameter.FileStorageDeleteParameter;
import com.smart.framework.file.core.parameter.FileStorageGetParameter;
import com.smart.framework.file.core.parameter.FileStorageInitProperties;
import com.smart.framework.file.core.parameter.FileStorageSaveParameter;
import com.smart.framework.file.core.pojo.bo.DiskFilePathBO;
import com.smart.framework.file.core.pojo.dto.FileStorageSaveResult;
import com.smart.framework.file.core.properties.SmartFileStorageQiniuProperties;
import com.smart.module.api.file.constants.FileStorageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhongming4762
 * 2023/3/30
 */
@Slf4j
public class FileStorageQiniuServiceImpl implements QiniuService {

    private static final Map<Long, QiniuClientCache> CLIENT_CACHE = new ConcurrentHashMap<>(16);

    @Getter
    @AllArgsConstructor
    @Builder
    private static class QiniuClientCache {
        private Long fileStorageId;
        private boolean encryptedYn;
        private SmartFileStorageQiniuProperties properties;

        private Auth auth;

        private String uploadToken;

        private UploadManager uploadManager;

        private BucketManager bucketManager;
    }

    private QiniuClientCache getClientCache(Long id) {
        return CLIENT_CACHE.get(id);
    }

    /**
     * 初始化
     *
     * @param initProperties 初始化参数
     */
    @Override
    public void init(FileStorageInitProperties initProperties) {
        CLIENT_CACHE.computeIfAbsent(initProperties.getFileStorageId(), key -> {
            SmartFileStorageQiniuProperties properties = JsonUtils.parse(initProperties.getProperties(), SmartFileStorageQiniuProperties.class);
            Configuration configuration = new Configuration();
            UploadManager uploadManager = new UploadManager(configuration);
            Auth auth = Auth.create(properties.getAccessKey(), properties.getSecretKey());
            String uploadToken = auth.uploadToken(properties.getBucketName());
            return QiniuClientCache.builder()
                    .fileStorageId(initProperties.getFileStorageId())
                    .encryptedYn(initProperties.isEncryptedYn())
                    .properties(properties)
                    .auth(auth)
                    .uploadToken(uploadToken)
                    .uploadManager(uploadManager)
                    .bucketManager(new BucketManager(auth, configuration))
                    .build();
        });
    }

    /**
     * 根据ID销毁存储器
     *
     * @param fileStorageId 存储器ID
     */
    @Override
    public void destroy(Long fileStorageId) {
        QiniuClientCache qiniuClientCache = CLIENT_CACHE.get(fileStorageId);
        if (qiniuClientCache == null) {
            return;
        }
        CLIENT_CACHE.remove(fileStorageId);
    }


    @Override
    public void destroy() throws Exception {
        this.destroy(new ArrayList<>(CLIENT_CACHE.keySet()));
    }

    /**
     * 获取注册名字
     *
     * @return 注册名字
     */
    @Override
    public FileStorageServiceRegisterName getRegisterName() {
        return FileStorageServiceRegisterName.builder()
                .storageType(FileStorageTypeEnum.QINIU)
                .beanName(FileStorageTypeEnum.QINIU.getServiceName())
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
        return this.getObjectUrl(parameter, Duration.ofDays(30));
    }

    /**
     * 获取文件外链
     *
     * @param parameter 参数
     * @param expiry    过期时间（单位秒）
     * @return 文件外链
     */
    @Override
    @SneakyThrows(QiniuException.class)
    public String getObjectUrl(FileStorageGetParameter parameter, Duration expiry) {
        QiniuClientCache clientCache = this.getClientCache(parameter.getFileStorageId());
        DownloadUrl downloadUrl = new DownloadUrl(clientCache.getProperties().getUrl(), Boolean.TRUE.equals(clientCache.getProperties().getUseHttps()), parameter.getStorageStoreKey());
        long deadline = System.currentTimeMillis() / 1000 + expiry.getSeconds();
        return downloadUrl.buildURL(clientCache.getAuth(), deadline);
    }

    /**
     * 保存文件
     *
     * @param inputStream 输入流
     * @param parameter   参数
     * @param bucketName  bucket
     * @return key
     */
    @SneakyThrows(QiniuException.class)
    @Override
    public FileStorageSaveResult save(@NonNull InputStream inputStream, @NonNull FileStorageSaveParameter parameter, String bucketName) {
        QiniuClientCache clientCache = this.getClientCache(parameter.getFileStorageId());
        DiskFilePathBO diskFilePath = new DiskFilePathBO("", parameter);
        String uploadToken = clientCache.getUploadToken();
        if (StringUtils.isNotBlank(bucketName)) {
            uploadToken = clientCache.getAuth().uploadToken(bucketName);
        }
        Response response = clientCache.getUploadManager().put(inputStream, diskFilePath.getFilePath(true), uploadToken, null, null);
        DefaultPutRet putRet = JsonUtils.parse(response.bodyString(), DefaultPutRet.class);
        return FileStorageSaveResult.builder()
               .fileStoreKey(putRet.key)
               .fileStorageId(parameter.getFileStorageId())
               .encryptedYn(clientCache.isEncryptedYn())
               .build();
    }

    /**
     * 删除文件
     *
     * @param parameter  参数
     * @param bucketName bucket
     */
    @Override
    @SneakyThrows(QiniuException.class)
    public void delete(@NonNull FileStorageDeleteParameter parameter, String bucketName) {
        List<FileStorageDeleteParameter.FileStorageDeleteItem> itemList = parameter.getFileStoreList();
        if (CollectionUtils.isEmpty(itemList)) {
            return;
        }
        QiniuClientCache clientCache = this.getClientCache(parameter.getFileStorageId());
        if (StringUtils.isBlank(bucketName)) {
            bucketName = clientCache.getProperties().getBucketName();
        }
        BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
        batchOperations.addDeleteOp(bucketName, itemList.stream().map(FileStorageDeleteParameter.FileStorageDeleteItem::getFileStoreKey).toList().toArray(new String[]{}));
        clientCache.getBucketManager().batch(batchOperations);
    }

    /**
     * 下载文件
     *
     * @param parameter  参数
     * @param bucketName bucket
     * @return 输入流
     */
    @SneakyThrows(IOException.class)
    @Override
    public InputStream download(@NonNull FileStorageGetParameter parameter, String bucketName) {
        File tempFile = File.createTempFile("qiniu", null);
        // 系统退出后删除临时文件
        tempFile.deleteOnExit();
        // 写入临时文件
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tempFile))) {
            this.download(parameter, outputStream);
            return new FileInputStream(tempFile);
        }
    }

    /**
     * 下载文件
     *
     * @param parameter    参数
     * @param outputStream 输出流
     */
    @Override
    public void download(@NonNull FileStorageGetParameter parameter, OutputStream outputStream) {
        String objectUrl = this.getObjectUrl(parameter, Duration.ofDays(1));
        RestUtils.download(objectUrl, HttpMethod.GET, null, null, outputStream, null);
    }
}
