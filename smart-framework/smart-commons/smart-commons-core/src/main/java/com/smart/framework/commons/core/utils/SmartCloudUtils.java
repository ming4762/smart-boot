package com.smart.framework.commons.core.utils;

import com.smart.framework.commons.core.constants.HttpHeaderConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Optional;

/**
 * 微服务模式下工具类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-11 16:08
 * @since 5.0.0
 */
public class SmartCloudUtils {

    private SmartCloudUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final String HEADER_SPLIT = ",";

    /**
     * 获取服务追踪信息
     * @return 服务追踪信息
     */
    public static List<String> getServiceTrace() {
        HttpServletRequest request = Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
        if (request == null) {
            return List.of();
        }
        String sourceHeader = request.getHeader(HttpHeaderConstants.SERVICE_TRACE);
        if (!StringUtils.hasText(sourceHeader)) {
            return List.of();
        }
        return List.of(sourceHeader.split(HEADER_SPLIT));
    }
}
