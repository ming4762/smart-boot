package com.smart.framework.auth.core.utils;

import com.smart.framework.auth.core.matcher.ExtensionPathMatcher;
import com.smart.framework.auth.core.properties.AuthIgnoreProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;

import java.util.HashSet;
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
    public static boolean checkIgnores(@NonNull HttpServletRequest request, @NonNull AuthIgnoreProperties authIgnoreProperties) {
        String method = request.getMethod();
        HttpMethod httpMethod = HttpMethod.valueOf(method);

        Set<String> ignores = HashSet.newHashSet(16);
        if (GET.equals(httpMethod)) {
            ignores.addAll(authIgnoreProperties
                    .getGet());
        } else if (PUT.equals(httpMethod)) {
            ignores.addAll(authIgnoreProperties
                    .getPut());
        } else if (HEAD.equals(httpMethod)) {
            ignores.addAll(authIgnoreProperties
                    .getHead());
        } else if (POST.equals(httpMethod)) {
            ignores.addAll(authIgnoreProperties
                    .getPost());
        } else if (PATCH.equals(httpMethod)) {
            ignores.addAll(authIgnoreProperties
                    .getPatch());
        } else if (TRACE.equals(httpMethod)) {
            ignores.addAll(authIgnoreProperties
                    .getTrace());
        } else if (DELETE.equals(httpMethod)) {
            ignores.addAll(authIgnoreProperties
                    .getDelete());
        } else if (OPTIONS.equals(httpMethod)) {
            ignores.addAll(authIgnoreProperties
                    .getOptions());
        }

        ignores.addAll(authIgnoreProperties
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
