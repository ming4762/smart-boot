package com.smart.monitor.client.core.interceptor;

import com.smart.commons.core.message.Result;
import com.smart.commons.core.utils.JsonUtils;
import com.smart.monitor.core.constants.CommonHeadersEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 监控端口权限控制
 * 监测token是否匹配
 * @author ShiZhongMing
 * 2022/10/24
 * @since 3.0.0
 */
@Slf4j
public class MonitorAuthInterceptor implements HandlerInterceptor {

    private final String token;

    public MonitorAuthInterceptor(String token) {
        this.token = token;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        if (StringUtils.isBlank(this.token)) {
            log.debug("application not set token");
            return true;
        }
        String serverToken = request.getHeader(CommonHeadersEnum.Monitor_Authorization.name());
        if (!this.token.equals(serverToken)) {
            log.warn("token not match, application token: {}, server token: {}", this.token, serverToken);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JsonUtils.toJsonString(Result.failure(403, "token not match")));
            return false;
        }
        return true;
    }
}
