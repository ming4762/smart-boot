package com.smart.cloud.api.file.feign;

import com.smart.framework.commons.core.utils.BeanUtils;
import com.smart.module.api.file.SmartFileApi;
import com.smart.module.api.file.bo.FileDownloadResult;
import com.smart.module.api.file.bo.FileHandlerResult;
import com.smart.module.api.file.dto.FilenameDownloadParameter;
import com.smart.module.api.file.dto.RemoteFileSaveParameter;
import feign.Response;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhongming4762
 * 2023/3/21
 */
public class RemoteSmartFileApi implements SmartFileApi {

    private final FeignSmartFileApi feignSmartFileApi;

    public RemoteSmartFileApi(FeignSmartFileApi feignSmartFileApi) {
        this.feignSmartFileApi = feignSmartFileApi;
    }

    @Override
    public FileDownloadResult download(@NonNull Long id) {
        Response response = this.feignSmartFileApi.download(id);
        return this.buildFileDownloadResult(response);
    }

    /**
     * 下载文件
     *
     * @param parameter 文件名下载参数
     * @return 下载内容
     */
    @Override
    public FileDownloadResult download(@NonNull FilenameDownloadParameter parameter) {
        Response response = this.feignSmartFileApi.download(parameter);
        return this.buildFileDownloadResult(response);
    }

    @SneakyThrows(IOException.class)
    private FileDownloadResult buildFileDownloadResult(Response response) {
        FileDownloadResult result = new FileDownloadResult();
        String fileId = response.headers().get(FILE_ID_HEADER).stream().findFirst().orElse(null);
        result.setFileId(fileId == null ? null : Long.parseLong(fileId));
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
        Map<String, Object> flattenBean = BeanUtils.flattenBean(parameter);
        // 创建新的Map存储处理后的键值对
        Map<String, Object> processedMap = HashMap.newHashMap(flattenBean.size());

        for (Map.Entry<String, Object> entry : flattenBean.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            // 跳过null值
            if (value == null) {
                continue;
            }
            // 如果是枚举类型，使用toString值
            if (value instanceof Enum<?>) {
                processedMap.put(key, value.toString());
            } else {
                processedMap.put(key, value);
            }
        }
        return this.feignSmartFileApi.save(processedMap);

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
