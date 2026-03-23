package com.smart.cloud.api.system.feign.fallback;

import com.smart.cloud.api.system.feign.RemoteSystemAuthUserApi;
import com.smart.cloud.common.core.exception.SmartFallbackException;
import com.smart.framework.commons.core.dto.auth.UserAccountData;
import com.smart.module.api.system.dto.AuthUserDTO;
import com.smart.module.api.system.dto.QueryUserAccountDTO;
import com.smart.module.api.system.parameter.UserAccountUnLockParameter;
import com.smart.module.api.system.parameter.WechatUserQueryParameter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author shizhongming
 * 2025/6/15 20:49
 * @since 5.0.0
 */
@Component
@Slf4j
public class RemoteSystemAuthUserApiFallback implements FallbackFactory<RemoteSystemAuthUserApi> {

    @Override
    public RemoteSystemAuthUserApi create(Throwable cause) {
        return new RemoteSystemAuthUserApi() {

            private void errorLog() {
                log.error("RemoteSystemAuthUserApiFallback", cause);
            }

            @Override
            public AuthUserDTO getByUsername(@NonNull String username) {
                this.errorLog();
                throw new SmartFallbackException(cause);
            }

            @Override
            public AuthUserDTO getByMobile(@NonNull String mobile) {
                this.errorLog();
                throw new SmartFallbackException(cause);
            }

            @Override
            public UserAccountData queryUserAccount(@NonNull QueryUserAccountDTO parameter) {
                this.errorLog();
                throw new SmartFallbackException(cause);
            }

            @Override
            public AuthUserDTO getByWehchatAppOpenid(WechatUserQueryParameter parameter) {
                this.errorLog();
                throw new SmartFallbackException(cause);
            }

            @Override
            public AuthUserDTO getByWechatUnionid(WechatUserQueryParameter parameter) {
                this.errorLog();
                throw new SmartFallbackException(cause);
            }

            @Override
            public boolean unlockAccount(UserAccountUnLockParameter parameter) {
                this.errorLog();
                throw new SmartFallbackException(cause);
            }
        };
    }
}
