package com.smart.cloud.api.file.feign;

import com.smart.module.api.file.SmartFileApi;
import com.smart.module.api.file.bo.FileDownloadResult;
import com.smart.module.api.file.bo.FileHandlerResult;
import com.smart.module.api.file.dto.RemoteFileSaveParameter;
import feign.Response;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

/**
 * @author zhongming4762
 * 2023/3/21
 */
public class RemoteSmartFileApi implements SmartFileApi {

    private final FeignSmartFileApi feignSmartFileApi;

    public RemoteSmartFileApi(FeignSmartFileApi feignSmartFileApi) {
        this.feignSmartFileApi = feignSmartFileApi;
    }

    @SneakyThrows(IOException.class)
    @Override
    public FileDownloadResult download(@NonNull Long id) {
        Response response = this.feignSmartFileApi.download(id);
        FileDownloadResult result = new FileDownloadResult();
        result.setFileId(id);
        result.setInputStream(response.body().asInputStream());
        Collection<String> strings = response.headers().get(HttpHeaders.CONTENT_DISPOSITION);
        String filename = strings.stream()
                .findFirst()
                .map(item -> item.split("filename=")[1])
                .orElse("");
        result.setFilename(URLDecoder.decode(filename, StandardCharsets.UTF_8));
        return result;
    }

    @Override
    public List<FileHandlerResult> batchDelete(@NonNull Collection<Long> fileIds) {
        return this.feignSmartFileApi.batchDelete(fileIds);
    }

    @Override
    public FileHandlerResult save(RemoteFileSaveParameter parameter) {
        return this.feignSmartFileApi.save(parameter);
    }

    /**
     * 获取文件的访问地址
     *
     * @param idList ID 列表
     * @return 访问地址列表
     */
    @Override
    public List<String> listAddress(List<Long> idList) {
        return this.feignSmartFileApi.listAddress(idList);
    }

    @Override
    public FileHandlerResult delete(@NonNull Long fileId) {
        return SmartFileApi.super.delete(fileId);
    }
}
