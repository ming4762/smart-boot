package com.smart.auth.security.temptoken;

import com.smart.auth.core.annotation.TempToken;
import com.smart.auth.core.model.TempTokenData;
import com.smart.auth.core.service.AuthCache;
import com.smart.commons.core.message.Result;
import com.smart.commons.core.utils.IpUtils;
import com.smart.commons.core.utils.RestJsonWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * temp token注册拦截器
 * @author ShiZhongMing
 * 2021/3/10 12:09
 * @since 1.0
 */
@Slf4j
public class TempTokenValidateInterceptor implements HandlerInterceptor {

    /**
     * 标识token的key
     */
    private static final String TEMP_TOKEN_KEY = "access-token";

    @Autowired
    private AuthCache<String, Object> authCache;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws IOException {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        final TempToken tempToken = this.hasValidate((HandlerMethod) handler);
        if (Objects.isNull(tempToken)) {
            return true;
        }
        final String result = this.doValidate(request, tempToken);
        if (StringUtils.isEmpty(result)) {
            // 验证通过
            return true;
        }
        RestJsonWriter.writeJson(response, Result.failure(403, result));
        return false;
    }

    /**
     * 进行临时token验证
     * @param request HttpServletRequest
     * @return 结果
     */
    protected String doValidate(@NonNull HttpServletRequest request, @NonNull TempToken tempToken) {
        String message = null;
        // 获取临时token
        String token = request.getParameter(TEMP_TOKEN_KEY);
        if (StringUtils.isBlank(token)) {
            message = "Temp Token validate fail，token is null";
            log.warn(message);
            return message;
        }
        // 从缓存中获取信息
        final TempTokenData data = (TempTokenData) this.authCache.get(token);
        if (Objects.isNull(data)) {
            message = "Temp Token validate fail，token is expire";
            log.warn(message);
            return message;
        }
        // IP 验证失败
        if (tempToken.ipValidate() && !StringUtils.equals(IpUtils.getIpAddr(request), data.getIp())) {
            message = String.format("Temp Token validate fail: IP validate fail, register IP:[%s], request IP: [%s]", data.getIp(), IpUtils.getIpAddr(request));
            log.warn(message);
            return message;
        }
        // 验证资源是否匹配
        if (!StringUtils.equals(tempToken.resource(), data.getResource())) {
            message = String.format("Temp Token validate fail: resource validate fail, register resource:[%s], request resource: [%s]", data.getResource(), tempToken.resource());
            log.warn(message);
        }
        if (StringUtils.isEmpty(message) && Boolean.TRUE.equals(data.getOnce())) {
            // 验证成功且token只使用一次，则删除token
            this.authCache.remove(token);
        }
        return message;
    }

    protected TempToken hasValidate(@NonNull HandlerMethod handler) {
        return AnnotationUtils.findAnnotation(handler.getMethod(), TempToken.class);
    }
}
