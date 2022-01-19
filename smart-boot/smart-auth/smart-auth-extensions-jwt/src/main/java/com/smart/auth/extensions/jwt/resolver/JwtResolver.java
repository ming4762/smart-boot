package com.smart.auth.extensions.jwt.resolver;

import com.smart.auth.core.userdetails.RestUserDetails;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;

/**
 * JWT解析器
 * @author ShiZhongMing
 * 2021/5/26 13:48
 * @since 1.0
 */
public interface JwtResolver extends Ordered {

    /**
     * 解析JWT
     * @param jwt jwt
     * @return 解析结果
     */
    RestUserDetails resolver(@NonNull String jwt);

    /**
     * 创建JWT
     * @param userDetails 用户信息
     * @return jwt字符串
     */
    String create(RestUserDetails userDetails);
}
