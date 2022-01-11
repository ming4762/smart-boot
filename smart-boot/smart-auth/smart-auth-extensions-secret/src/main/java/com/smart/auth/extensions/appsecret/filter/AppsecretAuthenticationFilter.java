package com.smart.auth.extensions.appsecret.filter;

import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.secret.data.AccessTokenStoreData;
import com.smart.auth.core.secret.store.AccessTokenStore;
import com.smart.auth.core.secret.store.AppNoncestrStore;
import com.smart.auth.core.utils.AuthCheckUtils;
import com.smart.auth.extensions.appsecret.authentication.AppsecretAuthenticationToken;
import com.smart.auth.extensions.appsecret.constants.AppsecretHttpStatus;
import com.smart.auth.extensions.appsecret.exception.AuthAppsecretException;
import com.smart.auth.extensions.appsecret.parameter.AppsecretCommonParameter;
import com.smart.auth.extensions.appsecret.sign.AppsecretSignModel;
import com.smart.auth.extensions.appsecret.sign.SignProvider;
import com.smart.commons.core.i18n.I18nUtils;
import com.smart.commons.core.utils.IpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Objects;

/**
 * Appsecret 验证过滤器
 * @author ShiZhongMing
 * @since 1.0.7
 */
public class AppsecretAuthenticationFilter extends OncePerRequestFilter {

    private static final int TIMESTAMP_LENGTH = 13;

    private final AuthProperties authProperties;

    private final AppNoncestrStore appNoncestrStore;

    private final AccessTokenStore accessTokenStore;

    private final SignProvider signProvider;

    public AppsecretAuthenticationFilter(AuthProperties authProperties, AppNoncestrStore appNoncestrStore, AccessTokenStore accessTokenStore, SignProvider signProvider) {
        this.authProperties = authProperties;
        this.appNoncestrStore = appNoncestrStore;
        this.accessTokenStore = accessTokenStore;
        this.signProvider = signProvider;
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        // 获取验证参数
        AppsecretCommonParameter parameter = AppsecretCommonParameter.createFromRequest(request);
        if (StringUtils.isAnyBlank(parameter.getTimestamp(), parameter.getNoncestr(), parameter.getSignature())) {
            throw new AuthAppsecretException(AppsecretHttpStatus.APP_SECRET_PARAMETER_DEFECT.getCode(), I18nUtils.get(AppsecretHttpStatus.APP_SECRET_PARAMETER_DEFECT));
        }
        // 验证时间戳是否过期
        if (!this.validateTimestamp(parameter)) {
            throw new AuthAppsecretException(AppsecretHttpStatus.APP_SECRET_VALIDATE_TIMESTAMP_ERROR.getCode(), I18nUtils.get(AppsecretHttpStatus.APP_SECRET_VALIDATE_TIMESTAMP_ERROR));
        }
        // 验证随机字符串是否使用
        if (this.appNoncestrStore.has(parameter.getNoncestr())) {
            throw new AuthAppsecretException(AppsecretHttpStatus.APP_SECRET_VALIDATE_NONCESTR_ERROR.getCode(), I18nUtils.get(AppsecretHttpStatus.APP_SECRET_VALIDATE_NONCESTR_ERROR));
       }
        // 获取accessToken信息
        AccessTokenStoreData accessTokenStoreData = this.accessTokenStore.validate(parameter.getAppId());
        if (accessTokenStoreData == null) {
            throw new AuthAppsecretException(AppsecretHttpStatus.APP_SECRET_VALIDATE_TOKEN_ERROR.getCode(), I18nUtils.get(AppsecretHttpStatus.APP_SECRET_VALIDATE_TOKEN_ERROR));
        }
        // 获取URL
        String url = request.getHeader(HttpHeaders.REFERER);
        // 创建第一个签名参数
        AppsecretSignModel signModel = new AppsecretSignModel(parameter.getTimestamp(), parameter.getNoncestr(), accessTokenStoreData.getAccessTokenList().get(0).getAccessToken(), url);
        // 签名验证
        String signature = this.signProvider.sign(signModel);
        boolean validate = !StringUtils.equals(signature, parameter.getSignature());
        // 如果没有验证成功，使用上一个accessToken验证签名
        if (!validate && accessTokenStoreData.getAccessTokenList().size() > 1) {
            signModel.setAccessToken(accessTokenStoreData.getAccessTokenList().get(1).getAccessToken());
            String signature2 = this.signProvider.sign(signModel);
            validate = !StringUtils.equals(signature2, parameter.getSignature());
        }
        if (!validate) {
            // 签名验证失败
            throw new AuthAppsecretException(AppsecretHttpStatus.APP_SECRET_VALIDATE_SIGNATURE_ERROR.getCode(), I18nUtils.get(AppsecretHttpStatus.APP_SECRET_VALIDATE_SIGNATURE_ERROR));
        }
        AppsecretAuthenticationToken token = new AppsecretAuthenticationToken(accessTokenStoreData.getApp(), null, IpUtils.getIpAddr(request), accessTokenStoreData.getApp().getAuthorities());
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);
        filterChain.doFilter(request, response);
    }

    /**
     * 验证时间戳
     */
    protected boolean validateTimestamp(@NonNull AppsecretCommonParameter parameter) {
        if (parameter.getTimestamp().length() != TIMESTAMP_LENGTH) {
            return false;
        }
        return Instant.now().isBefore(Instant.ofEpochMilli(Long.parseLong(parameter.getTimestamp())).plus(this.authProperties.getAppsecret().getTimestampTimeout()));
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return Objects.equals(this.authProperties.getDevelopment(), Boolean.TRUE) || AuthCheckUtils.checkIgnores(request, this.authProperties.getIgnores());
    }

}
