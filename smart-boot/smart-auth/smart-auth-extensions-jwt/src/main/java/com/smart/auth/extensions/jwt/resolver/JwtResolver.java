package com.smart.auth.extensions.jwt.resolver;

import com.smart.auth.core.userdetails.RestUserDetails;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletRequest;

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
     * @param request 请求体
     * @return 解析结果
     */
    RestUserDetails resolver(@NonNull String jwt, HttpServletRequest request);

    /**
     * 创建JWT
     * @param userDetails 用户信息
     * @return jwt字符串
     */
    String create(RestUserDetails userDetails);
}
