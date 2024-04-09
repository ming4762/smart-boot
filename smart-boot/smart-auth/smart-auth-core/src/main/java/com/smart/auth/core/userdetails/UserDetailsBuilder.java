package com.smart.auth.core.userdetails;

import com.smart.module.api.system.dto.AuthUserDTO;
import org.springframework.lang.Nullable;

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
