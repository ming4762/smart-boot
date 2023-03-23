package com.smart.file.manager.api.local;

import com.smart.file.core.service.FileService;
import com.smart.module.api.file.SmartFileApi;
import com.smart.module.api.file.bo.FileDownloadResult;
import com.smart.module.api.file.bo.FileHandlerResult;
import com.smart.module.api.file.dto.RemoteFileSaveParameter;
import lombok.NonNull;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author zhongming4762
 * 2023/3/21
 */
@Component
@Primary
public class LocalSmartFileApi implements SmartFileApi {

    private final FileService fileService;

    public LocalSmartFileApi(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 下载文件
     *
     * @param id 文件ID
     * @return 下载内容
     */
    @Override
    public FileDownloadResult download(@NonNull Long id) {
        return this.fileService.download(id);
    }

    /**
     * 批量删除文件信息
     *
     * @param fileIds 文件ID列表
     * @return 删除结果
     */
    @Override
    public List<FileHandlerResult> batchDelete(@NonNull Collection<Long> fileIds) {
        return this.fileService.batchDelete(fileIds);
    }

    /**
     * 保存文件
     *
     * @param parameter 保存参数
     * @return 文件信息
     */
    @Override
    public FileHandlerResult save(RemoteFileSaveParameter parameter) {
        return this.fileService.save(parameter.getMultipartFile(), parameter);
    }
}
