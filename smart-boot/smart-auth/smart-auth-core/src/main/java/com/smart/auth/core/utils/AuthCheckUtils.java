package com.smart.auth.core.utils;

import com.google.common.collect.Sets;
import com.smart.auth.core.matcher.ExtensionPathMatcher;
import com.smart.auth.core.properties.AuthProperties;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Set;

/**
 * @author ShiZhongMing
 * @since 1.0.7
 */
public class AuthCheckUtils {

    private AuthCheckUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 请求是否不需要进行权限拦截
     * @param request 当前请求
     * @return true - 忽略，false - 不忽略
     */
    public static boolean checkIgnores(@NonNull HttpServletRequest request, @NonNull AuthProperties.IgnoreConfig ignoreConfig) {
        String method = request.getMethod();
        HttpMethod httpMethod = HttpMethod.resolve(method);
        if (Objects.isNull(httpMethod)) {
            httpMethod = HttpMethod.GET;
        }
        Set<String> ignores = Sets.newHashSet();
        switch (httpMethod) {
            case GET:
                ignores.addAll(ignoreConfig
                        .getGet());
                break;
            case PUT:
                ignores.addAll(ignoreConfig
                        .getPut());
                break;
            case HEAD:
                ignores.addAll(ignoreConfig
                        .getHead());
                break;
            case POST:
                ignores.addAll(ignoreConfig
                        .getPost());
                break;
            case PATCH:
                ignores.addAll(ignoreConfig
                        .getPatch());
                break;
            case TRACE:
                ignores.addAll(ignoreConfig
                        .getTrace());
                break;
            case DELETE:
                ignores.addAll(ignoreConfig
                        .getDelete());
                break;
            case OPTIONS:
                ignores.addAll(ignoreConfig
                        .getOptions());
                break;
            default:
                break;
        }

        ignores.addAll(ignoreConfig
                .getPattern());
        if (!ignores.isEmpty()) {
            for (String ignore : ignores) {
                ExtensionPathMatcher extensionPathMatcher = new ExtensionPathMatcher(httpMethod, ignore);
                if (extensionPathMatcher.matches(request)) {
                    return true;
                }
            }
        }
        return false;
    }
}
