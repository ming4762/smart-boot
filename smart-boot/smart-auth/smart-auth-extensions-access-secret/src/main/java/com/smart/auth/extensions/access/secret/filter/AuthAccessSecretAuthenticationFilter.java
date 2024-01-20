package com.smart.auth.extensions.access.secret.filter;

import com.smart.auth.core.exception.AuthException;
import com.smart.auth.core.i18n.AuthI18nMessage;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.secret.AccessSecretProvider;
import com.smart.auth.core.secret.data.AccessSecretData;
import com.smart.auth.core.service.AuthCache;
import com.smart.commons.core.i18n.I18nUtils;
import com.smart.commons.core.message.Result;
import com.smart.commons.core.utils.Base64Utils;
import com.smart.commons.core.utils.IpUtils;
import com.smart.commons.core.utils.RestJsonWriter;
import com.smart.commons.core.utils.auth.ShaUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author shizhongming
 * 2023/10/25 15:52
 * @since 3.0.0
 */
public class AuthAccessSecretAuthenticationFilter implements Filter {

    /**
     * 日期格式化
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("E, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);

    /**
     * 随机串key
     */
    private static final String NONCE_KEY = "nonce";

    private static final String ACCESS_SECRET_CACHE_KEY = "ACCESS_SECRET:";

    private static final int ERROR_CODE = 403;

    private static final String SPLIT = ":";

    private static final int TOKEN_LIST_SIZE = 3;

    private final AuthProperties authProperties;

    private final AccessSecretProvider accessSecretProvider;

    private final AuthCache<String, Object> authCache;

    public AuthAccessSecretAuthenticationFilter(AuthProperties authProperties, AccessSecretProvider accessSecretProvider, AuthCache<String, Object> authCache) {
        this.authProperties = authProperties;
        this.accessSecretProvider = accessSecretProvider;
        this.authCache = authCache;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            this.filter((HttpServletRequest) request);
            chain.doFilter(request, response);
        } catch (AuthException e) {
            RestJsonWriter.writeJson((HttpServletResponse) response, Result.failure(ERROR_CODE, e.getMessage()));
        }
    }

    private void filter(HttpServletRequest servletRequest) {
        String token =  servletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String httpMethod = servletRequest.getMethod();
        String contentType = servletRequest.getHeader(HttpHeaders.CONTENT_TYPE).split(";")[0];
        String date = servletRequest.getHeader(HttpHeaders.DATE);
        String nonce = servletRequest.getHeader(NONCE_KEY);

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
        if (!tokeList.get(0).equals(this.authProperties.getAccessSecret().getTokenPrefix())) {
            this.throwException(AuthI18nMessage.ACCESS_SECRET_FORMAT_ERROR);
        }
        String accessKey = tokeList.get(1);
        String sign = tokeList.get(2);

        AccessSecretData accessSecretData = this.getValidateAccessSecretData(accessKey);
        // 验证Access 状态
        if (accessSecretData.getExpireDate() != null && LocalDateTime.now().isAfter(accessSecretData.getExpireDate())) {
            this.throwException(AuthI18nMessage.ACCESS_SECRET_ACCESS_KEY_EXPIRE);
        }
        if (StringUtils.hasText(accessSecretData.getAccessIp())) {
            List<String> ipList = Arrays.asList(accessSecretData.getAccessIp().split(","));
            if (!ipList.contains(IpUtils.getIpAddr(servletRequest))) {
                this.throwException(AuthI18nMessage.ACCESS_SECRET_IP_UNAUTHORIZED);
            }
        }
        // 计算sign
        String encryptKey = String.join(SPLIT, List.of(httpMethod, contentType, date, nonce));
        String encodeSign = Base64Utils.encode(ShaUtils.hmacSha1Encrypt(accessSecretData.getSecretKey(), encryptKey));
        if (!sign.equals(encodeSign)) {
            this.throwException(AuthI18nMessage.ACCESS_SECRET_SIGN_ERROR);
        }
    }

    /**
     * 验证时间和随机串
     * @param date 时间
     * @param nonce 随机穿
     */
    private void validateDateNonce(String date, String nonce) {
        // 验证时间格式
        if(!StringUtils.hasText(date)) {
            this.throwException(AuthI18nMessage.ACCESS_SECRET_DATE_ERROR);
        }
        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.parse(date, DATE_FORMATTER);
        } catch (Exception e) {
            throw new AuthException(I18nUtils.get(AuthI18nMessage.ACCESS_SECRET_DATE_ERROR));
        }
        // 验证时间是否超出
        Duration expire = this.authProperties.getAccessSecret().getExpire();
        LocalDateTime now = LocalDateTime.now(ZoneId.of("GMT"));
        if (
                localDateTime.isAfter(now.plus(expire)) ||
                        localDateTime.isBefore(now.minus(expire))
        ) {
            this.throwException(AuthI18nMessage.ACCESS_SECRET_DATE_EXPIRE);
        }
        if (!StringUtils.hasText(nonce)) {
            this.throwException(AuthI18nMessage.ACCESS_SECRET_NONCE_ERROR);
        }
        // 验证随机串
        String nonceKey = this.getCacheKey(nonce);
        if (this.authCache.get(nonceKey) != null) {
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

    private String getCacheKey(String key) {
        return ACCESS_SECRET_CACHE_KEY + key;
    }
}
