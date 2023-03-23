package com.smart.file.manager.api.remote;

import com.smart.file.manager.api.local.LocalSmartFileApi;
import com.smart.module.api.file.SmartFileApi;
import com.smart.module.api.file.bo.FileDownloadResult;
import com.smart.module.api.file.bo.FileHandlerResult;
import com.smart.module.api.file.constants.SmartFileApiUrlConstants;
import com.smart.module.api.file.dto.RemoteFileSaveParameter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 文件存储接口
 * @author zhongming4762
 * 2023/3/21
 */
@RestController
@RequestMapping
public class RemoteSmartFileApiController implements SmartFileApi {

    private final LocalSmartFileApi smartFileApi;

    public RemoteSmartFileApiController(LocalSmartFileApi smartFileApi) {
        this.smartFileApi = smartFileApi;
    }

    /**
     * 下载文件
     *
     * @param id 文件ID
     * @return 下载内容
     */
    @SneakyThrows(IOException.class)
    @Override
    @PostMapping(SmartFileApiUrlConstants.DOWNLOAD_FILE)
    public FileDownloadResult download(@NonNull @RequestBody Long id) {
        FileDownloadResult downloadResult = this.smartFileApi.download(id);
        HttpServletResponse response = Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getResponse)
                .orElse(null);
        if (response != null) {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + downloadResult.getFilename());
            IOUtils.copy(downloadResult.getInputStream(), response.getOutputStream());
        }
        return null;
    }

    /**
     * 批量删除文件信息
     *
     * @param fileIds 文件ID列表
     * @return 删除结果
     */
    @Override
    @PostMapping(SmartFileApiUrlConstants.BATCH_DELETE)
    public List<FileHandlerResult> batchDelete(@NonNull @RequestBody Collection<Long> fileIds) {
        return this.smartFileApi.batchDelete(fileIds);
    }

    /**
     * 保存文件
     *
     * @param parameter 保存参数
     * @return 文件信息
     */
    @Override
    @PostMapping(SmartFileApiUrlConstants.SAVE)
    public FileHandlerResult save(RemoteFileSaveParameter parameter) {
        return this.smartFileApi.save(parameter);
    }

    /**
     * 删除文件
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    @Override
    @PostMapping(SmartFileApiUrlConstants.DELETE)
    public FileHandlerResult delete(@NonNull @RequestBody Long fileId) {
        return SmartFileApi.super.delete(fileId);
    }
}
