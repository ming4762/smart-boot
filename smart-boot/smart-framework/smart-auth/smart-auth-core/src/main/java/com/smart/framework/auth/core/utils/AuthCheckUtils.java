package com.smart.framework.auth.core.utils;

import com.smart.framework.auth.core.matcher.ExtensionPathMatcher;
import com.smart.framework.auth.core.properties.AuthIgnoreProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

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
        for (ExtensionPathMatcher extensionPathMatcher : createExtensionPathMatchers(authIgnoreProperties)) {
            if (extensionPathMatcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建扩展路径匹配器
     * @param authIgnoreProperties 忽略配置
     * @return 扩展路径匹配器
     */
    public static List<ExtensionPathMatcher> createExtensionPathMatchers(AuthIgnoreProperties authIgnoreProperties) {
        List<ExtensionPathMatcher> extensionPathMatchers = new ArrayList<>(10);
        authIgnoreProperties.getGet().forEach(ignore -> extensionPathMatchers.add(new ExtensionPathMatcher(GET, ignore)));
        authIgnoreProperties.getPut().forEach(ignore -> extensionPathMatchers.add(new ExtensionPathMatcher(PUT, ignore)));
        authIgnoreProperties.getHead().forEach(ignore -> extensionPathMatchers.add(new ExtensionPathMatcher(HEAD, ignore)));
        authIgnoreProperties.getPost().forEach(ignore -> extensionPathMatchers.add(new ExtensionPathMatcher(POST, ignore)));
        authIgnoreProperties.getPatch().forEach(ignore -> extensionPathMatchers.add(new ExtensionPathMatcher(PATCH, ignore)));
        authIgnoreProperties.getTrace().forEach(ignore -> extensionPathMatchers.add(new ExtensionPathMatcher(TRACE, ignore)));
        authIgnoreProperties.getDelete().forEach(ignore -> extensionPathMatchers.add(new ExtensionPathMatcher(DELETE, ignore)));
        authIgnoreProperties.getOptions().forEach(ignore -> extensionPathMatchers.add(new ExtensionPathMatcher(OPTIONS, ignore)));
        authIgnoreProperties.getPattern().forEach(ignore -> extensionPathMatchers.add(new ExtensionPathMatcher(null, ignore)));
        return extensionPathMatchers;
    }
}
