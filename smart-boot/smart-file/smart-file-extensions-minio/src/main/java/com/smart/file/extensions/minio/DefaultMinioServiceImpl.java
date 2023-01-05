package com.smart.file.extensions.minio;

import com.smart.file.core.SmartFileProperties;
import com.smart.file.core.common.ActualFileServiceRegisterName;
import com.smart.file.core.constants.ActualFileServiceEnum;
import com.smart.file.core.model.FileSaveParameter;
import com.smart.file.core.pojo.bo.DiskFilePathBO;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.lang.NonNull;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.List;

/**
 * @author zhongming4762
 * 2022/12/31 21:17
 */
public class DefaultMinioServiceImpl implements MinioService {

    private final MinioClient minioClient;

    private final SmartFileProperties.MinioProperties minioProperties;

    public DefaultMinioServiceImpl(MinioClient minioClient, SmartFileProperties properties) {
        this.minioClient = minioClient;
        this.minioProperties = properties.getMinio();
    }

    protected String getBucket() {
        return this.minioProperties.getBucketName();
    }

    /**
     * 保存文件
     *
     * @param file     文件
     * @param parameter 参数
     * @return 文件id
     * @throws IOException 文件写入失败抛出异常
     */
    @Override
    @NonNull
    public String save(@NonNull File file, @NonNull FileSaveParameter parameter) throws IOException {
        return this.save(this.getBucket(), file, parameter.getFilename(), parameter.getPath());
    }

    /**
     * 保存文件
     *
     * @param inputStream 文件流
     * @param parameter   参数
     * @return 文件ID
     */
    @SneakyThrows(IOException.class)
    @Override
    @NonNull
    public String save(@NonNull InputStream inputStream, @NonNull FileSaveParameter parameter) {
        return this.save(this.getBucket(), inputStream, parameter.getFilename(), parameter.getPath());
    }

    /**
     * 删除文件
     *
     * @param id 文件ID
     */
    @Override
    public void delete(@NonNull String id) {
        this.delete(this.minioProperties.getBucketName(), id);
    }

    /**
     * 批量删除文件
     *
     * @param fileIdList 文件ID
     */
    @Override
    public void batchDelete(@NonNull List<String> fileIdList) {
        fileIdList.forEach(this::delete);
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储桶
     * @param id         文件ID
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
    public void delete(String bucketName, String id) {
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(this.getObject(id))
                .build();
        this.minioClient.removeObject(removeObjectArgs);
    }

    /**
     * 下载文件
     *
     * @param id 文件id
     * @return 文件流
     */
    @Override
    public InputStream download(@NonNull String id) {
        return this.download(this.minioProperties.getBucketName(), id);
    }

    /**
     * 下载文件
     *
     * @param id           文件ID
     * @param outputStream 输出流
     */
    @SneakyThrows(IOException.class)
    @Override
    public void download(@NonNull String id, @NonNull OutputStream outputStream) {
        try (InputStream inputStream = this.download(id)) {
            IOUtils.copy(inputStream, outputStream);
        }
    }

    /**
     * 下载文件
     *
     * @param bucketName 存储桶
     * @param id         文件ID
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
    public InputStream download(String bucketName, String id) {
        GetObjectArgs objectArgs = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(this.getObject(id))
                .build();
        return this.minioClient.getObject(objectArgs);
    }

    /**
     * 获取文件的绝对路径
     *
     * @param id 文件ID
     * @return 文件绝对路径
     */
    @Override
    public String getAbsolutePath(@NonNull String id) {
        return this.getObjectUrl(id, null);
    }

    /**
     * 获取注册名字
     *
     * @return 注册名字
     */
    @Override
    public ActualFileServiceRegisterName getRegisterName() {
        return ActualFileServiceRegisterName.builder()
                .dbName(ActualFileServiceEnum.MINIO.name())
                .beanName(ActualFileServiceEnum.MINIO.getServiceName())
                .build();
    }


    /**
     * 判断存储桶是否存在
     *
     * @param bucketName 存储桶名字
     * @return boolean
     */
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
    @Override
    public boolean bucketExists(String bucketName) {
        return this.minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 创建存储桶
     *
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
    public boolean makeBucket(String bucketName) {
        this.minioClient.makeBucket(
                MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build()
        );
        return true;
    }

    /**
     * 删除存储桶
     *
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
    public boolean removeBucket(String bucketName) {
        this.minioClient.removeBucket(
                RemoveBucketArgs.builder()
                        .bucket(bucketName)
                        .build()
        );
        return true;
    }

    /**
     * 查询存储桶列表
     *
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
    public List<Bucket> listBuckets() {
        return this.minioClient.listBuckets();
    }

    /**
     * 查询存储桶列表
     *
     * @param args 参数
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
    public List<Bucket> listBuckets(ListBucketsArgs args) {
        return this.minioClient.listBuckets(args);
    }

    /**
     * 保存文件
     *
     * @param file       文件
     * @param filename   文件名
     * @param bucketName 存储桶名字
     * @return 文件id
     * @throws IOException 文件写入失败抛出异常
     */
    @Override
    @NonNull
    public String save(String bucketName, @NonNull File file, String filename, String folder) throws IOException {
        try (InputStream inputStream = Files.newInputStream(file.toPath())) {
            return this.save(bucketName, inputStream, filename, folder);
        }
    }

    /**
     * 保存文件
     *
     * @param inputStream 文件流
     * @param filename    文件名
     * @param folder      文件夹
     * @param bucketName  存储桶名字
     * @return 文件ID
     */
    @Override
    @NonNull
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
    public String save(String bucketName, @NonNull InputStream inputStream, String filename, String folder) {
        DiskFilePathBO filePath = new DiskFilePathBO("", FileSaveParameter.create(filename, folder));
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(filePath.getFilePath())
                .contentType("application/octet-stream")
                .stream(inputStream, inputStream.available(), -1)
                .build();
        this.minioClient.putObject(putObjectArgs);
        return filePath.getFileId();
    }

    /**
     * 获取文件外链
     *
     * @param bucketName 存储桶
     * @param fileId     文件ID
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
    public String getObjectUrl(String bucketName, String fileId, Duration expiry) {
        GetPresignedObjectUrlArgs.Builder builder = GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .object(this.getObject(fileId))
                .method(Method.GET);
        if (expiry != null) {
            builder.expiry(Long.valueOf(expiry.getSeconds()).intValue());
        }
        GetPresignedObjectUrlArgs urlArgs = builder.build();
        String objectUrl = this.minioClient.getPresignedObjectUrl(urlArgs);
        return URLEncoder.encode(objectUrl, StandardCharsets.UTF_8.name());
    }

    /**
     * 获取文件外链
     *
     * @param fileId 文件ID
     * @param expiry 过期时间（单位秒）
     * @return 文件外链
     */
    @Override
    public String getObjectUrl(String fileId, Duration expiry) {
        return this.getObjectUrl(this.minioProperties.getBucketName(), fileId, expiry);
    }

    protected String getObject(String id) {
        DiskFilePathBO filePath = DiskFilePathBO.createById(id, "");
        return filePath.getFilePath();
    }
}
