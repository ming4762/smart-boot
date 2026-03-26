package com.smart.framework.file.core.service;

import com.smart.framework.file.core.common.FileStorageServiceRegisterName;
import com.smart.framework.file.core.parameter.FileStorageDeleteParameter;
import com.smart.framework.file.core.parameter.FileStorageGetParameter;
import com.smart.framework.file.core.parameter.FileStorageInitProperties;
import com.smart.framework.file.core.parameter.FileStorageSaveParameter;
import com.smart.framework.file.core.pojo.dto.FileStorageSaveResult;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.jspecify.annotations.NonNull;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 文件存储器服务
 * @author zhongming4762
 * 2023/2/16
 */
public interface FileStorageService extends DisposableBean {

    Logger LOGGER = LoggerFactory.getLogger(FileStorageService.class);

    /**
     * 获取注册名字
     * @return 注册名字
     */
    FileStorageServiceRegisterName getRegisterName();

    /**
     * 保存文件
     * @param inputStream 输入流
     * @param parameter 参数
     * @return 文件存储标识
     */
    FileStorageSaveResult save(@NonNull InputStream inputStream, @NonNull FileStorageSaveParameter parameter);

    /**
     * 删除文件
     * @param parameter 删除参数
     */
    void delete(@NonNull FileStorageDeleteParameter parameter);

    /**
     * 下载文件
     * @param parameter 文件下载参数
     * @return 文件流
     */
    InputStream download(@NonNull FileStorageGetParameter parameter);

    /**
     * 下载文件
     * @param parameter 参数
     * @param outputStream 输出流
     */
    @SneakyThrows(IOException.class)
    default void download(@NonNull FileStorageGetParameter parameter, OutputStream outputStream) {
        try (InputStream inputStream = this.download(parameter)) {
            IOUtils.copy(inputStream, outputStream);
        }
    }

    /**
     * 获取文件访问地址
     * @param parameter 参数
     * @return address
     */
    String getAddress(@NonNull FileStorageGetParameter parameter);

    /**
     * 初始化
     * @param initProperties 初始化参数
     */
    void init(FileStorageInitProperties initProperties);

    /**
     * 根据ID销毁存储器
     * @param fileStorageIdList 存储器列表
     */
    default void destroy(List<Long> fileStorageIdList) {
        if (CollectionUtils.isEmpty(fileStorageIdList)) {
            return;
        }
        for (Long id : fileStorageIdList) {
            try {
                this.destroy(id);
            } catch (Exception e) {
                LOGGER.error("destroy file storage error, id:{}", id, e);
            }
        }
    }

    /**
     * 根据ID销毁存储器
     * @param fileStorageId 存储器ID
     */
    void destroy(Long fileStorageId);
}
