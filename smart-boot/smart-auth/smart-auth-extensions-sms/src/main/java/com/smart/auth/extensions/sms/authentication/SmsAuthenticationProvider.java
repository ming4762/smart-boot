package com.smart.auth.extensions.sms.authentication;

import com.smart.auth.core.i18n.AuthI18nMessage;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.extensions.sms.provider.SmsCreateValidateProvider;
import com.smart.auth.extensions.sms.userdetails.SmsUserDetailService;
import com.smart.commons.core.i18n.I18nUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Objects;

/**
 * @author ShiZhongMing
 * 2021/6/3 13:56
 * @since 1.0
 */
public class SmsAuthenticationProvider implements AuthenticationProvider {

    private final SmsUserDetailService smsUserDetailService;

    private final SmsCreateValidateProvider smsCreateValidateProvider;

    public SmsAuthenticationProvider(SmsUserDetailService smsUserDetailService, SmsCreateValidateProvider smsCreateValidateProvider) {
        this.smsUserDetailService = smsUserDetailService;
        this.smsCreateValidateProvider = smsCreateValidateProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取手机号
        final String phone = (String) authentication.getPrincipal();
        final String code = (String) authentication.getCredentials();
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(code)) {
            throw new BadCredentialsException(I18nUtils.get(AuthI18nMessage.PHONE_CODE_NULL));
        }
        // 验证验证码
        boolean valid = this.smsCreateValidateProvider.validate(phone, code);
        if (!valid) {
            throw new BadCredentialsException(I18nUtils.get(AuthI18nMessage.PHONE_CODE_ERROR));
        }
        // 通过手机号查询用户
        final RestUserDetails restUserDetails = (RestUserDetails) this.smsUserDetailService.loadUserByMobile(phone);
        if (Objects.isNull(restUserDetails)) {
            throw new BadCredentialsException(I18nUtils.get(AuthI18nMessage.PHONE_CODE_ERROR));
        }
        // 鉴权成功
        final SmsAuthenticationToken token = new SmsAuthenticationToken(restUserDetails, code, restUserDetails.getAuthorities());
        token.setDetails(authentication.getDetails());
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
