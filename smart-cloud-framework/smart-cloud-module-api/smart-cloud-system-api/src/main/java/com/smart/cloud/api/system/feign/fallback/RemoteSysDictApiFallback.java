package com.smart.cloud.api.system.feign.fallback;

import com.smart.cloud.api.system.feign.RemoteSysDictApi;
import com.smart.module.api.system.dto.SysDictItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author shizhongming
 * 2025/6/13 19:17
 * @since 5.0.0
 */
@Component
@Slf4j
public class RemoteSysDictApiFallback implements FallbackFactory<RemoteSysDictApi> {

    @Override
    public RemoteSysDictApi create(Throwable cause) {
        return new RemoteSysDictApi() {

            private void errorLog() {
                log.error("RemoteSysDictApiFallback", cause);
            }

            @Override
            public List<SysDictItemDTO> listByDictCode(String dictCode) {
                this.errorLog();
                return List.of();
            }

            @Override
            public Map<String, List<SysDictItemDTO>> listByDictCode(List<String> dictCode) {
                this.errorLog();
                return Map.of();
            }
        };
    }
}
