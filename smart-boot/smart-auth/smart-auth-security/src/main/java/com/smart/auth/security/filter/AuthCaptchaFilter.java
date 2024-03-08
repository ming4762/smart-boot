package com.smart.auth.security.filter;

import com.smart.auth.core.i18n.AuthI18nMessage;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.captcha.exception.CaptchaException;
import com.smart.commons.core.captcha.dto.CaptchaGenerateDTO;
import com.smart.commons.core.captcha.dto.CaptchaGenerateParameter;
import com.smart.commons.core.captcha.dto.CaptchaValidateParameter;
import com.smart.commons.core.i18n.I18nUtils;
import com.smart.commons.core.message.Result;
import com.smart.commons.core.utils.JsonUtils;
import com.smart.commons.core.utils.RestJsonWriter;
import com.smart.module.api.auth.AuthCaptchaApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 验证码创建 验证拦截器
 * @author ShiZhongMing
 * 2022/1/22
 * @since 2.0.0
 */
public class AuthCaptchaFilter extends OncePerRequestFilter {

    private final AuthProperties authProperties;

    private final AuthCaptchaApi authCaptchaApi;

    public AuthCaptchaFilter(AuthProperties authProperties, AuthCaptchaApi authCaptchaApi) {
        this.authCaptchaApi = authCaptchaApi;
        this.authProperties = authProperties;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (this.isCreate(request)) {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            CaptchaGenerateParameter generateParameter = this.authProperties.getCaptcha().getType().isText() ? this.createTextParameter() : this.createImageParameter();
            CaptchaGenerateDTO result = this.authCaptchaApi.generate(generateParameter);
            RestJsonWriter.writeJson(response, Result.success(result));
        } else if (this.isValidate(request)) {
            // 验证验证码
            String code = request.getParameter("code");
            Result<Object> errorResult = Result.failure(HttpStatus.UNAUTHORIZED.value(), I18nUtils.get(AuthI18nMessage.CAPTCHA_ERROR));
            if (!StringUtils.hasText(code)) {
                RestJsonWriter.writeJson(response, errorResult);
                return;
            }
            try {
                CaptchaValidateParameter validateParameter = JsonUtils.parse(code, CaptchaValidateParameter.class);
                boolean validate = this.authCaptchaApi.validate(validateParameter);
                if (!validate) {
                    RestJsonWriter.writeJson(response, errorResult);
                    return;
                }
            } catch (CaptchaException e) {
                RestJsonWriter.writeJson(response, Result.failure(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
                return;
            }
            filterChain.doFilter(request, response);
        }
    }


    private CaptchaGenerateParameter createTextParameter() {
        CaptchaGenerateParameter.TextParameter textParameter = new CaptchaGenerateParameter.TextParameter();
        BeanUtils.copyProperties(this.authProperties.getCaptcha().getText(), textParameter);
        return CaptchaGenerateParameter.builder()
                .type(this.authProperties.getCaptcha().getType())
                .text(textParameter)
                .build();
    }

    private CaptchaGenerateParameter createImageParameter() {
        CaptchaGenerateParameter.ImageParameter imageParameter = new CaptchaGenerateParameter.ImageParameter();
        BeanUtils.copyProperties(this.authProperties.getCaptcha().getImage(), imageParameter);
        return CaptchaGenerateParameter.builder()
                .type(this.authProperties.getCaptcha().getType())
                .image(imageParameter)
                .build();
    }

    private boolean isCreate(@NonNull HttpServletRequest request) {
        return (new AntPathRequestMatcher(this.authProperties.getCaptcha().getCreateUrl(), HttpMethod.POST.name())).matches(request);
    }

    private boolean isValidate(@NonNull HttpServletRequest request) {
        return new AntPathRequestMatcher(this.authProperties.getLoginUrl()).matches(request);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return !this.isCreate(request) && !isValidate(request);
    }
}
