package com.smart.cloud.api.file.feign;

import com.smart.cloud.api.file.feign.fallback.FeignSmartFileApiFallback;
import com.smart.cloud.common.core.constants.CloudServiceNameConstants;
import com.smart.module.api.file.bo.FileHandlerResult;
import com.smart.module.api.file.constants.SmartFileApiUrlConstants;
import com.smart.module.api.file.dto.RemoteFileSaveParameter;
import feign.Response;
import lombok.NonNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;
import java.util.List;

/**
 * @author zhongming4762
 * 2023/3/21 14:53
 */
@FeignClient(value = CloudServiceNameConstants.FILE_SERVICE, fallbackFactory = FeignSmartFileApiFallback.class, contextId = "feignSmartFileApi")
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
     * 下载文件
     *
     * @param fileStorageCode 文件存储器代码
     * @param filename        文件名
     * @return 下载内容
     */
    @PostMapping(SmartFileApiUrlConstants.DOWNLOAD_FILE_BY_NAME)
    Response download(@NonNull String fileStorageCode, @NonNull String filename);


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
    @PostMapping(value = SmartFileApiUrlConstants.SAVE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileHandlerResult save(RemoteFileSaveParameter parameter);

    @PostMapping(SmartFileApiUrlConstants.LIST_ADDRESS)
    List<String> listAddress(List<Long> idList);

}
