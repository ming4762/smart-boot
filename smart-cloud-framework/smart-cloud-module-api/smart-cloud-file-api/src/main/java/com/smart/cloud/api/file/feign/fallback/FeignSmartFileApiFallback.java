package com.smart.cloud.api.file.feign.fallback;

import com.smart.cloud.api.file.feign.FeignSmartFileApi;
import com.smart.module.api.file.bo.FileHandlerResult;
import com.smart.module.api.file.dto.RemoteFileSaveParameter;
import feign.Response;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author shizhongming
 * 2025/6/16 9:44
 * @since 5.0.0
 */
@Slf4j
@Component
public class FeignSmartFileApiFallback implements FallbackFactory<FeignSmartFileApi> {
    @Override
    public FeignSmartFileApi create(Throwable cause) {

        return new FeignSmartFileApi() {

            private void errorLog() {
                log.error("FeignSmartFileApiFallback", cause);
            }

            @Override
            public Response download(@NonNull Long id) {
                this.errorLog();
                return null;
            }

            /**
             * 下载文件
             *
             * @param fileStorageCode 文件存储器代码
             * @param filename        文件名
             * @return 下载内容
             */
            @Override
            public Response download(@NonNull String fileStorageCode, @NonNull String filename) {
                this.errorLog();
                return null;
            }

            @Override
            public List<FileHandlerResult> batchDelete(@NonNull Collection<Long> fileIds) {
                this.errorLog();
                return List.of();
            }

            @Override
            public FileHandlerResult delete(@NonNull Long fileId) {
                this.errorLog();
                return null;
            }

            @Override
            public FileHandlerResult save(RemoteFileSaveParameter parameter) {
                this.errorLog();
                return null;
            }

            @Override
            public List<String> listAddress(List<Long> idList) {
                this.errorLog();
                return List.of();
            }
        };
    }
}
