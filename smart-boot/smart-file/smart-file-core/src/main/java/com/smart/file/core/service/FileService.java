package com.smart.file.core.service;

import com.smart.module.api.file.bo.FileDownloadResult;
import com.smart.module.api.file.bo.FileHandlerResult;
import com.smart.module.api.file.dto.FileSaveParameter;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 文件服务类
 * @author zhongming4762
 * 2023/2/16
 */
public interface FileService {

    /**
     * 保存文件
     * @param file 文件
     * @param parameter 保存参数
     * @return 文件信息
     */
    FileHandlerResult save(@NonNull MultipartFile file, FileSaveParameter parameter);

    /**
     * 保存文件
     * @param inputStream 输入流
     * @param parameter 保存参数
     * @return 文件信息
     */
    FileHandlerResult save(@NonNull InputStream inputStream, FileSaveParameter parameter);

    /**
     * 保存文件
     * @param file 文件
     * @param parameter 保存参数
     * @return 文件信息
     */
    FileHandlerResult save(@NonNull File file, FileSaveParameter parameter);

    /**
     * 删除文件
     * @param fileId 文件ID
     * @return 文件信息
     */
    default FileHandlerResult delete(@NonNull Long fileId) {
        ArrayList<Long> deleteIds = new ArrayList<>(1);
        deleteIds.add(fileId);
        List<FileHandlerResult> fileHandlerResults = this.batchDelete(deleteIds);
        if (!CollectionUtils.isEmpty(fileHandlerResults)) {
            return fileHandlerResults.get(0);
        }
        return null;
    }

    /**
     * 批量删除文件信息
     * @param fileIds 文件ID列表
     * @return 删除结果
     */
    List<FileHandlerResult> batchDelete(@NonNull Collection<Long> fileIds);

    /**
     * 下载文件
     * @param id 文件ID
     * @return 下载内容
     */
    FileDownloadResult download(@NonNull Long id);

    /**
     * 下载文件
     * @param id 文件ID
     * @param outputStream 输出流
     * @return 文件下载结果
     */
    @SneakyThrows({IOException.class})
    default FileDownloadResult download(@NonNull Long id, OutputStream outputStream) {
        FileDownloadResult downloadResult = this.download(id);
        try (InputStream inputStream = downloadResult.getInputStream()) {
            IOUtils.copy(inputStream, outputStream);
        }
        downloadResult.setInputStream(null);
        return downloadResult;
    }
}
