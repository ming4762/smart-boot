package com.smart.auth.extensions.aksk.filter;

import com.smart.framework.auth.common.constants.AccessSignatureEnum;
import com.smart.framework.auth.common.exception.AuthException;
import com.smart.framework.auth.core.i18n.AuthI18nMessage;
import com.smart.framework.auth.core.properties.AuthProperties;
import com.smart.framework.auth.core.secret.AccessSecretProvider;
import com.smart.framework.auth.core.secret.data.AccessSecretData;
import com.smart.framework.auth.core.service.AuthCache;
import com.smart.framework.commons.core.dto.auth.AuthAkSkCreateTokenDTO;
import com.smart.framework.commons.core.http.RepeatReadBodyHttpServletRequest;
import com.smart.framework.commons.core.i18n.I18nUtils;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.commons.core.tenant.SmartTenantHolder;
import com.smart.framework.commons.core.utils.IpUtils;
import com.smart.framework.commons.core.utils.JsonSignerUtils;
import com.smart.framework.commons.core.utils.RestJsonWriter;
import com.smart.framework.commons.core.utils.auth.SecretUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author shizhongming
 * 2023/10/25 15:52
 * @since 3.0.0
 */
public class AuthAkskAuthenticationFilter implements Filter {

    /**
     * 日期格式化
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("E, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);

    private static final int ERROR_CODE = 403;

    private static final String SPLIT = ":";

    private static final int TOKEN_LIST_SIZE = 3;

    private static final String UNSIGNED_PAYLOAD = "UNSIGNED-PAYLOAD";

    private static final String ACCESS_SECRET_CACHE_KEY = "ACCESS_SECRET:";

    private final AuthProperties authProperties;

    private final AccessSecretProvider accessSecretProvider;

    private final AuthCache authCache;

    /**
     * 签名排除的key
     */
    private static final List<String> EXCLUDE_KEYS = Stream.concat(
            Stream.of(HttpHeaders.AUTHORIZATION),
            Arrays.stream(AccessSignatureEnum.values()).map(AccessSignatureEnum::getKey)
    ).toList();


