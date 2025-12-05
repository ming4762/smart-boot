package com.smart.cloud.api.file.feign.fallback;

import com.smart.cloud.api.file.feign.RemoteSmartFileStorageApi;
import com.smart.module.api.file.dto.SmartFileStorageListDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author shizhongming
 * 2025/6/16 9:45
 * @since 5.0.0
 */
@Component
@Slf4j
public class RemoteSmartFileStorageApiFallback implements FallbackFactory<RemoteSmartFileStorageApi> {
    @Override
    public RemoteSmartFileStorageApi create(Throwable cause) {
        return new RemoteSmartFileStorageApi() {

            private void errorLog() {
                log.error("RemoteSmartFileStorageApiFallback", cause);
            }

            @Override
            public List<SmartFileStorageListDTO> listByIds(Collection<Long> idList) {
                this.errorLog();
                return List.of();
            }

            /**
             * 通过代码查询列表
             *
             * @param codeList 代码列表
             * @return 文件存储器列表
             */
            @Override
            public List<SmartFileStorageListDTO> listByCode(Collection<String> codeList) {
                this.errorLog();
                return List.of();
            }
        };
    }
}
