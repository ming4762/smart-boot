package com.smart.cloud.api.file.feign;

import com.smart.cloud.common.core.constants.CloudServiceNameConstants;
import com.smart.module.api.file.bo.FileHandlerResult;
import com.smart.module.api.file.constants.SmartFileApiUrlConstants;
import com.smart.module.api.file.dto.RemoteFileSaveParameter;
import feign.Response;
import lombok.NonNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;
import java.util.List;

/**
 * @author zhongming4762
 * 2023/3/21 14:53
 */
@FeignClient(value = CloudServiceNameConstants.FILE_SERVICE, contextId = "remoteSmartFileApi")
public interface FeignSmartFileApi {

    /**
     * 下载文件
     *
     * @param id 文件ID
     * @return 下载内容
     */
    @PostMapping(SmartFileApiUrlConstants.DOWNLOAD_FILE)
    Response download(@NonNull Long id);

    /**
     * 批量删除文件信息
     *
     * @param fileIds 文件ID列表
     * @return 删除结果
     */
    @PostMapping(SmartFileApiUrlConstants.BATCH_DELETE)
    List<FileHandlerResult> batchDelete(@NonNull Collection<Long> fileIds);

    /**
     * 删除文件
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    @PostMapping(SmartFileApiUrlConstants.DELETE)
    FileHandlerResult delete(@NonNull Long fileId);

    /**
     * 保存文件
     *
     * @param parameter 保存参数
     * @return 文件信息
     */
    @PostMapping(SmartFileApiUrlConstants.SAVE)
    FileHandlerResult save(RemoteFileSaveParameter parameter);
}
