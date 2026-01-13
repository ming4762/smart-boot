package com.smart.cloud.api.file.feign.fallback;

import com.smart.cloud.api.file.feign.FeignSmartFileApi;
import com.smart.cloud.starter.feign.exception.SmartFeignBusinessException;
import com.smart.module.api.file.bo.FileHandlerResult;
import com.smart.module.api.file.dto.FilenameDownloadParameter;
import feign.FeignException;
import feign.Response;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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

            private <T> T doReturn() {
                this.errorLog();
                if (cause instanceof FeignException feignException) {
                    throw new SmartFeignBusinessException(feignException);
                }
                return null;
            }

            @Override
            public Response download(@NonNull Long id) {
                this.errorLog();
                return this.doReturn();
            }

            /**
             * 下载文件
             *
             * @param parameter 文件名下载参数
             * @return 下载内容
             */
            @Override
            public Response download(@NonNull FilenameDownloadParameter parameter) {
                this.errorLog();
                return this.doReturn();
            }

            @Override
            public List<FileHandlerResult> batchDelete(@NonNull Collection<Long> fileIds) {
                this.errorLog();
                return this.doReturn();
            }

            @Override
            public FileHandlerResult delete(@NonNull Long fileId) {
                this.errorLog();
                return this.doReturn();
            }

            @Override
            public FileHandlerResult save(Map<String,Object> parameter) {
                this.errorLog();
                return this.doReturn();
            }

            @Override
            public List<String> listAddress(List<Long> idList) {
                this.errorLog();
                return this.doReturn();
            }
        };
    }
}
