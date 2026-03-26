package com.smart.module.file.api.remote;

import com.smart.module.api.file.SmartFileApi;
import com.smart.module.api.file.bo.FileDownloadResult;
import com.smart.module.api.file.bo.FileHandlerResult;
import com.smart.module.api.file.constants.SmartFileApiUrlConstants;
import com.smart.module.api.file.dto.FilenameDownloadParameter;
import com.smart.module.api.file.dto.RemoteFileSaveParameter;
import com.smart.module.file.api.local.LocalSmartFileApi;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    @Override
    @PostMapping(SmartFileApiUrlConstants.DOWNLOAD_FILE)
    public FileDownloadResult download(@NonNull @RequestBody Long id) {
        FileDownloadResult downloadResult = this.smartFileApi.download(id);
        this.doDownload(downloadResult);
        return null;
    }

    /**
     * 下载文件
     *
     * @param parameter 文件名下载参数
     * @return 下载内容
     */
    @Override
    @PostMapping(SmartFileApiUrlConstants.DOWNLOAD_FILE_BY_NAME)
    public FileDownloadResult download(@NonNull FilenameDownloadParameter parameter) {
        FileDownloadResult downloadResult = this.smartFileApi.download(parameter);
        this.doDownload(downloadResult);
        return null;
    }

    @SneakyThrows(IOException.class)
    private void doDownload(FileDownloadResult downloadResult) {
        if (downloadResult == null) {
            return;
        }
        HttpServletResponse response = Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getResponse)
                .orElse(null);
        if (response == null) {
            return;
        }
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(downloadResult.getFilename(), StandardCharsets.UTF_8));
        response.setHeader(FILE_ID_HEADER, downloadResult.getFileId().toString());
        IOUtils.copy(downloadResult.getInputStream(), response.getOutputStream());
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
    @PostMapping(path = SmartFileApiUrlConstants.SAVE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

    /**
     * 获取文件的访问地址
     *
     * @param idList ID 列表
     * @return 访问地址列表
     */
    @Override
    @PostMapping(SmartFileApiUrlConstants.LIST_ADDRESS)
    public List<String> listAddress(@RequestBody List<Long> idList) {
        return this.smartFileApi.listAddress(idList);
    }
}
