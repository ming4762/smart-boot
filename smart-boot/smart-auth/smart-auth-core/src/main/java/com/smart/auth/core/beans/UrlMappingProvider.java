package com.smart.auth.core.beans;

import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ShiZhongMing
 * 2021/3/8 17:41
 * @since 1.0
 */
public interface UrlMappingProvider {

    /**
     * 匹配Mapping信息
     * @param request 请求信息
     * @return 匹配的结果，如果没有匹配上则返回null
     */
    @Nullable
    UrlMapping matchMapping(@NonNull HttpServletRequest request);

    /**
     * 获取所有的映射关系
     * @return 所有的映射关系
     */
    Multimap<String, UrlMapping> getAllMapping();

    /**
     * URL映射
     */
    @Getter
    @Setter
    class UrlMapping {
        /**
         * 请求方法
         */
        private RequestMethod requestMethod;

        /**
         * 执行目标方法
         */
        private HandlerMethod handlerMethod;
    }
}
