package com.smart.framework.auth.core.userdetails;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.module.api.system.dto.AuthUserDTO;
import org.jspecify.annotations.Nullable;

/**
 * UserDetails 构建
 * @author shizhongming
 * 2024/4/9 9:52
 * @since 3.0.0
 */
public interface UserDetailsBuilder {

    /**
     * 构建 RestUserDetails
     * @param user 用户信息
     * @return RestUserDetails
     */
    RestUserDetails buildUserDetails(@Nullable AuthUserDTO user);
}
