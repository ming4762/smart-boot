package com.smart.cloud.api.system.feign.fallback;

import com.smart.cloud.api.system.feign.RemoteSysDictApi;
import com.smart.module.api.system.dto.SysDictItemDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author shizhongming
 * 2025/6/13 19:17
 * @since 5.0.0
 */
@Component
public class RemoteSysDictApiFallback implements RemoteSysDictApi {
    @Override
    public List<SysDictItemDTO> listByDictCode(String dictCode) {
        return List.of();
    }

    @Override
    public Map<String, List<SysDictItemDTO>> listByDictCode(List<String> dictCode) {
        return Map.of();
    }
}
