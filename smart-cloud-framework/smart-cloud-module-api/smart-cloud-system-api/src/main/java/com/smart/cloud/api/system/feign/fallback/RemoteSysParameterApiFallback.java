package com.smart.cloud.api.system.feign.fallback;

import com.smart.cloud.api.system.feign.RemoteSysParameterApi;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author shizhongming
 * 2025/6/15 20:47
 * @since 5.0.0
 */
@Component
public class RemoteSysParameterApiFallback implements RemoteSysParameterApi {
    @Override
    public String getParameter(@NonNull String code) {
        return "";
    }

    @Override
    @NonNull
    public Map<String, String> getParameter(@NonNull List<String> codeList) {
        return Map.of();
    }
}
