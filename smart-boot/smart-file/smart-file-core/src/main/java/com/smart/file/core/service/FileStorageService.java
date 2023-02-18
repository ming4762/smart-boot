package com.smart.file.core.service;

import com.smart.file.core.common.FileStorageServiceRegisterName;
import com.smart.file.core.parameter.FileStorageDeleteParameter;
import com.smart.file.core.parameter.FileStorageGetParameter;
import com.smart.file.core.parameter.FileStorageSaveParameter;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件存储器服务
 * @author zhongming4762
 * 2023/2/16
 */
public interface FileStorageService {

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
    String save(@NonNull InputStream inputStream, @NonNull FileStorageSaveParameter parameter);

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
}
