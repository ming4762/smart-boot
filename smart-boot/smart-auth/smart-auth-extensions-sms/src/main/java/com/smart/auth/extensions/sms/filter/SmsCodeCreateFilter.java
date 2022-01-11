package com.smart.auth.extensions.sms.filter;

import com.smart.auth.core.i18n.AuthI18nMessage;
import com.smart.auth.extensions.sms.provider.SmsCreateValidateProvider;
import com.smart.commons.core.i18n.I18nUtils;
import com.smart.commons.core.message.Result;
import com.smart.commons.core.utils.RestJsonWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码创建拦截器
 * @author ShiZhongMing
 * 2021/6/3 16:57
 * @since 1.0
 */
public class SmsCodeCreateFilter extends OncePerRequestFilter {

    private final SmsCreateValidateProvider smsCreateValidateProvider;

    public SmsCodeCreateFilter(SmsCreateValidateProvider smsCreateValidateProvider) {
        this.smsCreateValidateProvider = smsCreateValidateProvider;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        // 1、获取手机号
        final String phone = request.getParameter("phone");
        if (StringUtils.isBlank(phone)) {
            throw new BadCredentialsException(I18nUtils.get(AuthI18nMessage.PHONE_NULL));
        }
        // 2、发送短信
        this.smsCreateValidateProvider.create(phone);
        RestJsonWriter.writeJson(response, Result.success());
    }
}
