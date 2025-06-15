package com.smart.cloud.api.system.feign.fallback;

import com.smart.cloud.api.system.feign.RemoteSystemAuthUserApi;
import com.smart.framework.commons.core.dto.auth.UserAccountData;
import com.smart.module.api.system.dto.AuthUserDTO;
import com.smart.module.api.system.dto.QueryUserAccountDTO;
import com.smart.module.api.system.parameter.UserAccountUnLockParameter;
import com.smart.module.api.system.parameter.WechatUserQueryParameter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author shizhongming
 * 2025/6/15 20:49
 * @since 5.0.0
 */
@Component
public class RemoteSystemAuthUserApiFallback implements RemoteSystemAuthUserApi {
    @Override
    public AuthUserDTO getByUsername(@NonNull String username) {
        return null;
    }

    @Override
    public AuthUserDTO getByMobile(@NonNull String mobile) {
        return null;
    }

    @Override
    public UserAccountData queryUserAccount(@NonNull QueryUserAccountDTO parameter) {
        return null;
    }

    @Override
    public AuthUserDTO getByAppOpenid(WechatUserQueryParameter parameter) {
        return null;
    }

    @Override
    public AuthUserDTO getByUnionid(WechatUserQueryParameter parameter) {
        return null;
    }

    @Override
    public boolean unlockAccount(UserAccountUnLockParameter parameter) {
        return false;
    }
}
