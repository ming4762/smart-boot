package com.smart.cloud.api.system.feign.fallback;

import com.smart.cloud.api.system.feign.RemoteSysParameterApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
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
@Slf4j
public class RemoteSysParameterApiFallback implements FallbackFactory<RemoteSysParameterApi> {

    @Override
    public RemoteSysParameterApi create(Throwable cause) {
        return new RemoteSysParameterApi() {

            private void errorLog() {
                log.error("RemoteSysParameterApiFallback", cause);
            }
            @Override
            public String getParameter(@NonNull String code) {
                this.errorLog();
                return "";
            }

            @Override
            @NonNull
            public Map<String, String> getParameter(@NonNull List<String> codeList) {
                this.errorLog();
                return Map.of();
            }
        };
    }
}
