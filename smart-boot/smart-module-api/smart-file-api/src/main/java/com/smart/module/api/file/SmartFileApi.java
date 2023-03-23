package com.smart.module.api.file;

import com.smart.module.api.file.bo.FileDownloadResult;
import com.smart.module.api.file.bo.FileHandlerResult;
import com.smart.module.api.file.dto.RemoteFileSaveParameter;
import lombok.NonNull;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 文件处理API
 * @author zhongming4762
 * 2023/3/21 14:41
 */
public interface SmartFileApi {

    /**
     * 下载文件
     * @param id 文件ID
     * @return 下载内容
     */
    FileDownloadResult download(@NonNull Long id);

    /**
     * 批量删除文件信息
     * @param fileIds 文件ID列表
     * @return 删除结果
     */
    List<FileHandlerResult> batchDelete(@NonNull Collection<Long> fileIds);

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
     * 保存文件
     * @param parameter 保存参数
     * @return 文件信息
     */
    FileHandlerResult save(RemoteFileSaveParameter parameter);

}
