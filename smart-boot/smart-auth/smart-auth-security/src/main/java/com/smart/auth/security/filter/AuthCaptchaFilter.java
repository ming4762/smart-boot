package com.smart.auth.security.filter;

import com.smart.auth.core.exception.CaptchaException;
import com.smart.auth.core.i18n.AuthI18nMessage;
import com.smart.auth.core.service.AuthCache;
import com.smart.auth.core.utils.CaptchaUtils;
import com.smart.commons.core.i18n.I18nUtils;
import com.smart.commons.core.message.Result;
import com.smart.commons.core.utils.RestJsonWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * 验证码创建 验证拦截器
 * @author ShiZhongMing
 * 2022/1/22
 * @since 2.0.0
 */
public class AuthCaptchaFilter extends OncePerRequestFilter {

    private final String createUrl;

    private final List<String> loginUrls;

    private final AuthCache<String, Object> authCache;

    public AuthCaptchaFilter(String createUrl, List<String> loginUrls, AuthCache<String, Object> authCache) {
        this.createUrl = createUrl;
        this.loginUrls = loginUrls;
        this.authCache = authCache;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String key = request.getParameter("codeKey");
        Assert.notNull(key, "验证码验证发生错误，key为null");
        if (this.isCreate(request)) {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            CaptchaUtils.out(response.getOutputStream(), key, authCache);
        } else if (this.isValidate(request)) {
            // 验证验证码
            String code = request.getParameter("code");
            try {
                if (StringUtils.isBlank(code) || !CaptchaUtils.validate(key, code, authCache)) {
                    CaptchaUtils.invalid(key, authCache);
                    RestJsonWriter.writeJson(response, Result.failure(HttpStatus.UNAUTHORIZED.value(), I18nUtils.get(AuthI18nMessage.CAPTCHA_ERROR)));
                    return;
                }
            } catch (CaptchaException e) {
                RestJsonWriter.writeJson(response, Result.failure(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
                return;
            }
            CaptchaUtils.invalid(key, authCache);
            filterChain.doFilter(request, response);
        }
    }


    private boolean isCreate(@NonNull HttpServletRequest request) {
        return (new AntPathRequestMatcher(createUrl, HttpMethod.GET.name())).matches(request);
    }

    private boolean isValidate(@NonNull HttpServletRequest request) {
        return loginUrls.stream().anyMatch(item -> (new AntPathRequestMatcher(item)).matches(request));
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return !this.isCreate(request) && !isValidate(request);
    }
}