    public AuthAkskAuthenticationFilter(AuthProperties authProperties, AccessSecretProvider accessSecretProvider, AuthCache authCache) {
        this.authProperties = authProperties;
        this.accessSecretProvider = accessSecretProvider;
        this.authCache = authCache;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            // 需要校验body，包装为可重复读取的request
            if (!this.isUnsignedPayload(httpServletRequest) && this.isJsonContentType(httpServletRequest)) {
                httpServletRequest = new RepeatReadBodyHttpServletRequest(httpServletRequest);
            }
            this.filter(httpServletRequest);
            chain.doFilter(httpServletRequest, response);
        } catch (AuthException e) {
            RestJsonWriter.writeJson((HttpServletResponse) response, Result.failure(ERROR_CODE, e.getMessage()));
        } finally {
            // 清除租户信息
            SmartTenantHolder.clear();
        }
    }

    private void filter(HttpServletRequest servletRequest) {
        String token = this.getParameter(servletRequest, HttpHeaders.AUTHORIZATION);
        String httpMethod = servletRequest.getMethod();
        String contentTypeHeader = Objects.requireNonNullElse(servletRequest.getHeader(HttpHeaders.CONTENT_TYPE), "");
        String contentType = contentTypeHeader.split(";")[0];
        String date = this.getParameter(servletRequest, HttpHeaders.DATE);
        String nonce = this.getParameter(servletRequest, AccessSignatureEnum.X_SIGNATURE_NONCE.getKey());

        if (!StringUtils.hasText(token)) {
            this.throwException(AuthI18nMessage.ACCESS_SECRET_TOKEN_EMPTY);
        }

        // 验证时间和随机串
        this.validateDateNonce(date, nonce);

        // 验证token格式
        List<String> tokeList = Arrays.asList(token.split(SPLIT));
        if (tokeList.size() != TOKEN_LIST_SIZE) {
            this.throwException(AuthI18nMessage.ACCESS_SECRET_FORMAT_ERROR);
        }
        if (!tokeList.getFirst().equals(this.authProperties.getAccessSecret().getTokenPrefix())) {
            this.throwException(AuthI18nMessage.ACCESS_SECRET_FORMAT_ERROR);
        }
        String accessKey = tokeList.get(1);

        AccessSecretData accessSecretData = this.getValidateAccessSecretData(accessKey);
        // 验证Access 状态
        if (accessSecretData.getExpireDate() != null && ZonedDateTime.now().isAfter(accessSecretData.getExpireDate())) {
            this.throwException(AuthI18nMessage.ACCESS_SECRET_ACCESS_KEY_EXPIRE);
        }
        if (StringUtils.hasText(accessSecretData.getAccessIp())) {
            List<String> ipList = Arrays.asList(accessSecretData.getAccessIp().split(","));
            if (!ipList.contains(IpUtils.getIpAddr(servletRequest))) {
                this.throwException(AuthI18nMessage.ACCESS_SECRET_IP_UNAUTHORIZED);
            }
        }
        String parameterStr = this.getParameterStr(servletRequest);
        // 计算sign
        String encodeSign = SecretUtils.createSign(
                AuthAkSkCreateTokenDTO.builder()
                        .httpMethod(HttpMethod.valueOf(httpMethod))
                        .contentType(contentType)
                        .nonce(nonce)
                        .parameterStr(parameterStr)
                        .prefix(this.authProperties.getAccessSecret().getTokenPrefix())
                        .accessKey(accessSecretData.getAccessKey())
                        .secretKey(accessSecretData.getSecretKey())
                        .build(),
                date
        );
        if (!token.equals(encodeSign)) {
            this.throwException(AuthI18nMessage.ACCESS_SECRET_SIGN_ERROR);
        }
        // 设置租户信息
        SmartTenantHolder.set(accessSecretData::getUserTenant);
    }

    /**
     * 获取参数
     * @param request request
     * @return 参数
     */
    private String getParameterStr(HttpServletRequest request) {
        String path = request.getServletPath();
        // 获取query参数
        String queryParameter = this.buildCanonicalQueryString(request);

        // 判断body是否需要签名
        String bodyStr = "";
        if (!this.isUnsignedPayload(request) && this.isJsonContentType(request)) {
            RepeatReadBodyHttpServletRequest repeatReadBodyHttpServletRequest = (RepeatReadBodyHttpServletRequest) request;
            bodyStr = JsonSignerUtils.canonicalJsonString(new String(repeatReadBodyHttpServletRequest.getBody(), StandardCharsets.UTF_8));
        }
        return Stream.of(path, queryParameter, bodyStr)
                .filter(StringUtils::hasText)
                .collect(Collectors.joining());
    }

    /**
     * 获取URL参数
     * @param request 请求
     * @return URL参数
     */
    private String buildCanonicalQueryString(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        List<String> sortedKeys = params.keySet().stream()
                .filter(item -> !EXCLUDE_KEYS.contains(item)).sorted().toList();

        List<String> pairs = new ArrayList<>();
        for (String key : sortedKeys) {
            for (String value : params.get(key)) {
                pairs.add(key + "=" + value);
            }
        }
        return String.join("&", pairs);
    }

    /**
     * 验证时间和随机串
     * @param date 时间
     * @param nonce 随机串
     */
    private void validateDateNonce(String date, String nonce) {
        // 验证时间格式
        if(!StringUtils.hasText(date)) {
            this.throwException(AuthI18nMessage.ACCESS_SECRET_DATE_ERROR);
        }
        ZonedDateTime zonedDateTime;
        try {
            zonedDateTime = ZonedDateTime.parse(date, DATE_FORMATTER);
        } catch (Exception e) {
            throw new AuthException(I18nUtils.get(AuthI18nMessage.ACCESS_SECRET_DATE_ERROR));
        }
        // 验证时间是否超出
        Duration expire = this.authProperties.getAccessSecret().getExpire();
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT"));
        if (
                zonedDateTime.isAfter(now.plus(expire)) ||
                        zonedDateTime.isBefore(now.minus(expire))
        ) {
            this.throwException(AuthI18nMessage.ACCESS_SECRET_DATE_EXPIRE);
        }
        if (!StringUtils.hasText(nonce)) {
            this.throwException(AuthI18nMessage.ACCESS_SECRET_NONCE_ERROR);
        }
        // 验证随机串
        String nonceKey = this.getCacheKey(nonce);
        if (this.authCache.getValue(nonceKey) != null) {
            this.throwException(AuthI18nMessage.ACCESS_SECRET_NONCE_USED);
        }
        this.authCache.put(nonceKey, nonce, this.authProperties.getAccessSecret().getExpire());
    }

    /**
     * 获取认证信息
     * @param accessKey access key
     * @return 认证信息
     */
    @NonNull
    private AccessSecretData getValidateAccessSecretData(@NonNull String accessKey) {
        AccessSecretData accessSecretData = this.accessSecretProvider.get(accessKey);
        if (accessSecretData == null) {
            this.throwException(AuthI18nMessage.ACCESS_SECRET_ACCESS_KEY_ERROR);
        }
        return accessSecretData;
    }

    private void throwException(AuthI18nMessage authI18nMessage) {
        throw new AuthException(I18nUtils.get(authI18nMessage));
    }

    /**
     * 获取参数，先从header中获取，再从parameter中获取
     * @param request 请求
     * @param parameterKey 参数键
     * @return 参数值
     */
    @Nullable
    private String getParameter(HttpServletRequest request, String parameterKey) {
        String parameter = request.getHeader(parameterKey);
        if (!StringUtils.hasText(parameter)) {
            parameter = request.getParameter(parameterKey);
            if (StringUtils.hasText(parameter)) {
                parameter = URLDecoder.decode(parameter, StandardCharsets.UTF_8);
            }
        }
        return parameter;
    }

    /**
     * 判断body是否需要签名
     * @param request 请求
     * @return 是否需要签名
     */
    private boolean isUnsignedPayload(HttpServletRequest request) {
        return UNSIGNED_PAYLOAD.equals(this.getParameter(request, AccessSignatureEnum.X_SIGNATURE_BODY_HASH.getKey()));
    }

    /**
     * 是否是json类型
     * @param request 请求
     * @return 是否是json类型
     */
    private boolean isJsonContentType(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.contains("json");
    }

    private String getCacheKey(String key) {
        return ACCESS_SECRET_CACHE_KEY + key;
    }
}
