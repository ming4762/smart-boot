package com.smart.cloud.api.auth.feign.fallback;

import com.smart.cloud.api.auth.feign.RemoteAuthApi;
import com.smart.framework.commons.core.message.Result;
import com.smart.module.api.auth.dto.AuthCacheDTO;
import com.smart.module.api.auth.dto.AuthUserDetailsDTO;
import com.smart.module.api.auth.dto.AuthenticationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author shizhongming
 * 2025/6/16 9:40
 * @since 5.0.0
 */
@Component
@Slf4j
public class RemoteAuthApiFallback implements FallbackFactory<RemoteAuthApi> {
    @Override
    public RemoteAuthApi create(Throwable cause) {
        return new RemoteAuthApi() {

            private void errorLog() {
                log.error("RemoteAuthApiFallback", cause);
            }

            @Override
            public boolean offlineByToken(@NonNull String token) {
                this.errorLog();
                return false;
            }

            @Override
            public boolean offlineByUsername(@NonNull String username) {
                this.errorLog();
                return false;
            }

            @Override
            public AuthUserDetailsDTO getUserDetails(@NonNull String token) {
                this.errorLog();
                return null;
            }

            @Override
            public Result<Boolean> authenticate(AuthenticationDTO parameter) {
                this.errorLog();
                return null;
            }

            @Override
            public Object getAuthCache(@NonNull String key) {
                this.errorLog();
                return null;
            }

            @Override
            public void setAuthCache(@NonNull AuthCacheDTO parameter) {
                this.errorLog();
            }

            @Override
            public void removeAuthCache(@NonNull String key) {
                this.errorLog();
            }
        };
    }
}
