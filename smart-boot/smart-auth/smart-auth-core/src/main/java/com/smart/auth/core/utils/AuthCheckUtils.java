package com.smart.auth.core.utils;

import com.google.common.collect.Sets;
import com.smart.auth.core.matcher.ExtensionPathMatcher;
import com.smart.auth.core.properties.AuthProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;

import java.util.Objects;
import java.util.Set;

import static org.springframework.http.HttpMethod.*;

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
        HttpMethod httpMethod = HttpMethod.valueOf(method);

        Set<String> ignores = Sets.newHashSet();
        if (GET.equals(httpMethod)) {
            ignores.addAll(ignoreConfig
                    .getGet());
        } else if (PUT.equals(httpMethod)) {
            ignores.addAll(ignoreConfig
                    .getPut());
        } else if (HEAD.equals(httpMethod)) {
            ignores.addAll(ignoreConfig
                    .getHead());
        } else if (POST.equals(httpMethod)) {
            ignores.addAll(ignoreConfig
                    .getPost());
        } else if (PATCH.equals(httpMethod)) {
            ignores.addAll(ignoreConfig
                    .getPatch());
        } else if (TRACE.equals(httpMethod)) {
            ignores.addAll(ignoreConfig
                    .getTrace());
        } else if (DELETE.equals(httpMethod)) {
            ignores.addAll(ignoreConfig
                    .getDelete());
        } else if (OPTIONS.equals(httpMethod)) {
            ignores.addAll(ignoreConfig
                    .getOptions());
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
